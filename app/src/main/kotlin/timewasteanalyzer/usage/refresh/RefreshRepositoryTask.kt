package timewasteanalyzer.usage.refresh


import android.os.AsyncTask
import com.timewasteanalyzer.usage.control.UsageRepository


class RefreshRepositoryTask(private val mCallback: RefreshDoneCallback) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        UsageRepository.getInstance(mCallback.getCurrentContext()).queryUsageStatisticsForCurrentType()
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        mCallback.refreshFinished()
    }
}
