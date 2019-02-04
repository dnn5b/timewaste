package com.timewasteanalyzer.usage.control


import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.timewasteanalyzer.usage.model.AppUsage
import timewasteanalyzer.util.SingletonHolder
import java.util.*

class UsageRepository private constructor(context: Context) {

    private var mAppUsageList: MutableList<AppUsage> = ArrayList()
    private var mUsageStatsManager: UsageStatsManager
    private var mAppUsageMap: HashMap<String, AppUsage> = HashMap()
    private var mAllEvents: MutableList<UsageEvents.Event> = ArrayList()
    private var mContext: Context = context
    private var phoneUsageTotal: Long = 0

    init {
        mContext = context
        mUsageStatsManager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    companion object : SingletonHolder<UsageRepository, Context>(::UsageRepository)

    val totalTypeForFilter: String
        get() {
            val seconds = (phoneUsageTotal / 1000).toInt() % 60
            val minutes = (phoneUsageTotal / (1000 * 60) % 60).toInt()
            val hours = (phoneUsageTotal / (1000 * 60 * 60) % 24).toInt()

            return StringBuilder()
                    .append(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                    .toString()
        }

    val usageList: List<AppUsage>
        get() {
            return mAppUsageList
        }

    /**
     * Queries the usage data for the passed [FilterType].
     *
     * @param filterType the current [FilterType]
     */
    fun queryUsageStatisticsForType(filterType: FilterType) {
        resetFormerData()

        val now = System.currentTimeMillis()
        var startTime: Long = when (filterType) {
            // TODO filter from 0:00 instead of 24h
            FilterType.DAY -> now - 1000 * 3600 * 24 // querying last day

            // TODO test whether last weeks events are queried like this
            FilterType.WEEK -> now - 1000 * 3600 * 24 * 7

            // TODO test whether all events are queried like this
            FilterType.ALL -> 0
        }

        // Query events and initialize repository data
        val queriedUsage = mUsageStatsManager.queryEvents(startTime, now)
        mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, now)
        initializeDataBasedOn(queriedUsage)

        // Iterate through usage list
        for (i in 0 until mAllEvents.size - 1) {
            val event = mAllEvents[i]
            val event2 = mAllEvents[i + 1]

            // Calculate opened counter
            if (event.packageName != event2.packageName && event2.eventType == 1) {
                // if true, E1 (launch event of an app) app launched
                mAppUsageMap[event2.packageName]?.increaseLaunchCount()
            }

            // UsageTime of apps in time range
            if (event.eventType == 1 && event2.eventType == 2 && event.className == event2.className) {
                // Update total phone usage
                val diff = event2.timeStamp - event.timeStamp
                phoneUsageTotal += diff

                // Update current usage
                val currentUsage = mAppUsageMap[event.packageName]
                currentUsage?.addTimeInForeground(diff)
            }
        }

        // Percentages have to updated after total phone usage is calculate based on all events.
        mAppUsageMap.values.forEach { it.updatePercentage(phoneUsageTotal) }

        updateUsageList(mAppUsageMap.values)
    }

    private fun resetFormerData() {
        phoneUsageTotal = 0
        mAppUsageMap = HashMap()
        mAllEvents = ArrayList()
    }

    /**
     * Resets the [.mAppUsageMap] and [.mAllEvents]. Afterwards the data is updated based on the passed
     * [UsageEvents].
     *
     * @param usageEvents the queried [UsageEvents]
     */
    private fun initializeDataBasedOn(usageEvents: UsageEvents) {
        var currentEvent: UsageEvents.Event
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND || currentEvent.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                mAllEvents.add(currentEvent)

                // Save usages into map
                val key = currentEvent.packageName
                if (mAppUsageMap[key] == null)
                    mAppUsageMap[key] = AppUsage(mContext, key)
            }
        }
    }

    private fun updateUsageList(values: Collection<AppUsage>) {
        mAppUsageList.clear()
        mAppUsageList.addAll(values.sortedWith(compareBy { -it.msInForeground }))
    }

}