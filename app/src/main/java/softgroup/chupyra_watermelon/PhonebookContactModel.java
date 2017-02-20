package softgroup.chupyra_watermelon;

public class PhonebookContactModel {
    private String mName;
    private String mPhone;
    private boolean mReadonly;
    private long mId;

    public PhonebookContactModel(String name, String phone, boolean readonly) {
        mName = name;
        mPhone = phone;
        mReadonly = readonly;
    }

    public PhonebookContactModel(String name, String phone, boolean readonly, long id) {
        mName = name;
        mPhone = phone;
        mReadonly = readonly;
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }
    public String getName() {
        return mName;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }
    public String getPhone() {
        return mPhone;
    }

    public void setId(long id) {
        mId = id;
    }
    public long getId() {
        return mId;
    }

    public void setReadonlyStatus(boolean readonly) {
        mReadonly = readonly;
    }
    public boolean getReadonlyStatus() {
        return mReadonly;
    }

}