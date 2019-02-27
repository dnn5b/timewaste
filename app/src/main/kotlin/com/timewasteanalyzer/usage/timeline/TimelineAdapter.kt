package com.timewasteanalyzer.usage.timeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewasteanalyzer.R
import com.timewasteanalyzer.dateduration.DurationFormat
import com.timewasteanalyzer.usage.timeline.TimelineItemData.Type
import kotlinx.android.synthetic.main.layout_timeline_item_break.view.*
import kotlinx.android.synthetic.main.layout_timeline_item_usage.view.*
import org.threeten.bp.format.DateTimeFormatter


class TimelineAdapter(private val mTimelineList: List<TimelineItemData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Type.USAGE.value) {
            val usageView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_timeline_item_usage, parent, false)
            UsageViewHolder(usageView)

        } else {
            val breakView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_timeline_item_break, parent, false)
            BreakViewHolder(breakView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val usage = mTimelineList[position]
        if (holder.itemViewType == Type.USAGE.value) {
            (holder as UsageViewHolder).bindUsage(usage)
        } else {
            (holder as BreakViewHolder).bindUsage(usage)
        }
    }

    override fun getItemCount(): Int {
        return mTimelineList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mTimelineList[position].mType.value
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class UsageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUsage(usage: TimelineItemData) {
            var heading = "empty..."
            if (usage.mPackageNames.isNotEmpty() && usage.mPackageNames[usage.mPackageNames.size - 1] != null) {
                heading = usage.mPackageNames[usage.mPackageNames.size - 1]
            }
            itemView.itemHeading.text = usage.mStartDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            itemView.itemDuration.text = DurationFormat(usage.mDuration).getShortText()
            itemView.itemName.text = heading + " | " + usage.mPackageNames.size

            itemView.timelineBarUsage.setBackgroundResource(usage.getColor())
//            itemView.itemIcon.setImageDrawable(usage.mAppIcon)
        }
    }

    /**
     * The ViewHolder for a usage list item.
     */
    class BreakViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUsage(usage: TimelineItemData) {
            itemView.breakDuration.text = DurationFormat(usage.mDuration).getShortText()
            itemView.timelineBarBreak.setBackgroundResource(usage.getColor())
        }
    }
}