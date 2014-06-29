package org.yessiowa.duckderby.teamduck.game;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import game.duckderby.yessiowa.org.duckderby3.R;

public class Game extends ActionBarActivity {

    private MediaPlayer quackMedia ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        quackMedia = MediaPlayer.create(getApplicationContext(), R.raw.quack);

        WebView webView = (WebView)findViewById(R.id.game_web_view);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        String file = "File:///android_asset/www/index.html";
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);


        class JsObject {
            @JavascriptInterface
            public void playQuack() { quackSound();};

            @JavascriptInterface
            public void goBack(){
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                finish();
            }
        }

        webView.addJavascriptInterface(new JsObject(), "JSInterface");

        webView.loadUrl(file);


    }

    private void quackSound(){
        quackMedia.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
