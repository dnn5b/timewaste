package com.timewasteanalyzer


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.timewasteanalyzer.permission.PermissionRequester
import com.timewasteanalyzer.usage.control.FilterType
import com.timewasteanalyzer.usage.control.UsageRepository
import com.timewasteanalyzer.usage.list.UsageAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    /** The repository for accessing the usage data. */
    private var mRepo: UsageRepository? = null

    /** Adapter to show usages in RecyclerView. */
    private var mUsageAdapter: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRepo = UsageRepository.getInstance(this)

        setupRecyclerView()
        setupBottomNavigation()

        if (PermissionRequester(this).checkForPermission()) {
            // Today's usage is default filtering on startup
            updateView(FilterType.DAY)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        usageRecyclerview.layoutManager = layoutManager

        // Layout size won't change so performance can be improved with fix size
        usageRecyclerview.setHasFixedSize(true)

        // Add adapter containing the current list of usages
        mUsageAdapter = UsageAdapter(mRepo!!.usageList)
        usageRecyclerview.adapter = mUsageAdapter
    }

    private fun setupBottomNavigation() {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_today -> {
                    updateView(FilterType.DAY)
                    true
                }
                R.id.navigation_week -> {
                    updateView(FilterType.WEEK)
                    true
                }
                R.id.navigation_all -> {
                    updateView(FilterType.ALL)
                    true
                }
                else -> false
            }
        }
    }

    private fun updateView(filterType: FilterType) {
        var headingText = ""
        when (filterType) {
            FilterType.DAY -> {
                headingText = getString(R.string.title_today)
                mRepo!!.queryUsageStatisticsForType(FilterType.DAY)
            }

            FilterType.WEEK -> {
                headingText = getString(R.string.title_week)
                mRepo!!.queryUsageStatisticsForType(FilterType.WEEK)
            }

            FilterType.ALL -> {
                headingText = getString(R.string.title_all)
                mRepo!!.queryUsageStatisticsForType(FilterType.ALL)
            }
        }
        usageHeading.text = headingText + ": " + mRepo!!.totalTypeForFilter
        mUsageAdapter!!.notifyDataSetChanged()
    }

}
