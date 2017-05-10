package com.klimchuk.mapsloader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.data.Region;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alexey on 05.05.17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private final Context mContext;

    private List<Region> mRegions;

    private OnItemClickListener onClickListener;

    private OnItemClickListener onDownloadClick;

    public RecyclerAdapter(List<Region> regions, Context context, OnItemClickListener onClick, OnItemClickListener onDownloadClick) {
        mRegions = regions;
        mContext = context;
        onClickListener = onClick;
        this.onDownloadClick = onDownloadClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regions_item, parent, false);

            return new ViewHolderItem(v);

        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regions_header, parent, false);

            return new ViewHolderHeader(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p) {
        int position = holder.getAdapterPosition();

        if (holder instanceof ViewHolderItem) {

            Region currentRegion = mRegions.get(position);

            ((ViewHolderItem) holder).root.
                    setOnClickListener(v -> {
                        onClickListener.onItemClick(currentRegion);

                        if (currentRegion.getNestedRegions().size() == 0)
                            this.notifyItemChanged(position);
                    });

            // Show world or map icon if region is in the top
            if (currentRegion.isFirstLevel())
                ((ViewHolderItem) holder).ivIcon.
                        setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_world_globe_dark));
            else
                ((ViewHolderItem) holder).ivIcon.
                        setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_map));

            ((ViewHolderItem) holder).ivDownload
                    .setImageDrawable(null);

            if (currentRegion.getNestedRegions().size() == 0)
                setImage((ViewHolderItem) holder, currentRegion);

            ((ViewHolderItem) holder).tvName.
                    setText(currentRegion.getName());
        }
    }

    private void setImage(ViewHolderItem holder, Region currentRegion) {
        int defaultColor = mContext.getResources().getColor(R.color.colorIcons);
        holder.ivDownload.setColorFilter(defaultColor);

        switch (currentRegion.getLoadingState()) {
            case NOT_LOADING:
                holder.ivDownload.setOnClickListener(v ->
                        onDownloadClick.onItemClick(currentRegion));
                holder.ivDownload
                        .setImageDrawable(mContext.getResources()
                                .getDrawable(R.drawable.ic_action_import));
                break;

            case LOADING:
                holder.ivDownload
                        .setImageDrawable(mContext.getResources()
                                .getDrawable(R.drawable.ic_action_remove_dark));

                break;
            case LOADED:
                holder.ivDownload
                        .setImageDrawable(mContext.getResources()
                                .getDrawable(R.drawable.ic_check_black_24dp));

                int color = mContext.getResources().getColor(R.color.colorGreen);
                holder.ivDownload.setColorFilter(color);

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mRegions.size();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface OnItemClickListener {
        void onItemClick(Region region);
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.iv_icon_download)
        ImageView ivDownload;

        @BindView(R.id.line)
        View line;

        View root;

        ViewHolderItem(View v) {
            super(v);

            root = v;
            ButterKnife.bind(this, v);
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {

        ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }
}