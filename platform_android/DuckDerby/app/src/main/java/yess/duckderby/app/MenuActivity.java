package yess.duckderby.app;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MenuActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        final Button playButton= (Button)findViewById(R.id.play_button);
        final Button websiteButton= (Button)findViewById(R.id.website_button);
        final Button donateButton= (Button)findViewById(R.id.donate_button);

        playButton.setOnClickListener(this);
        websiteButton.setOnClickListener(this);
        donateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.website_button:
                goToUrl("http://www.yessiowa.org");
                break;
            case R.id.donate_button:
                goToUrl("https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=237442304");
                break;
        }
    }

    private void goToUrl(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
