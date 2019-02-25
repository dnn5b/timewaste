package com.timewasteanalyzer.usage.data


import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.list.ListItemData
import com.timewasteanalyzer.usage.timeline.TimelineItemData
import com.timewasteanalyzer.util.SingletonHolder
import java.time.LocalDateTime
import java.util.*

class UsageRepository private constructor(context: Context) {

    private var mListDataList: MutableList<ListItemData> = ArrayList()

    private var mTimelineDataList: MutableList<TimelineItemData> = ArrayList()
    private var mTimelineDataMap: HashMap<LocalDateTime, TimelineItemData> = HashMap()

    private var mUsageStatsManager: UsageStatsManager
    private var mAllEvents: MutableList<UsageEvents.Event> = ArrayList()
    private var mContext: Context = context
    private var phoneUsageTotal: Long = 0
    private var mCurrentFilterType: FilterType

    init {
        mContext = context
        mUsageStatsManager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        mCurrentFilterType = FilterType.DAY
    }

    companion object : SingletonHolder<UsageRepository, Context>(::UsageRepository)

    val mListUsages: List<ListItemData>
        get() {
            return mListDataList
        }

    val mTimelineUsages: List<TimelineItemData>
        get() {
            return mTimelineDataList
        }

    val getTotalTimeHeading: String
        get() {
            val filterText = when (mCurrentFilterType) {
                FilterType.DAY -> R.string.title_today
                FilterType.WEEK -> R.string.title_week
            }

            val seconds = (phoneUsageTotal / 1000).toInt() % 60
            val minutes = (phoneUsageTotal / (1000 * 60) % 60).toInt()
            val hours = (phoneUsageTotal / (1000 * 60 * 60) % 24).toInt()

            return StringBuilder()
                    .append(mContext.getString(filterText))
                    .append(": ")
                    .append(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                    .toString()
        }

    /**
     * Queries the usage data for the passed [FilterType].
     */
    fun queryUsageStatisticsForCurrentType() {
        resetFormerData()

        val now = System.currentTimeMillis()
        val startTime: Long = when (mCurrentFilterType) {
            // querying hours of current day
            FilterType.DAY -> now - 1000 * 3600 * LocalDateTime.now().hour
            FilterType.WEEK -> now - 1000 * 3600 * 24 * 7
        }

        val mListItemDataMap = fetchListItemUsages(startTime, now)
        val listDataResult = ListData(mAllEvents, mListItemDataMap).getResult()
        mListDataList.clear()
        mListDataList.addAll(listDataResult.items)
        phoneUsageTotal = listDataResult.phoneUsage

        mTimelineDataList.addAll(TimelineData(mContext, mAllEvents).get())
    }

    /**
     * Query events and initialize repository data.
     */
    private fun fetchListItemUsages(start: Long, end: Long): HashMap<String, ListItemData> {
        val queriedUsage = mUsageStatsManager.queryEvents(start, end)
        mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, end)
        return initializeListDataBasedOn(queriedUsage)
    }

    private fun resetFormerData() {
        phoneUsageTotal = 0

        mTimelineDataMap = HashMap()

        mTimelineDataList.clear()

        mAllEvents = ArrayList()
    }

    /**
     * Resets the [mListItemDataMap] and [mAllEvents]. Afterwards the data is updated based on the passed
     * [UsageEvents].
     *
     * @param usageEvents the queried [UsageEvents]
     */
    private fun initializeListDataBasedOn(usageEvents: UsageEvents): HashMap<String, ListItemData> {
        val result = HashMap<String, ListItemData>()

        var currentEvent: UsageEvents.Event
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                mAllEvents.add(currentEvent)

                // Save usages into map
                val key = currentEvent.packageName
                if (result[key] == null)
                    result[key] = ListItemData(mContext, key)
            }
        }
        return result
    }

    /**
     * Setter for {@link #mCurrentFilterType}.
     */
    fun setCurrentType(currentFilter: FilterType) {
        mCurrentFilterType = currentFilter
    }

    /**
     * Deletes all entries from {@link #mListDataList} and {@link #mTimelineDataList}.
     */
    fun clear() {
        mListDataList.clear()
        mTimelineDataList.clear()
    }
}