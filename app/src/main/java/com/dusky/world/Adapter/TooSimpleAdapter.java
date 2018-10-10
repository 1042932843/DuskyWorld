package com.dusky.world.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dusky.world.Design.helper.CircleCropBorder;
import com.dusky.world.Module.activities.HomePage;
import com.dusky.world.Module.entity.Fate;
import com.dusky.world.Module.entity.TooSimple;
import com.dusky.world.Module.entity.User;
import com.dusky.world.R;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/9/30
 * @DESCRIPTION:
 */
public class TooSimpleAdapter extends RecyclerView.Adapter<TooSimpleAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<TooSimple> tooSimples=new ArrayList<>();
    Context context;

    public TooSimpleAdapter(Context context){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        for (int i=0;i<5;i++){
            User user=new User("dusky","10423932843","http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg","500");
            int s=1;
            if(i==2){
                s=2;
            }
            tooSimples.add(new TooSimple(user,"http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg","十二月份图集"+i,i+"人看过",s));
        }
    }

    public ArrayList<TooSimple> getList(){
        return tooSimples;
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.activity_homepage_toosimple_item,parent,false);
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
        Glide.with(context).load(tooSimples.get(position).getImgUrl()).into(holder.img);
        MultiTransformation multi = new MultiTransformation(new CircleCropBorder(2,context.getResources().getColor(R.color.gray)));
        Glide.with(context).load(tooSimples.get(position).getUser().getAvatar()).apply(bitmapTransform(multi)).into(holder.user_avatar);
        holder.title.setText(""+tooSimples.get(position).getTitle());
        holder.user_name.setText(""+tooSimples.get(position).getUser().getNickname());
        //holder.num.setText(""+fate.get(position).getExpNum());
    }

    @Override
    public int getItemCount() {
        return tooSimples.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img,user_avatar;
        TextView title,num,user_name;
        ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.img);
            user_avatar = (ImageView)view.findViewById(R.id.user_avatar);
            title = (TextView)view.findViewById(R.id.title);
            //num = (TextView)view.findViewById(R.id.num);
            user_name = (TextView)view.findViewById(R.id.user_name);
        }
    }
}

