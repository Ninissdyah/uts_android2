package com.example.uts_android2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<ModelMain> modelMainList;
    private Context mContext;

    public MainAdapter(List<ModelMain> modelMainList, Context mContext) {
        this.modelMainList = modelMainList;
        this.mContext = mContext;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final modelMain data = modelMainList.get(position);

        if (data.getStrPhoto() !=null) {
            holder.tvStatus.setText("Available");
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            Glide.with(mContext).load(ApiClient.PHOTO +data.getStrPhoto() + ApiClient.API_KEY).transform(new CenterCrop(), new RoundedCorners(25)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePlace);

            holder.CVlistMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.namaRS = modelMainList.get(position).getStrName();
                    Intent intent = new Intent(mContext, InputDataActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.tvStatus.setText("Not  Available");
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            Glide.with(mContext).load(R.drawable.ic_hospital).transform(new CenterCrop(), new RoundedCorners(25)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePlace);

            holder.CVlistMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Vaksinasi tidak tersedia di sini!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.tvPlaceName.setText(((ModelMain) data).getStrName());
        holder.tvVicinity.setText(((ModelMain) data).getStrVicinity());
    }

    @Override
    public int getItemCount() {
        return modelMainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView CVlistMain;
        public ImageView imagePlace;
        public TextView tvPlaceName;
        public TextView tvVicinity;
        public TextView tvStatus;

        public ViewHolder(@NonNull View itemView, CardView CVlistMain, ImageView imagePlace, TextView tvPlaceName, TextView tvVicinity, TextView tvStatus) {
            super(itemView);
            this.CVlistMain = CVlistMain;
            this.imagePlace = imagePlace;
            this.tvPlaceName = tvPlaceName;
            this.tvVicinity = tvVicinity;
            this.tvStatus = tvStatus;
        }




    }


}
