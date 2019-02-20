package com.timewasteanalyzer.usage.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.timewasteanalyzer.R
import com.timewasteanalyzer.util.Utility.isEmpty
import kotlinx.android.synthetic.main.layout_usage_list_item.view.*


class ListAdapter(private val mUsageList: List<ListItemData>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_usage_list_item, parent, false) as RelativeLayout
        return ListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val usage = mUsageList[position]
        holder.bindUsage(usage)
    }

    override fun getItemCount(): Int {
        return mUsageList.size
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUsage(usage: ListItemData) {
            itemView.itemIcon.setImageDrawable(usage.mAppIcon)
            itemView.itemName.text = if (isEmpty(usage.mAppName)) usage.packageName else usage.mAppName
            itemView.itemCountOpen.text = "Opened: " + usage.mLaunchCount
            itemView.itemForegroundTime.text = usage.foregroundTimeString
            itemView.itemPercentage.text = usage.mPercent.toString() + "%"
            itemView.progressbar.progress = usage.mPercent
        }
    }
}