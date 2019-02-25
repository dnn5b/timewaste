package com.timewasteanalyzer.usage.data

import android.app.usage.UsageEvents
import android.content.Context
import com.timewasteanalyzer.usage.timeline.TimelineItemData
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TimelineData {

    private var mContext: Context
    private var mEvents: MutableList<UsageEvents.Event>

    constructor(context: Context, events: MutableList<UsageEvents.Event>) {
        mContext = context
        mEvents = events
    }

    /**
     * Determines the entries of the timeline based on the passed [mEvents].
     */
    fun get(): MutableList<TimelineItemData> {
        val result = ArrayList<TimelineItemData>()

        mEvents.sortedWith(compareBy { it.timeStamp })
        for (i in 0 until mEvents.size - 1) {
            val currentEvent = mEvents[i]
            val nextEvent = mEvents[i + 1]

            if (currentEvent.eventType == 1 && nextEvent.eventType == 2 && currentEvent.className == nextEvent.className) {
                // App is closed so this usage can be added to the timeline slot
                val appUsageTimeMs = nextEvent.timeStamp - currentEvent.timeStamp

                val currentEventDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentEvent.timeStamp), ZoneId.systemDefault())
                if (result.isEmpty()) {
                    // Create first entry
                    val timeLineEvent = TimelineItemData(mContext, currentEventDate)
                    timeLineEvent.addEntry(appUsageTimeMs, currentEvent.packageName)
                    result.add(timeLineEvent)

                } else {
                    val lastTimelineItemData = result[result.size - 1]
                    if (lastTimelineItemData.isInSameBlock(currentEventDate)) {
                        lastTimelineItemData.addEntry(appUsageTimeMs, currentEvent.packageName)

                    } else {
                        // Create break between last and new event
                        val breakStart = lastTimelineItemData.mStartDate.plusSeconds(1)
                        val breakBlock = TimelineItemData(mContext, breakStart)
                        breakBlock.mDuration = Duration.between(breakStart, currentEventDate).toMillis()
                        result.add(breakBlock)

                        // Create new entry for current event
                        val newTimeLineEvent = TimelineItemData(mContext, currentEventDate)
                        newTimeLineEvent.addEntry(appUsageTimeMs, currentEvent.packageName)
                        result.add(newTimeLineEvent)
                    }
                }
            }
        }
        return result
    }

}
