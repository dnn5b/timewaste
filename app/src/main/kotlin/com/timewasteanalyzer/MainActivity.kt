package com.timewasteanalyzer


import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.timewasteanalyzer.permission.PermissionRequester
import com.timewasteanalyzer.refresh.RefreshStatusCallback
import com.timewasteanalyzer.refresh.RefreshableFragment
import com.timewasteanalyzer.settings.SettingsFragment
import com.timewasteanalyzer.usage.data.FilterType
import com.timewasteanalyzer.usage.list.UsageListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RefreshStatusCallback {

    private lateinit var mListFragment: UsageListFragment
    private lateinit var mSettingsFragment: SettingsFragment
    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListFragment = UsageListFragment()
        mSettingsFragment = SettingsFragment()

        setupBottomNavigation()
        setupRefreshLayout()

        if (PermissionRequester(this).checkForPermission()) {
            showFragment(mListFragment)
        }
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            if (mCurrentFragment is RefreshableFragment) {
                // Triggers the refresh of the fragment if the RefreshLayout has been pulled down
                (mCurrentFragment as RefreshableFragment).refresh()
            } else {
                // Stop refresh view if the current fragment is not refreshable
                refreshLayout.isRefreshing = false
            }
        }

        // Configure the refreshing colors
        refreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    /**
     * Sets up the bottom navigation and handles the at/detaching of fragments and their refresh.
     */
    private fun setupBottomNavigation() {
        var fragmentToAdd: Fragment? = null

        navigation.setOnNavigationItemSelectedListener { item ->
            var isHandled = when (item.itemId) {
                R.id.navigation_today -> {
                    mListFragment.setFilterType(FilterType.DAY)
                    fragmentToAdd = mListFragment
                    true
                }
                R.id.navigation_week -> {
                    mListFragment.setFilterType(FilterType.WEEK)
                    fragmentToAdd = mListFragment
                    true
                }
                R.id.navigation_settings -> {
                    fragmentToAdd = mSettingsFragment
                    true
                }
                else -> false
            }

            if (fragmentToAdd is RefreshableFragment) {
                // Refresh should be only started, if the new fragment will notify this Activity after the refresh
                refreshStarted()
            }

            if (fragmentToAdd != mCurrentFragment) {
                showFragment(fragmentToAdd!!)
            }

            isHandled
        }
    }

    /**
     * Shows the passed fragment and sets {@link #mCurrentFragment}.
     */
    private fun showFragment(fragment: Fragment) {
        fragmentManager.inTransaction {
            if (::mCurrentFragment.isInitialized) {
                remove(mCurrentFragment)
            }
            add(R.id.fragmentContainer, fragment)
        }
        mCurrentFragment = fragment
    }

    /**
     * Convenience method to start and commit a FragmentTransaction.
     */
    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    override fun refreshStarted() {
        refreshLayout.isRefreshing = true
    }

    override fun refreshFinished() {
        refreshLayout.isRefreshing = false
    }

}
