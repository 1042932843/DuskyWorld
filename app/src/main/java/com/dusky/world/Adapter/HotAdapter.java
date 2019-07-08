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
import com.dusky.world.Module.entity.Hot;
import com.dusky.world.R;

import java.util.ArrayList;

/**
 * @AUTHOR: dsy
 * @TIME: 2019/7/8
 * @DESCRIPTION:
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<Hot> datas=new ArrayList<>();
    Context context;

    public HotAdapter(Context context){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        for(int i=0;i<3;i++){
            datas.add(new Hot("","测试top超长的标题大家啊算了开大巨阿斯利康的骄傲了圣诞节啦"+i,"2.6w"));
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
        View view=mInflater.inflate(R.layout.activity_homepage_hot_item,parent,false);
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
        Glide.with(context).load(R.drawable.icon_medal).into(holder.img);
        holder.title.setText(datas.get(position).getTitle());
        holder.num.setText(datas.get(position).getExpNum());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title,num;
        ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.medal1);
            title = (TextView)view.findViewById(R.id.title1);
            num = (TextView)view.findViewById(R.id.expnum1);
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

