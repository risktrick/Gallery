package company.wow.gallary.utils

import android.util.Log
import company.wow.gallary.BuildConfig

class Logger {
    companion object {
        fun log(s : String) {
            if (BuildConfig.DEBUG) {
                Log.e("aaa", s);
            }
        }
    }
}