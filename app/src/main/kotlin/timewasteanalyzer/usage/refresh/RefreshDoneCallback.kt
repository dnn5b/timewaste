package timewasteanalyzer.usage.refresh

import android.content.Context


interface RefreshDoneCallback {

    fun refreshFinished()
    fun getCurrentContext(): Context

}
