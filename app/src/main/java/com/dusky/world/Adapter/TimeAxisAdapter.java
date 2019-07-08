package com.dusky.world.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        /*if(i%2==0){
            viewHolder.itemView.setBackgroundResource(R.drawable.pop_left);
        }else{
            viewHolder.itemView.setBackgroundResource(R.drawable.pop_right);
        }*/
        viewHolder.des.setText(items.get(i).getDes());
        viewHolder.num.setText(items.get(i).getNum());
        viewHolder.time.setText(items.get(i).getTime());
        Glide.with(context).load(R.drawable.qyqx).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView des,time,num;

        public ViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.image);
            time=(TextView)view.findViewById(R.id.time);
            des=(TextView)view.findViewById(R.id.des);
            num=(TextView)view.findViewById(R.id.num);
        }
    }
}
