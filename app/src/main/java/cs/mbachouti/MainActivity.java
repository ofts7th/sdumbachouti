package cs.mbachouti;

import android.os.Bundle;

import java.util.Date;

import cs.mobile.WebViewActivity;
import cs.util.DbHelper;
import cs.util.Util;

public class MainActivity extends WebViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DbHelper.execNonquery("delete from cthistory where ctdate < '" + Util.formatDate(new Date()) + "'");
        super.onCreate(savedInstanceState);
        startWebView();
    }
}