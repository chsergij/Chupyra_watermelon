package softgroup.chupyra_watermelon;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WatermelonViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView variety;
    ImageView photoId;

    WatermelonViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById((R.id.cv));
        variety = (TextView)itemView.findViewById(R.id.watermelonVariety);
        photoId = (ImageView)itemView.findViewById(R.id.watermelonPhotoId);
    }
    public void bind(WatermelonModel watermelonModel) {
        variety.setText(watermelonModel.getVariety());
        photoId.setImageResource((int) watermelonModel.getPhotoId());
    }
}
