package com.timewasteanalyzer.usage.list


import android.content.Context
import android.os.AsyncTask
import com.timewasteanalyzer.usage.control.UsageRepository
import com.timewasteanalyzer.refresh.RefreshStatusCallback


class RefreshUsageListTask(private val mContext: Context, private val mCallback: RefreshStatusCallback) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        UsageRepository.getInstance(mContext).queryUsageStatisticsForCurrentType()
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        mCallback.refreshFinished()
    }

}
