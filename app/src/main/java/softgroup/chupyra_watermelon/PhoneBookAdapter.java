package softgroup.chupyra_watermelon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PhoneBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhonebookContactModel> mListPhoneBookContacts;

    public PhoneBookAdapter(Context context, List<PhonebookContactModel> list) {
        mContext = context;
        mListPhoneBookContacts = list;

    }

    @Override
    public int getCount() {
        return mListPhoneBookContacts.size();
    }

    @Override
    public Object getItem(int pos) {
        return mListPhoneBookContacts.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // get selected entry
        PhonebookContactModel entry = mListPhoneBookContacts.get(pos);

        // inflating list view layout if null
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.recycler_item, null);
        }

        // set name
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getName());

        // set phone
        TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getPhone());

        return convertView;
    }

}