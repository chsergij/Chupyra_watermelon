package softgroup.chupyra_watermelon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public String getWatermelonData_JSON_str() {
        return sharedPreferences.getString(getResources().getString(R.string.json_key), "");
    }

    public void setWatermelonData (List<WatermelonModel> mWatermelonModel) {
        JSONArray jsonArray = new JSONArray();
        WatermelonModel watermelon;
        try{
            for(int i=0; i<mWatermelonModel.size();i++) {
                JSONObject jsonObject = new JSONObject();
                watermelon = mWatermelonModel.get(i);
                jsonObject.put(getResources().getString(R.string.variety_field), watermelon.getVariety());
                jsonObject.put(getResources().getString(R.string.photoId_field), watermelon.getPhotoId());
                jsonArray.put(jsonObject);
                jsonObject = null;
            }
            sharedPrefEditor.putString(getResources().getString(R.string.json_key), jsonArray.toString());
            sharedPrefEditor.commit();
        } catch (JSONException e){}
    }

    public void saveWatermelonToDB(WatermelonModel watermelonModel) {
        WatermelonDBRec watermelonDBRec = new WatermelonDBRec();
        watermelonDBRec.variety = watermelonModel.variety;
        watermelonDBRec.photoId = watermelonModel.photoId;
        watermelonDBRec.save();
        watermelonModel.setId(watermelonDBRec.getId());
    }

    public List<WatermelonDBRec> getAllWatermelonDBRec() {
        return new Select()
                .from(WatermelonDBRec.class)
                .execute();
    }

    public void removeWatermelonFromDB (long id) {
        WatermelonDBRec item = WatermelonDBRec.load(WatermelonDBRec.class,id);
        item.delete();
    }

    public void updateWatermelonInDB(WatermelonModel watermelonModel) {
        WatermelonDBRec item =  new Select()
            .from(WatermelonDBRec.class)
            .where("Id = ?", watermelonModel.getId())
            .executeSingle();
        item.variety = watermelonModel.getVariety();
        item.photoId = (int) watermelonModel.getPhotoId();
        item.save();

    }

    public int getSelectedRVPosition() {
        return this.selectedRVPosition;
    }

    public void setSelectedRVPosition(int selectedRVPosition) {
        this.selectedRVPosition = selectedRVPosition;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
