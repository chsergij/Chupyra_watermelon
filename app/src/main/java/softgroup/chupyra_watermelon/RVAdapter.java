package softgroup.chupyra_watermelon;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import softgroup.chupyra_watermelon.R;



public class RVAdapter extends RecyclerView.Adapter<WatermelonViewHolder> {

    List<WatermelonModel> mWatermelonModel;

    public RVAdapter(List<WatermelonModel> mWatermelonModel){
        this.mWatermelonModel = mWatermelonModel;
    }

    @Override
    public void onBindViewHolder(WatermelonViewHolder watermelonViewHolder, int i) {
        final WatermelonModel model = mWatermelonModel.get(i);
        watermelonViewHolder.bind(model);

    }

    @Override
    public WatermelonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new WatermelonViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mWatermelonModel.size();
    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }

    public void setFilter(List<WatermelonModel> watermelonModels) {
        mWatermelonModel = new ArrayList<>();
        mWatermelonModel.addAll(watermelonModels);
        notifyDataSetChanged();
    }

}
