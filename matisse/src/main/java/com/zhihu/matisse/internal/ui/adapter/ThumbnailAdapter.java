package com.zhihu.matisse.internal.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 缩略图Adapter
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {

    private OnThumbnailClickListener mListener;
    private List<Item> mDatas = new ArrayList<>();

    private final Drawable mPlaceholder;
    private int mImageResize;

    public ThumbnailAdapter(Context context, List<Item> datas, OnThumbnailClickListener listener) {
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }

        mListener = listener;

        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{R.attr.item_placeholder});
        mPlaceholder = ta.getDrawable(0);
        ta.recycle();
    }

    public void refreshDatas(List<Item> datas) {
        mDatas.clear();
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public interface OnThumbnailClickListener {
        void onDeleteClick(Item data, int position);

        void onItemClick(Item data, int position);
    }

    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_item, parent, false);
        return new ThumbnailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
        Item data = mDatas.get(position);
        SelectionSpec.getInstance().imageEngine.loadThumbnail(holder.itemView.getContext(), getImageResize(holder.itemView.getContext()),
                mPlaceholder, holder.mIvThumbnail, data.getContentUri());
        holder.mIvDelete.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDeleteClick(data, position);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(data, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected static class ThumbnailViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvThumbnail;
        private ImageView mIvDelete;

        ThumbnailViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mIvDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private int getImageResize(Context context) {
        if (mImageResize == 0) {
            mImageResize = context.getResources().getDimensionPixelSize(R.dimen.album_item_height);
        }
        return mImageResize;
    }
}
