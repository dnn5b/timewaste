package com.timewasteanalyzer.usage.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.model.AppUsage
import kotlinx.android.synthetic.main.layout_usage_list_item.view.*


class TimelineAdapter(private val mUsageList: List<AppUsage>) : RecyclerView.Adapter<TimelineAdapter.UsageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_timeline_item, parent, false) as RelativeLayout
        return UsageViewHolder(v)
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        val usage = mUsageList[position]
        holder.bindUsage(usage)
    }

    override fun getItemCount(): Int {
        return mUsageList.size
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class UsageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUsage(usage: AppUsage) {
            itemView.itemIcon.setImageDrawable(usage.appIcon)
        }
    }
}