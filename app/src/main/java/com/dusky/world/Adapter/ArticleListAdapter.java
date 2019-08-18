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
import com.bumptech.glide.load.MultiTransformation;
import com.dusky.world.Design.helper.CircleCropBorder;
import com.dusky.world.Module.entity.ArticleItem;
import com.dusky.world.R;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @AUTHOR: dsy
 * @TIME: 2019/8/18
 * @DESCRIPTION:
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<ArticleItem> articleItems;
    Context context;

    public ArticleListAdapter(Context context,ArrayList<ArticleItem> articleItems){
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        this.articleItems=articleItems;

    }

    public void Refresh(int start,int range){
        this.notifyItemRangeChanged(start,range);
    }

    public ArrayList<ArticleItem> getList(){
        return articleItems;
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.activity_list_article_item,parent,false);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultiTransformation<android.graphics.Bitmap> multi = new MultiTransformation<>(new CircleCropBorder(2,context.getResources().getColor(R.color.gray)));
        Glide.with(context).load(articleItems.get(position).getUser().getAvatar()).apply(bitmapTransform(multi)).into(holder.user_avatar);
        Glide.with(context).load(articleItems.get(position).getImg()).into(holder.img);
        holder.title.setText(articleItems.get(position).getTitle());
        holder.summary.setText(articleItems.get(position).getSummary());
        holder.tag.setText(articleItems.get(position).getTag()+"·"+articleItems.get(position).getRead_v());
        holder.like.setText(articleItems.get(position).getComment_v()+"·"+articleItems.get(position).getLike_v());
    }

    @Override
    public int getItemCount() {
        return articleItems.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_avatar,img;
        TextView user_name,title,summary,tag,like;
        ViewHolder(View view){
            super(view);
            user_avatar=view.findViewById(R.id.user_avatar);
            img=view.findViewById(R.id.img);
            user_name=view.findViewById(R.id.user_name);
            title=view.findViewById(R.id.title);
            summary=view.findViewById(R.id.summary);
            tag=view.findViewById(R.id.tag);
            like=view.findViewById(R.id.like);
        }
    }
}

