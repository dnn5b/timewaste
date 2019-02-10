package timewasteanalyzer.usage.list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewasteanalyzer.R
import com.timewasteanalyzer.usage.control.FilterType
import com.timewasteanalyzer.usage.control.UsageRepository
import com.timewasteanalyzer.usage.list.UsageAdapter
import kotlinx.android.synthetic.main.fragment_appusage_list.*
import timewasteanalyzer.refresh.RefreshStatusCallback
import timewasteanalyzer.refresh.RefreshableFragment

class UsageListFragment : RefreshableFragment(), RefreshStatusCallback {

    /** The repository for accessing the usage data. */
    private lateinit var mRepo: UsageRepository

    /** Adapter to show usages in RecyclerView. */
    private lateinit var mUsageAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_appusage_list, container, false)
    }

    /**
     * Initializes the {@link #mRepo} and {@link recyclerView} after the view of this fragment
     * has been created.
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::mRepo.isInitialized) {
            // Initialize the repo if this fragment hasn't been used yet
            mRepo = UsageRepository.getInstance(activity!!)
            mRepo.setCurrentType(FilterType.DAY)
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
     * Sets the filter type of the usage list and triggers the update of it.
     */
    fun setFilterType(filterType: FilterType) {
        mRepo.setCurrentType(filterType)

        // Start update of repository if filter type has been switched while this fragment
        // is already attached.
        if (activity != null) {
            refresh()
        }
    }

    /**
     * Initializes
     */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        usageRecyclerview.layoutManager = layoutManager

        // Layout size won't change so performance can be improved with fix size
        usageRecyclerview.setHasFixedSize(true)

        // Add adapter containing the current list of usages
        mUsageAdapter = UsageAdapter(mRepo.mUsageList)
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

        // Update list with updated data
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
