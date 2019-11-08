package cs.mbachouti;

import android.os.Bundle;

import cs.mobile.WebViewActivity;

public class MainActivity extends WebViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startWebView();
    }
}