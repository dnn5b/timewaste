package com.timewasteanalyzer.usage.list


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.model.AppUsage

import com.timewasteanalyzer.util.Utility.isEmpty


class UsageAdapter(private val mUsageList: List<AppUsage>) : RecyclerView.Adapter<UsageAdapter.UsageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_usage_list_item, parent, false) as RelativeLayout
        val vh = UsageViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        val usage = mUsageList[position]
        holder.iconImage.setImageDrawable(usage.appIcon)
        holder.nameText.text = if (isEmpty(usage.appName)) usage.packageName else usage.appName
        holder.launchCountText.text = "Opened: " + usage.launchCount
        holder.foregroundTimeText.text = usage.foregroundTimeString
        holder.percentageText.text = usage.percent.toString() + "%"
        holder.progressBar.progress = usage.percent
    }

    override fun getItemCount(): Int {
        return mUsageList.size
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class UsageViewHolder internal constructor(itemContainer: RelativeLayout) : RecyclerView.ViewHolder(itemContainer) {
        internal var iconImage: ImageView
        internal var nameText: TextView
        internal var launchCountText: TextView
        internal var foregroundTimeText: TextView
        internal var percentageText: TextView
        internal var progressBar: ProgressBar

        init {
            iconImage = itemContainer.findViewById(R.id.item_icon)
            nameText = itemContainer.findViewById(R.id.item_name)
            launchCountText = itemContainer.findViewById(R.id.item_count_open)
            foregroundTimeText = itemContainer.findViewById(R.id.item_foreground_time)
            percentageText = itemContainer.findViewById(R.id.item_percentage)
            progressBar = itemContainer.findViewById(R.id.progressbar)
        }
    }
}
