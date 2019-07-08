package com.dusky.world.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dusky.world.Module.entity.TimeAxisItem;
import com.dusky.world.R;

import java.util.List;

public class TimeAxisAdapter extends RecyclerView.Adapter<TimeAxisAdapter.ViewHolder> {
    Context context;
    List<TimeAxisItem> items;

    public TimeAxisAdapter(Context context, List<TimeAxisItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.timeaxis_image,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(items.get(i).getDes());
        Glide.with(context).load(items.get(i).getUrl()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.image);
            textView=(TextView)view.findViewById(R.id.time);
        }
    }
}
