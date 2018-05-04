package company.wow.gallary

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val unsplashhApi = UnsplashhApi.Factory.create();
        unsplashhApi.search("39de03d8e03ddc8a583ee15227635acc30f176e6912566f49ba122fabd6829d4", 1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { t ->  Log.e("aaa", "" + t.toString())})
    }
}
