package com.dusky.world.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dusky.world.Module.activities.ArticleActivity;
import com.dusky.world.Module.entity.ArticleItem;
import com.dusky.world.Module.entity.User;
import com.dusky.world.R;

import java.util.ArrayList;

/**
 * @AUTHOR: dsy
 * @TIME: 2019/7/8
 * @DESCRIPTION:
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<ArticleItem> datas=new ArrayList<>();
    Context context;

    public HotAdapter(Context context){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        for(int i=0;i<3;i++){
            User user=new User("dusky","10423932843","http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg","500");
            if(i==1){
                datas.add(new ArticleItem("Titled大打上路大苏打到拉萨到啦测试长度标题嘎嘎"+i,"http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg",context.getString(R.string.tip),"手机游戏",i+"阅读",i*99+"回复",i*88+"喜欢",user));
            }else{
                datas.add(new ArticleItem("Titled大打上路大苏打到拉萨到啦测试长度标题嘎嘎嘎嘎打扫打扫打扫"+i,"http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg",context.getString(R.string.tip),"手机游戏",i+"阅读",i*99+"回复",i*88+"喜欢",user));
            }

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
        //Glide.with(context).load(R.drawable.qyqx).into(holder.backgroundImg);
        holder.title.setText(datas.get(position).getTitle());
        holder.tag.setText(datas.get(position).getTag());
        holder.like.setText(datas.get(position).getRead_v()+"·"+datas.get(position).getComment_v()+"·"+datas.get(position).getLike_v());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ArticleActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img,backgroundImg;
        TextView title,tag,like;
        ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.medal1);
            backgroundImg = (ImageView)view.findViewById(R.id.backgroundImg);
            title = (TextView)view.findViewById(R.id.title);
            tag=view.findViewById(R.id.tag);
            like=view.findViewById(R.id.like);
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

