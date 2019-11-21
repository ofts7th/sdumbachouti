package cs.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cs.data.DataTable;
import cs.mobile.WebViewActivity;
import cs.string;

public class WebViewActivityUtil {
    private WebViewActivity _activity;
    private WebView _view;

    public WebViewActivityUtil(WebViewActivity activity) {
        _activity = activity;
    }

    public void setWebView(WebView view) {
        _view = view;
    }

    public void callJsFunc(String func) {
        if (_view == null)
            return;

        _view.loadUrl("javascript:" + func);
    }

    public void finishActivity() {
        this._activity.finish();
    }

    File currentDir = Environment.getExternalStorageDirectory();
    String rootDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public String getChild(String s) {
        if (s.equals("")) {
            return getChildDir(currentDir);
        }

        if (s.equals("..")) {
            if (currentDir.getAbsolutePath().equals(rootDirPath)) {
                return getChildDir(currentDir);
            } else {
                currentDir = currentDir.getParentFile();
                return getChildDir(currentDir);
            }
        }
        for (File sf : currentDir.listFiles()) {
            if (sf.isDirectory() && sf.getName().equals(s)) {
                currentDir = sf;
                return getChildDir(currentDir);
            }
        }
        return "";
    }

    public String getChildDir(File f) {
        ArrayList<String> dirs = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        File[] children = f.listFiles();
        if (children == null)
            return "";
        for (File sf : children) {
            String name = sf.getName();
            if (sf.isDirectory()) {
                if (!name.startsWith(".") && name.trim().length() > 0) {
                    dirs.add(name);
                }
            } else {
                if (!name.startsWith(".") && name.trim().length() > 0) {
                    files.add(name);
                }
            }
        }
        String s = "";
        Collections.sort(dirs);
        for (int i = 0; i < dirs.size(); i++) {
            if (i > 0) {
                s += ",";
            }
            s += dirs.get(i);
        }
        s += ";";
        Collections.sort(files);
        for (int i = 0; i < files.size(); i++) {
            if (i > 0) {
                s += ",";
            }
            s += files.get(i);
        }
        s += ";";

        s += currentDir.getAbsolutePath();
        return s;
    }

    DataTable dataTable = null;

    public int getTiMuCount() {
        if (dataTable != null)
            return dataTable.rows.size();

        String path = Util.getConfig("configDataFilePath");
        if (string.IsNullOrEmpty(path))
            return 0;

        File file = new File(path);
        if (!file.exists())
            return 0;

        dataTable = ExcelManager.readExcelFile(file);
        if (dataTable != null)
            return dataTable.rows.size();

        return 0;
    }

    public String getTiMu(int tiMuId) {
        if (dataTable == null)
            return "";
        ArrayList<Map<String, Object>> rows = dataTable.select(x -> Util.parseIntFromDataRow(x, "Id") == tiMuId);
        if (rows.size() > 0) {
            return rows.get(0).get("Content").toString();
        }
        return "";
    }
}