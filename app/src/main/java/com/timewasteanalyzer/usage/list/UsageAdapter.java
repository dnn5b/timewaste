package com.timewasteanalyzer.usage.list;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timewasteanalyzer.R;
import com.timewasteanalyzer.usage.model.AppUsage;

import java.util.List;

import static com.timewasteanalyzer.util.Utility.isEmpty;


public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.UsageViewHolder> {

    private final List<AppUsage> mUsageList;

    public UsageAdapter(List<AppUsage> usageList) {
        mUsageList = usageList;
    }

    @Override
    public UsageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                                                          .inflate(R.layout.layout_usage_list_item, parent, false);
        UsageViewHolder vh = new UsageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UsageViewHolder holder, int position) {
        AppUsage usage = mUsageList.get(position);
        holder.iconImage.setImageDrawable(usage.getAppIcon());
        holder.nameText.setText(isEmpty(usage.getAppName()) ? usage.getPackageName() : usage.getAppName());
        holder.launchCountText.setText("Opened: " + usage.getLaunchCount());
        holder.foregroundTimeText.setText(usage.getTimeInForeground().asTimeString());
        holder.percentageText.setText(usage.getPercent() + "%");
        holder.progressBar.setProgress(usage.getPercent());
    }

    @Override
    public int getItemCount() {
        return mUsageList.size();
    }

    public static class UsageViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImage;
        TextView nameText;
        TextView launchCountText;
        TextView foregroundTimeText;
        TextView percentageText;
        ProgressBar progressBar;

        UsageViewHolder(RelativeLayout itemContainer) {
            super(itemContainer);
            iconImage = itemContainer.findViewById(R.id.item_icon);
            nameText = itemContainer.findViewById(R.id.item_name);
            launchCountText = itemContainer.findViewById(R.id.item_count_open);
            foregroundTimeText = itemContainer.findViewById(R.id.item_foreground_time);
            percentageText = itemContainer.findViewById(R.id.item_percentage);
            progressBar = itemContainer.findViewById(R.id.progressbar);
        }
    }
}
