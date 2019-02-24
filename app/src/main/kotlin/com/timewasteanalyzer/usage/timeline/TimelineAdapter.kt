package com.timewasteanalyzer.usage.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.timeline.TimelineItemData
import kotlinx.android.synthetic.main.layout_timeline_item.view.*
import java.time.format.DateTimeFormatter


class TimelineAdapter(private val mTimelineList: List<TimelineItemData>) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_timeline_item, parent, false) as RelativeLayout
        return TimelineViewHolder(v)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val usage = mTimelineList[position]
        holder.bindUsage(usage)
    }

    override fun getItemCount(): Int {
        return mTimelineList.size
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class TimelineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUsage(usage: TimelineItemData) {
            var heading = "leer"
            if (usage.mPackageNames.isNotEmpty() && usage.mPackageNames[usage.mPackageNames.size - 1] != null) {
                heading = usage.mPackageNames[usage.mPackageNames.size - 1]
            }
            itemView.itemHeading.text = usage.mStartDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            itemView.itemDuration.text = usage.getUsageInSeconds()
            itemView.itemName.text = heading + " | " + usage.mPackageNames.size

            itemView.timelineBar.setBackgroundResource(usage.getColor())
//            itemView.itemIcon.setImageDrawable(usage.mAppIcon)
        }
    }
}