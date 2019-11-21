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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public String getTiMuIds() {
        if (dataTable != null)
            return getTiMuIds(dataTable);

        String path = Util.getConfig("configDataFilePath");
        if (string.IsNullOrEmpty(path))
            return "";

        File file = new File(path);
        if (!file.exists())
            return "";

        dataTable = ExcelManager.readExcelFile(file);
        if (dataTable != null)
            return getTiMuIds(dataTable);

        return "";
    }

    String getTiMuIds(DataTable dt) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Map<String, Object> row : dt.rows) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(",");
            }
            sb.append(row.get("Id"));
        }
        return sb.toString();
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

    public String getTodayCtRecord() {
        List<List<String>> ret = DbHelper.query("select id, ctdate from cthistory");
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (List<String> row : ret) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(",");
            }
            sb.append(row.get(0));
        }
        return sb.toString();
    }

    public void addTodayCtRecord(int id) {
        DbHelper.execNonquery("insert into cthistory(id, ctdate) values (" + id + ",'" + Util.formatDate(new Date()) + "')");
    }

    public void clearCtRecord() {
        DbHelper.execNonquery("delete from cthistory");
    }
}