package softgroup.chupyra_watermelon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements OnClickListener{
    private EditText editTextName;
    private EditText editTextPassword;
    private String activeUserName;
    private String activeUserPassword;

    Toolbar loginToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
    }

    private boolean checkUser(String userName, String userPassword) {
        return userName.equals(activeUserName) && userPassword.equals(activeUserPassword);
    }

    @Override
    public void onClick(View v) {

        String userName = editTextName.getText().toString();
        String userPassword = editTextPassword.getText().toString();
        String message;

        switch (v.getId()) {
            case R.id.buttonLogin:
                if (checkUser(userName, userPassword)) {
                    Intent intent;
                    intent = new Intent(this, WelcomeActivity.class);
                    intent.putExtra(getResources().getString(R.string.nameKey), userName);
                    startActivity(intent);
                } else {
                    message = getResources().getString(R.string.loginError);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonRegistration:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication myApp = ((MyApplication) getApplicationContext());
        activeUserName = myApp.getUserName();
        editTextName.setText(activeUserName);
        activeUserPassword = myApp.getUserPassword();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editTextPassword.setText(null);
    }

}

