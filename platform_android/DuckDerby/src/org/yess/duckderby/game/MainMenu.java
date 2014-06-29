package org.yess.duckderby.game;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenu extends Activity implements View.OnClickListener {

	private String donateUrl = "https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=237442304";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.menu_main);

        final Button playButton= (Button)findViewById(R.id.play_button);
        final Button highScoreButton = (Button)findViewById(R.id.top_score_button);
        final Button websiteButton= (Button)findViewById(R.id.website_button);
        final Button donateButton= (Button)findViewById(R.id.donate_button);

        SetDonateButtonValues(donateButton);

        playButton.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);
        websiteButton.setOnClickListener(this);
        donateButton.setOnClickListener(this);

        ImageView banner = (ImageView)findViewById(R.id.logo_image);
        banner.setImageResource(R.drawable.duck_derby_banner);

        ImageView gameImage = (ImageView)findViewById(R.id.game_image);
        gameImage.setImageResource(R.drawable.duck_derby_logo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_button:
                Intent intent = new Intent(this, DuckDerbyGame.class);
                startActivity(intent);
                break;
            case R.id.top_score_button:
            	Intent highScoreIntent = new Intent(this, HighScore.class);
            	startActivity(highScoreIntent);
            	break;
            case R.id.website_button:
                goToUrl("http://www.yessiowa.org");
                break;
            case R.id.donate_button:
                goToUrl(donateUrl);
                break;
        }
    }

    private void goToUrl(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void SetDonateButtonValues(Button donateButton)
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
}
