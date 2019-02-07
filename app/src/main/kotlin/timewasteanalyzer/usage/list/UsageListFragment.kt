package timewasteanalyzer.usage.list

import android.app.Fragment
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
import timewasteanalyzer.usage.refresh.RefreshDoneCallback
import timewasteanalyzer.usage.refresh.RefreshRepositoryTask

class UsageListFragment : Fragment(), RefreshDoneCallback {

    /** The repository for accessing the usage data. */
    private lateinit var mRepo: UsageRepository

    /** Adapter to show usages in RecyclerView. */
    private lateinit var mUsageAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appusage_list, container, false)

        mRepo = UsageRepository.getInstance(activity!!)
        mRepo.setCurrentType(FilterType.DAY)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRefreshLayout()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Start refresh animation
//        refreshLayout.isRefreshing = true

        // Start update of repository
        RefreshRepositoryTask(activity,this).execute()
    }

    override fun onDetach() {
        super.onDetach()

        mRepo.clear()
    }

    fun setFilterType(filterType: FilterType) {
        mRepo.setCurrentType(filterType)

        // Start update of repository
        if (activity != null) {
            RefreshRepositoryTask(activity,this).execute()
        }
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            RefreshRepositoryTask(activity, this).execute()
        }

        // Configure the refreshing colors
        refreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        usageRecyclerview.layoutManager = layoutManager

        // Layout size won't change so performance can be improved with fix size
        usageRecyclerview.setHasFixedSize(true)

        // Add adapter containing the current list of usages
        mUsageAdapter = UsageAdapter(mRepo!!.mUsageList)
        usageRecyclerview.adapter = mUsageAdapter
    }

    override fun refreshFinished() {
        // Stop refresh animation
        refreshLayout.isRefreshing = false

        // Update heading above list
        usageHeading.text = mRepo!!.getTotalTimeHeading

        // Viewpool has to be cleared to prevent an "Inconsistency detected; invalid item position" error
//        usageRecyclerview.recycledViewPool.clear()

        // Update list with values
        mUsageAdapter!!.notifyDataSetChanged()
    }
}
