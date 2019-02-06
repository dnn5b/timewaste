package com.timewasteanalyzer


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.timewasteanalyzer.permission.PermissionRequester
import com.timewasteanalyzer.usage.control.FilterType
import com.timewasteanalyzer.usage.control.UsageRepository
import com.timewasteanalyzer.usage.list.UsageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import timewasteanalyzer.usage.refresh.RefreshDoneCallback
import timewasteanalyzer.usage.refresh.RefreshRepositoryTask


class MainActivity : AppCompatActivity(), RefreshDoneCallback {

    /** The repository for accessing the usage data. */
    private var mRepo: UsageRepository? = null

    /** Adapter to show usages in RecyclerView. */
    private var mUsageAdapter: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRepo = UsageRepository.getInstance(this)

        setupRecyclerView()
        setupRefreshLayout()
        setupBottomNavigation()

        if (PermissionRequester(this).checkForPermission()) {
            RefreshRepositoryTask(this).execute()
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
            var isHandled = when (item.itemId) {
                R.id.navigation_today -> {
                    mRepo!!.setCurrentType(FilterType.DAY)
                    true
                }
                R.id.navigation_week -> {
                    mRepo!!.setCurrentType(FilterType.WEEK)
                    true
                }
                R.id.navigation_all -> {
                    mRepo!!.setCurrentType(FilterType.ALL)
                    true
                }
                else -> false
            }
            // Start refresh animation
            refreshLayout.isRefreshing = false

            // Start update of repository
            RefreshRepositoryTask(this).execute()
            isHandled
        }
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            RefreshRepositoryTask(this).execute()
        }

        // Configure the refreshing colors
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
    }

    override fun refreshFinished() {
        // Stop refresh animation
        refreshLayout.isRefreshing = false

        // Update heading above list
        usageHeading.text = mRepo!!.getTotalTimeHeading

        // Update list with values
        mUsageAdapter!!.notifyDataSetChanged()
    }

    override fun getCurrentContext(): Context {
        return this
    }

}
