package com.timewasteanalyzer.usage.data


import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.list.ListItemData
import com.timewasteanalyzer.usage.timeline.TimelineItemData
import com.timewasteanalyzer.util.SingletonHolder
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.xml.datatype.DatatypeConstants

class UsageRepository private constructor(context: Context) {

    private var mListDataList: MutableList<ListItemData> = ArrayList()
    private var mListItemDataMap: HashMap<String, ListItemData> = HashMap()

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
            var filterText = when (mCurrentFilterType) {
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
        var startTime: Long = when (mCurrentFilterType) {
            // querying hours of current day
            FilterType.DAY -> now - 1000 * 3600 * LocalDateTime.now().hour
            FilterType.WEEK -> now - 1000 * 3600 * 24 * 7
        }

        fetchListItemUsages(startTime, now)
        determineTimelineFromEvents()
    }

    private fun fetchListItemUsages(start: Long, end: Long) {
        // Query events and initialize repository data
        val queriedUsage = mUsageStatsManager.queryEvents(start, end)
        mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, end)
        initializeListDataBasedOn(queriedUsage)

        // Iterate through usage list
        for (i in 0 until mAllEvents.size - 1) {
            val event = mAllEvents[i]
            val event2 = mAllEvents[i + 1]

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

        updateUsageList(mListItemDataMap.values)
    }

    /**
     * Determines the entries of the timeline based on [mAllEvents].
     */
    private fun determineTimelineFromEvents() {
        // 1: create appInForegroundList containing all ("app in foreground"?) events
//        var tempTimelineEvents = ArrayList<TimelineItemData>()

        mAllEvents.sortedWith(compareBy { it.timeStamp })
        for (i in 0 until mAllEvents.size - 1) {
            val currentEvent = mAllEvents[i]
            val nextEvent = mAllEvents[i + 1]

            if (currentEvent.eventType == 1 && nextEvent.eventType == 2 && currentEvent.className == nextEvent.className) {
                // App is closed so this usage can be added to the timeline slot
                val appUsageTimeMs = nextEvent.timeStamp - currentEvent.timeStamp

                val currentEventDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentEvent.timeStamp), ZoneId.systemDefault())
                if (mTimelineDataList.isEmpty()) {
                    // Create first entry
                    val timeLineEvent = TimelineItemData(mContext, currentEventDate)
                    timeLineEvent.addEntry(appUsageTimeMs, currentEvent.packageName)
                    mTimelineDataList.add(timeLineEvent)

                } else {
                    val lastTimelineItemData = mTimelineDataList[mTimelineDataList.size -1]
                    if (lastTimelineItemData.isInSameBlock(currentEventDate)) {
                        lastTimelineItemData.addEntry(appUsageTimeMs, currentEvent.packageName)

                    } else {
                        // Create break between last and new event
                        val breakStart = lastTimelineItemData.mStartDate.plusSeconds(1)
                        val breakBlock = TimelineItemData(mContext, breakStart)
                        breakBlock.mDuration = Duration.between(breakStart, currentEventDate).toMillis()
                        mTimelineDataList.add(breakBlock)

                        // Create new entry for current event
                        val newTimeLineEvent = TimelineItemData(mContext, currentEventDate)
                        newTimeLineEvent.addEntry(appUsageTimeMs, currentEvent.packageName)
                        mTimelineDataList.add(newTimeLineEvent)
                    }
                }
            }
        }
    }

    private fun resetFormerData() {
        phoneUsageTotal = 0

        mListItemDataMap = HashMap()
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
    private fun initializeListDataBasedOn(usageEvents: UsageEvents) {
        var currentEvent: UsageEvents.Event
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                mAllEvents.add(currentEvent)

                // Save usages into map
                val key = currentEvent.packageName
                if (mListItemDataMap[key] == null)
                    mListItemDataMap[key] = ListItemData(mContext, key)
            }
        }
    }

    private fun updateUsageList(values: Collection<ListItemData>) {
        mListDataList.clear()
        mListDataList.addAll(values.sortedWith(compareBy { -it.msInForeground }))
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