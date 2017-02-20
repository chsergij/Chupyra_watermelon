package softgroup.chupyra_watermelon;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhonebookViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView tvName;
    TextView tvPhone;


    PhonebookViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById((R.id.cv));
        tvName = (TextView)itemView.findViewById(R.id.tvName);
        tvPhone= (TextView)itemView.findViewById(R.id.tvPhone);
    }
    public void bind(PhonebookContactModel phonebookContact) {
        tvName.setText(phonebookContact.getName());
        tvPhone.setText(phonebookContact.getPhone());
    }
}
