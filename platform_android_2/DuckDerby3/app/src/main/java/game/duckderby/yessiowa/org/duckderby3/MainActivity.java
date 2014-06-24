package game.duckderby.yessiowa.org.duckderby3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private String donateUrl = "https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=237442304";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDonateButtonValues((Button)findViewById(R.id.button4));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    public void onGoToWebsiteClick(View view){
        goToUrl("http://www.yessiowa.org");

    }
    public void onDonateClick(View view){
        goToUrl(donateUrl);

    }


    private void goToUrl(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void setDonateButtonValues(Button donateButton)
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 17);
        Date startDate = cal.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.YEAR, year);
        calEnd.set(Calendar.MONTH, 4);
        calEnd.set(Calendar.DATE, 8);
        Date endDate = calEnd.getTime();

        Date today = new Date();

        if (today.compareTo(startDate) >= 0 && today.compareTo(endDate) < 0)
        {
            donateButton.setText("Adopt a Duck");
            donateUrl = "http://www.yessduckderby.org";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_exit){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateToGame(View view) {
        Intent gameIntent = new Intent(this, Game.class);
        startActivity(gameIntent);

    }
}
