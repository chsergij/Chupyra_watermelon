package softgroup.chupyra_watermelon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private int selectedRVPosition;
    private DESCryptoProvider myDESCryptoProvider;


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.watermelon_data), Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
        myDESCryptoProvider = new DESCryptoProvider(this);
        ActiveAndroid.initialize(this);
    }

    public String getUserName() {
        return sharedPreferences.getString(getResources().getString(R.string.nameKey), "");
    }

    public void setUserName(String newUserName) {
        sharedPrefEditor.putString(getResources().getString(R.string.nameKey), newUserName);
        sharedPrefEditor.commit();
    }

    public String getUserPassword() {
        return myDESCryptoProvider.decrypt(sharedPreferences.getString(getResources().getString(R.string.passwordKey), ""));
    }

    public void setUserPassword(String newUserPassword) {
        sharedPrefEditor.putString(getResources().getString(R.string.passwordKey), myDESCryptoProvider.encrypt(newUserPassword));
        sharedPrefEditor.commit();
}


    public void savePhonebookContactToDB(PhonebookContactModel phonebookContact) {
        PhonebookContactDBRec phonebookDBRec = new PhonebookContactDBRec();
        phonebookDBRec.name = phonebookContact.getName();
        phonebookDBRec.phone = phonebookContact.getPhone();
        phonebookDBRec.readonly = phonebookContact.getReadonlyStatus();
        phonebookDBRec.save();
        phonebookContact.setId(phonebookDBRec.getId());
    }

    public List<PhonebookContactDBRec> getAllPhonebookContactsDBRec() {
        return new Select()
                .from(PhonebookContactDBRec.class)
                .execute();
    }

    public void removePhonebookContactFromDB (long id) {
        PhonebookContactDBRec phonebookDBRec = PhonebookContactDBRec.load(PhonebookContactDBRec.class,id);
        if (!phonebookDBRec.readonly) phonebookDBRec.delete();
    }

    public void updatePhonebookContactInDB(PhonebookContactModel phonebookContact) {
        PhonebookContactDBRec phonebookDBRec =  new Select()
            .from(PhonebookContactDBRec.class)
            .where("Id = ?", phonebookContact.getId())
            .executeSingle();
        if (!phonebookDBRec.readonly) {
            phonebookDBRec.name = phonebookContact.getName();
            phonebookDBRec.phone = phonebookContact.getPhone();
            phonebookDBRec.save();
        }
    }

    public int getSelectedRVPosition() {
        return this.selectedRVPosition;
    }

    public void setSelectedRVPosition(int selectedRVPosition) {
        this.selectedRVPosition = selectedRVPosition;
    }

    public boolean getUserLogStatus() {
        return sharedPreferences.getString(getResources().getString(R.string.userLogStatusKey), "").equals(getResources().getString(R.string.userLogStatusLogged));
    }

    public void setUserLogStatus(boolean newLogStatus) {
        String loggedStatus = newLogStatus? getResources().getString(R.string.userLogStatusLogged) : getResources().getString(R.string.userLogStatusUnLogged);
        sharedPrefEditor.putString(getResources().getString(R.string.userLogStatusKey), loggedStatus);
        sharedPrefEditor.commit();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
