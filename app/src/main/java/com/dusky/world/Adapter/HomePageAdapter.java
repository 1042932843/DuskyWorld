package com.dusky.world.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.dusky.world.Design.helper.CircleCropBorder;
import com.dusky.world.Module.activities.TimeAxisActivity;
import com.dusky.world.Module.entity.HomePageData;
import com.dusky.world.Module.entity.TooSimple;
import com.dusky.world.R;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by test on 2017/11/22.
 */


public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private HomePageData data;

    private static final int TYPE_FATE = 0xff01;
    private static final int TYPE_HOT = 0xff02;
    private static final int TYPE_TOOSIMPLE = 0xff03;
    private static final int TYPE_DEFAULT = 0xff04;

    public HomePageAdapter(Context context, HomePageData data) {
        this.context = context;
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HOT:
                return new HolderTypeHOT(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_homepage_hot, parent, false));
            case TYPE_FATE:
                return new HolderTypeFATE(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_homepage_fate, parent, false));
            case TYPE_TOOSIMPLE:
                return new HolderTypeTOOSIMPLE(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_homepage_toosimple, parent, false));
            default:
                return new HolderTypeDEFAULT(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_homepage_def, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderTypeHOT){
            bindTypeHOT((HolderTypeHOT) holder);
        }else if(holder instanceof HolderTypeFATE){
            bindTypeFATE((HolderTypeFATE) holder);
        }else if(holder instanceof HolderTypeTOOSIMPLE){
            bindTypeTOOSIMPLE((HolderTypeTOOSIMPLE)holder);
        }else if(holder instanceof HolderTypeDEFAULT){
            if (data.isHotShow()) {
                position=position-1;
            }
            if (data.isFateShow()) {
                position=position-1;
            }
            if (data.isTooSimpleShow()) {
                position=position-1;
            }
            bindTypeDEFAULT((HolderTypeDEFAULT)holder,position);
        }
    }

    public void Refresh(int start,int range){
        if (data.isHotShow()) {
            start=start+1;
        }
        if (data.isFateShow()) {
            start=start+1;
        }
        if (data.isTooSimpleShow()) {
            start=start+1;
        }
        this.notifyItemRangeChanged(start,range);
    }

    @Override
    public int getItemCount() {
        int size=0;
        if(data.getDefaultTypes()!=null){
            size=size+data.getDefaultTypes().size();
        }
        if (data.isHotShow()) {
            size=size+1;
        }
        if (data.isFateShow()) {
            size=size+1;
        }
        if (data.isTooSimpleShow()) {
            size=size+1;
        }

       return size;
    }

    @Override
    public int getItemViewType(int position) {
        if(data.isHotShow()&&data.getHotPosition()==position){
            return TYPE_HOT;
        }
        if(data.isFateShow()&&data.getFatePosition()==position){
            return TYPE_FATE;
        }
        if(data.isTooSimpleShow()&&data.getTooSimplePosition()==position){
            return TYPE_TOOSIMPLE;
        }

        return TYPE_DEFAULT;
    }


    private class HolderTypeDEFAULT extends RecyclerView.ViewHolder {
        ImageView img,user_avatar;
        TextView title,num,user_name;
        private HolderTypeDEFAULT(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
            user_avatar = (ImageView)itemView.findViewById(R.id.user_avatar);
            title = (TextView)itemView.findViewById(R.id.title);
            //num = (TextView)view.findViewById(R.id.num);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
        }
    }
    private void bindTypeDEFAULT(HolderTypeDEFAULT holder,final  int position){
        Glide.with(context).load(data.getDefaultTypes().get(position).getImgUrl()).into(holder.img);
        MultiTransformation multi = new MultiTransformation(new CircleCropBorder(2,context.getResources().getColor(R.color.gray)));
        Glide.with(context).load(data.getDefaultTypes().get(position).getUser().getAvatar()).apply(bitmapTransform(multi)).into(holder.user_avatar);
        holder.title.setText(data.getDefaultTypes().get(position).getTitle());
        holder.user_name.setText(data.getDefaultTypes().get(position).getUser().getNickname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    private class HolderTypeHOT extends RecyclerView.ViewHolder {
        RecyclerView recyclerView_hot;
        private HolderTypeHOT(View itemView) {
            super(itemView);
            recyclerView_hot=(RecyclerView) itemView.findViewById(R.id.recyclerView_hot);
        }
    }
    private void bindTypeHOT(HolderTypeHOT holder){
        HotAdapter hotAdapter=new HotAdapter(context);
        holder.recyclerView_hot.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView_hot.setAdapter(hotAdapter);
        holder.recyclerView_hot.setNestedScrollingEnabled(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, Activity.class));
            }
        });
    }


    private class HolderTypeFATE extends RecyclerView.ViewHolder {
        RecyclerView recyclerView_fate;
        private HolderTypeFATE(View itemView) {
            super(itemView);
            recyclerView_fate = (RecyclerView) itemView.findViewById(R.id.recyclerView_fate);
        }
    }

    private void bindTypeFATE(HolderTypeFATE holder){
        FateAdapter fateAdapter=new FateAdapter(context);

        GridLayoutManager layoutManage = new GridLayoutManager(context, 3){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight());
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }

        };
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TimeAxisActivity.class));
            }
        });
        holder.recyclerView_fate.setLayoutManager(layoutManage);
        holder.recyclerView_fate.setAdapter(fateAdapter);
        holder.recyclerView_fate.setNestedScrollingEnabled(false);

    }

    private class HolderTypeTOOSIMPLE extends RecyclerView.ViewHolder {
        RecyclerView recyclerView_tooSimple;
        private HolderTypeTOOSIMPLE(View itemView) {
            super(itemView);
            recyclerView_tooSimple = (RecyclerView) itemView.findViewById(R.id.recyclerView_tooSimple);
        }
    }

    private void bindTypeTOOSIMPLE(HolderTypeTOOSIMPLE holder){
        TooSimpleAdapter tooSimpleAdapter=new TooSimpleAdapter(context);
        GridLayoutManager layoutManage = new GridLayoutManager(context, 2){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight());
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }

        };
        layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position,tooSimpleAdapter.getList());
            }
        });



        holder.recyclerView_tooSimple.setLayoutManager(layoutManage);
        holder.recyclerView_tooSimple.setAdapter(tooSimpleAdapter);
        holder.recyclerView_tooSimple.setNestedScrollingEnabled(false);
    }

    private int setSpanSize(int position, List<TooSimple> tooSimples) {

        return tooSimples.get(position).getSpanCount();
    }




}
