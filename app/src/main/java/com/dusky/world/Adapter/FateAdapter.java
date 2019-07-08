package com.dusky.world.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dusky.world.Module.entity.Fate;
import com.dusky.world.R;

import java.util.ArrayList;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/9/30
 * @DESCRIPTION:
 */
public class FateAdapter extends RecyclerView.Adapter<FateAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<Fate> fate=new ArrayList<>();
    Context context;

    public FateAdapter(Context context){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        for (int i=0;i<3;i++){
            fate.add(new Fate("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","电锯惊魂"+i,i+"票"));
        }
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.activity_homepage_fate_item,parent,false);
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(fate.get(position).getImgUrl()).into(holder.img);
        holder.title.setText(""+fate.get(position).getTitle());
        holder.num.setText(""+fate.get(position).getExpNum());
    }

    @Override
    public int getItemCount() {
        return fate.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title,num;
        ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.img);
            title = (TextView)view.findViewById(R.id.title);
            num = (TextView)view.findViewById(R.id.num);
        }


    }

    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}

