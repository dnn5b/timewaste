package com.timewasteanalyzer.settings


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewasteanalyzer.R
import com.timewasteanalyzer.settings.SETTING.PREF_USE_TIMELINE
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var mPrefs: PreferencesRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSettingsRepo()
        initializeTimelineSetting()
    }

    private fun initializeSettingsRepo() {
        mPrefs = PreferencesRepository.getInstance(activity)
    }

    private fun initializeTimelineSetting() {
        val ddf =  mPrefs.getBooleanFromPref(PREF_USE_TIMELINE)
        timelineSwitch.isChecked = mPrefs.getBooleanFromPref(PREF_USE_TIMELINE)
        timelineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPrefs.saveBooleanToPrefs(PREF_USE_TIMELINE, isChecked)
        }
    }


}
