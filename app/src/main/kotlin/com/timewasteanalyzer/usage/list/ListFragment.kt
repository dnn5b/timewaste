package com.timewasteanalyzer.usage.list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewasteanalyzer.R
import com.timewasteanalyzer.refresh.RefreshStatusCallback
import com.timewasteanalyzer.refresh.RefreshableFragment
import com.timewasteanalyzer.usage.data.RefreshUsageListTask
import com.timewasteanalyzer.usage.data.UsageRepository
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : RefreshableFragment(), RefreshStatusCallback {

    /** The repository for accessing the usage data. */
    private lateinit var mRepo: UsageRepository

    /** Adapter to show usages in RecyclerView. */
    private lateinit var mUsageAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    /**
     * Initializes the {@link #mRepo} and {@link recyclerView} after the view of this fragment
     * has been created.
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the repo if this fragment hasn't been used yet
        if (!::mRepo.isInitialized) {
            mRepo = UsageRepository.getInstance(activity!!)
        }

        setupRecyclerView()
    }

    /**
     * Triggers the refresh of this view, after it has been attached to the view.
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Start update of repository
        RefreshUsageListTask(activity, this).execute()
    }

    /**
     * Handles the cleanup of the fragment if it's removed from the view.
     */
    override fun onDetach() {
        super.onDetach()

        // Repo has to be cleared to prevent an "Inconsistency detected; invalid item position"
        // error when switching fast between different filters
        mRepo.clear()
    }

    /**
     * Initializes the {@link #usageRecyclerview}.
     */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        usageRecyclerview.layoutManager = layoutManager

        // Layout size won't change so performance can be improved with fix size
        usageRecyclerview.setHasFixedSize(true)

        // Add adapter containing the current list of usages
        mUsageAdapter = ListAdapter(mRepo.mListUsages)
        usageRecyclerview.adapter = mUsageAdapter
    }

    /**
     * Empty implementation of the callback. Only {@link #refreshFinished} is needed.
     */
    override fun refreshStarted() {}

    /**
     * Updates the list and heading after the data has been updated. Will also call
     * {@link RefreshStatusCallback#refreshFinished} of the parent activity, if the
     * interface is implemented by the activity.
     */
    override fun refreshFinished() {
        // Update heading
        usageHeading.text = mRepo.getTotalTimeHeading

        // Clear pool before update data to prevent leaks when switching fast between fragments
        usageRecyclerview.recycledViewPool.clear()
        mUsageAdapter.notifyDataSetChanged()

        // If the parent Activity implements also the refresh callback it should be notified as well
        if (activity is RefreshStatusCallback) {
            (activity as RefreshStatusCallback).refreshFinished()
        }
    }

    /**
     * Refresh this fragment by executing a {@link RefreshUsageListTask}.
     */
    override fun refresh() {
        RefreshUsageListTask(activity, this).execute()
    }
}
