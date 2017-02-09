package softgroup.chupyra_watermelon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextPassword2;

    Toolbar reg_Toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        reg_Toolbar = (Toolbar) findViewById(R.id.reg_toolbar);
    }

    private String getMessagePart(String message, String logDataField, int resourceStr) {
        String newMessage = message;
        if (logDataField.isEmpty()) {
            newMessage += newMessage.isEmpty() ? "" : ", ";
            newMessage += getResources().getString(resourceStr);
        }
        return newMessage;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonOK){
            String userName = editTextName.getText().toString();
            String userPassword = editTextPassword.getText().toString();
            String userPassword2 = editTextPassword2.getText().toString();
            String message = "";
            if (!userPassword.equals(userPassword2)) {
                message = getResources().getString(R.string.differentPasswords);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else {
                message = getMessagePart(message, userName, R.string.name);
                message = getMessagePart(message, userPassword, R.string.password);
                if (!message.isEmpty()) {
                    message = getResources().getString(R.string.specify) + " " + message;
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    MyApplication myApp = ((MyApplication) getApplicationContext());
                    myApp.setUserName(userName);
                    myApp.setUserPassword(userPassword);
                    message = getResources().getString(R.string.loginDataSaved);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
    }

}
