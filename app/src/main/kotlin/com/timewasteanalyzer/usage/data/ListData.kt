package com.timewasteanalyzer.usage.data

import android.app.usage.UsageEvents
import com.timewasteanalyzer.usage.list.ListItemData
import java.util.*

class ListData(events: MutableList<UsageEvents.Event>, listItemDataMap: HashMap<String, ListItemData>) {

    private var mListItemDataMap: HashMap<String, ListItemData> = listItemDataMap
    private var phoneUsageTotal = 0L
    private var mEvents: MutableList<UsageEvents.Event> = events

    /**
     * Determines the entries of the timeline based on the passed [mEvents].
     */
    fun getResult(): ListDataResult {
        for (i in 0 until mEvents.size - 1) {
            val event = mEvents[i]
            val event2 = mEvents[i + 1]

            // Calculate opened counter
            if (event.packageName != event2.packageName && event2.eventType == 1) {
                // Another app (event2) has been opened
                mListItemDataMap[event2.packageName]?.increaseLaunchCount()
            }

            // UsageTime of apps
            if (event.eventType == 1 && event2.eventType == 2 && event.className == event2.className) {
                // Update total phone usage
                val diff = event2.timeStamp - event.timeStamp
                phoneUsageTotal += diff

                // Update current usage
                val currentUsage = mListItemDataMap[event.packageName]
                currentUsage?.addTimeInForeground(diff)
            }
        }

        // Percentages have to updated after total phone usage is calculate based on all events.
        mListItemDataMap.values.forEach { it.updatePercentage(phoneUsageTotal) }

        // Sort items and return
        val sortedItems = mListItemDataMap.values.sortedWith(compareBy { -it.msInForeground })
        return ListDataResult(sortedItems, phoneUsageTotal)
    }

    class ListDataResult(val items: Collection<ListItemData>, val phoneUsage: Long)

}
