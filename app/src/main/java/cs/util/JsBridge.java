package cs.util;

import android.webkit.JavascriptInterface;

public class JsBridge {
    private WebViewActivityUtil util;

    public JsBridge(WebViewActivityUtil util) {
        this.util = util;
    }

    @JavascriptInterface
    public String readAssetFile(String path) {
        return Util.readAssetFile(path);
    }

    @JavascriptInterface
    public void exit() {
        this.util.finishActivity();
    }

    @JavascriptInterface
    public String getConfig(String k) {
        return Util.getSafeConfig(k);
    }

    @JavascriptInterface
    public void saveConfig(String k, String v) {
        Util.saveConfig(k, v);
    }

    @JavascriptInterface
    public void exitApp() {
        System.exit(0);
    }

    @JavascriptInterface
    public String getChild(String s) {
        return this.util.getChild(s);
    }

    @JavascriptInterface
    public String getTiMuIds() {
        return this.util.getTiMuIds();
    }

    @JavascriptInterface
    public String getTiMu(int id) {
        return this.util.getTiMu(id);
    }

    @JavascriptInterface
    public String getTodayCtRecord() {
        return this.util.getTodayCtRecord();
    }

    @JavascriptInterface
    public void addTodayCtRecord(int id) {
        this.util.addTodayCtRecord(id);
    }

    @JavascriptInterface
    public void clearCtRecord() {
        this.util.clearCtRecord();
    }
}
