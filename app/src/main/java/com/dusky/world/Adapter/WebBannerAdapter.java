package com.dusky.world.Adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dusky.world.Base.DuskyApp;
import com.dusky.world.Module.entity.Banner;
import com.dusky.world.R;
import com.nbsix.dsy.bannerview.BannerView;
import com.nbsix.player.music.StandardMusicPlayer;
import com.nbsix.player.video.NormalVideoPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public class WebBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Banner> banners;
    private BannerView.OnBannerItemClickListener onBannerItemClickListener;

    public static final int TYPE_IMG = 0xff01;
    public static final int TYPE_MUSIC = 0xff02;
    public static final int TYPE_VIDEO = 0xff03;

    public WebBannerAdapter(Context context, List<Banner> banners) {
        this.context = context;
        if(banners==null){
            banners=new ArrayList<>();
            banners.add(new Banner("1","http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg",null,null,null));
            banners.add(new Banner("2","http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg",null,"http://m10.music.126.net/20181006203324/fb5622962900de56cd3cfe79fca769a0/ymusic/85f0/4419/b77f/3e45162d3e2b89b72ad5abce9c43a9eb.mp3",null));
            banners.add(new Banner("3","http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg",null,null,null));
            banners.add(new Banner("4","http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg",null,null,null));
            banners.add(new Banner("5","http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg",null,null,null));
        }
        this.banners = banners;
    }

    public void setOnBannerItemClickListener(BannerView.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_IMG:
                return new HolderTypeImg(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image, parent, false));
            case TYPE_VIDEO:
                return new HolderTypePlayer(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_player, parent, false));
            case TYPE_MUSIC:
                return new HolderTypeMusic(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderTypeImg){
            bindTypeImg((HolderTypeImg) holder, position);
        }else if(holder instanceof HolderTypePlayer){
            bindTypePlayer((HolderTypePlayer) holder, position);
        }else if(holder instanceof HolderTypeMusic){
            bindTypeMusic((HolderTypeMusic)holder,position);
        }
    }


    @Override
    public int getItemCount() {
        if (banners != null) {
           return banners.size();
        }
       return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (banners == null || banners.isEmpty())
            return TYPE_IMG;

        if(banners.get(position).getVideoUrl()!=null)
            return TYPE_VIDEO;

        if(banners.get(position).getMusicUrl()!=null)
            return TYPE_MUSIC;

        return TYPE_IMG;
    }


    private class HolderTypeImg extends RecyclerView.ViewHolder {
        ImageView imageView;
        StandardMusicPlayer musicPlayer;
        private HolderTypeImg(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            musicPlayer= (StandardMusicPlayer) itemView.findViewById(R.id.musicplayer);
            musicPlayer.setUp("http://www.ytmp3.cn/down/53894.mp3",false);
        }
    }
    private void bindTypeImg(HolderTypeImg holder, final int position){
        String url = banners.get(position).getImgUrl();
        ImageView img = (ImageView) holder.imageView;
        Glide.with(context).load(R.drawable.banner).apply(DuskyApp.optionsReflected).into(holder.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position,TYPE_IMG);
                }

            }
        });
    }


    private class HolderTypeMusic extends RecyclerView.ViewHolder {
        ImageView imageView;
        private HolderTypeMusic(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private void bindTypeMusic(HolderTypeMusic holder, final int position){
        String url = banners.get(position).getImgUrl();
        ImageView img = (ImageView) holder.imageView;
        Glide.with(context).load(R.drawable.banner).apply(DuskyApp.optionsReflected).into(holder.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position,TYPE_MUSIC);
                }

            }
        });
    }

    private class HolderTypePlayer extends RecyclerView.ViewHolder {
        NormalVideoPlayer player;
        private HolderTypePlayer(View itemView) {
            super(itemView);
            player = (NormalVideoPlayer) itemView.findViewById(R.id.palyer);
        }
    }

    private void bindTypePlayer(HolderTypePlayer holder, final int position){
        String url = banners.get(position).getVideoUrl();
        holder.player.setUp(url,false,"");
    }




}
