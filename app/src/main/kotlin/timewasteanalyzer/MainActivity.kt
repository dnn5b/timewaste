package com.timewasteanalyzer


import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.app.ListFragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.timewasteanalyzer.permission.PermissionRequester
import com.timewasteanalyzer.usage.control.FilterType
import kotlinx.android.synthetic.main.activity_main.*
import timewasteanalyzer.settings.SettingsFragment
import timewasteanalyzer.usage.list.UsageListFragment


class MainActivity : AppCompatActivity() {

    private lateinit var mListFragment: UsageListFragment
    private lateinit var mSettingsFragment: SettingsFragment
    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListFragment = UsageListFragment()
        mSettingsFragment = SettingsFragment()

        setupBottomNavigation()

        if (PermissionRequester(this).checkForPermission()) {
            showFragment(mListFragment)
        }
    }

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

            if (fragmentToAdd != mCurrentFragment) {
                showFragment(fragmentToAdd!!)
            }

            isHandled
        }
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.inTransaction {
            if (::mCurrentFragment.isInitialized) {
                remove(mCurrentFragment)
            }
            add(R.id.fragmentContainer, fragment)
        }
        mCurrentFragment = fragment
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

}
