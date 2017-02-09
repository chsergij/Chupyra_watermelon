package softgroup.chupyra_watermelon;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class WelcomeActivity extends AppCompatActivity {

    Toolbar welcomeToolbar ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeToolbar = (Toolbar) findViewById(R.id.welcome_toolbar);
        setSupportActionBar(welcomeToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



//        Bundle extras = getIntent().getExtras();
//        String title = getResources().getString(R.string.LoggedPageTitle) + " '";
//        setTitle(title + extras.getString(getResources().getString(R.string.nameKey)) + "'");
    }
}
