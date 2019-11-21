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
    public int getTiMuCount() {
        return this.util.getTiMuCount();
    }

    @JavascriptInterface
    public String getTiMu(int id) {
        return this.util.getTiMu(id);
    }
}
