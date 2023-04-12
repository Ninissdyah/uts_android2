package com.example.uts_android2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<ModelInput> modelInputList = new ArrayList<>();
    private Context mContext;

    public DataAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataAdapter(ArrayList<ModelInput> items) {
        modelInputList.clear();
        modelInputList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vaksin, parent, false);
        return new DataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelInput data = modelInputList.get(position);

        holder.tvDate.setText(data.getTglVaksin());
        holder.tvName.setText(data.getNama());
        holder.tvNik.setText(data.getNik());
        holder.tvTanggalLahir.setText(data.getTtl());
    }


    @Override
    public int getItemCount() {
        return modelInputList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvName;
        public TextView tvNik;
        public TextView tvTanggalLahir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvNama);
            tvNik = itemView.findViewById(R.id.tvNik);
            tvTanggalLahir = itemView.findViewById(R.id.tvTanggalLahir);
        }
    }

}
