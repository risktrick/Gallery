package company.wow.gallary

import android.util.Log

class Logger {
    companion object {
        fun log(s : String) {
            if (BuildConfig.DEBUG) {
                Log.e("aaa", s);
            }
        }
    }
}