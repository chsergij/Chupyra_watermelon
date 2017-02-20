package softgroup.chupyra_watermelon;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;



public class RVAdapter extends RecyclerView.Adapter<PhonebookViewHolder> {

    List<PhonebookContactModel> mPhonebookContacts;

    public RVAdapter(List<PhonebookContactModel> mPhonebookContacts){
        this.mPhonebookContacts = mPhonebookContacts;
    }

    @Override
    public void onBindViewHolder(PhonebookViewHolder phonebookViewHolder, int i) {
        final PhonebookContactModel model = mPhonebookContacts.get(i);
        phonebookViewHolder.bind(model);

    }

    @Override
    public PhonebookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new PhonebookViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mPhonebookContacts.size();
    }

    public void setFilter(List<PhonebookContactModel> mPhonebookContacts) {
        mPhonebookContacts = new ArrayList<>();
        mPhonebookContacts.addAll(mPhonebookContacts);
        notifyDataSetChanged();
    }

}
