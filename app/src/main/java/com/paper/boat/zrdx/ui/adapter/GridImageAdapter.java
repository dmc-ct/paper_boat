package com.paper.boat.zrdx.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.util.base.MyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择适配器
 */
public class GridImageAdapter extends
        RecyclerView.Adapter <GridImageAdapter.ViewHolder> {
    private static final int TYPE_CAMERA = 1;
    private static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List <LocalMedia> list = new ArrayList <>();
    private int selectMax = 9;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from( context );
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List <LocalMedia> list) {
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        LinearLayout ll_del;
        TextView tv_duration;

        ViewHolder(View view) {
            super( view );
            mImg = view.findViewById( R.id.fiv );
            ll_del = view.findViewById( R.id.ll_del );
            tv_duration = view.findViewById( R.id.tv_duration );

            RecyclerView recyclerView = (RecyclerView)view;
            // 执行列表动画
            recyclerView.setLayoutAnimation( AnimationUtils.loadLayoutAnimation( context, R.anim.layout_animation_from_right));
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem( position )) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate( R.layout.gv_filter_image,
                                       viewGroup, false );
        final ViewHolder viewHolder = new ViewHolder( view );
        MyToast.printLog( viewHolder + "" );
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {


        //少于8张，显示继续添加的图标
        if (getItemViewType( position ) == TYPE_CAMERA) {
//            viewHolder.mImg.setImageResource(R.drawable.addimg_1x);
            viewHolder.mImg.setImageResource( R.mipmap.img );
            viewHolder.mImg.setOnClickListener( v -> {
                MyToast.printLog( list.size() + "==" );
                mOnAddPicClickListener.onAddPicClick();
            } );
            viewHolder.ll_del.setVisibility( View.INVISIBLE );
        } else {
            viewHolder.ll_del.setVisibility( View.VISIBLE );
            //删除
            viewHolder.ll_del.setOnClickListener( view -> {
                int index = viewHolder.getAdapterPosition();
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION) {
                    list.remove( index );
                    notifyItemRemoved( index ); //通知项目删除
                    notifyItemRangeChanged( index, list.size() ); //通知项目范围更改
                }
            } );
            LocalMedia media = list.get( position );
            int mimeType = media.getMimeType();
            String path = "";

            //判断是否剪裁压缩等
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            // 图片
            if (media.isCompressed()) {
                Log.i( "compress image result:", new File( media.getCompressPath() ).length() / 1024 + "k" );
                Log.i( "压缩地址::", media.getCompressPath() );
            }

            Log.i( "原图地址::", media.getPath() );
            //得到文件类型
            int pictureType = PictureMimeType.isPictureType( media.getPictureType() );
            if (media.isCut()) {
                Log.i( "裁剪地址::", media.getCutPath() );
            }
            long duration = media.getDuration(); //得到视频文件时长
            //进度显示
            viewHolder.tv_duration.setVisibility( pictureType == PictureConfig.TYPE_VIDEO
                                                          ? View.VISIBLE : View.GONE );
            /*图片类型*/
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.tv_duration.setVisibility( View.VISIBLE );
                Drawable drawable = ContextCompat.getDrawable( context, R.drawable.picture_audio );
                StringUtils.modifyTextViewDrawable( viewHolder.tv_duration, drawable, 0 );
            } else {
                Drawable drawable = ContextCompat.getDrawable( context, R.drawable.video_icon );
                StringUtils.modifyTextViewDrawable( viewHolder.tv_duration, drawable, 0 );
            }
            viewHolder.tv_duration.setText( DateUtils.timeParse( duration ) );
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource( R.drawable.audio_placeholder );
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder( R.color.white )
                        .diskCacheStrategy( DiskCacheStrategy.ALL );
                Glide.with( viewHolder.itemView.getContext() )
                        .load( path )  //图片路径
                        .apply( options )
                        .into( viewHolder.mImg );
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener( v -> {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick( adapterPosition, v );
                } );
            }
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
