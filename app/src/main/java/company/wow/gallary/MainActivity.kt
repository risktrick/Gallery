package company.wow.gallary

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import company.wow.gallary.model.UnsplashModel
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    var allUrls: MutableList<UnsplashModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var COLUMN_COUNT = 0
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            COLUMN_COUNT = 3
        } else {
            COLUMN_COUNT = 2
        }

        var gridLayoutManager = StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerViewGallery.layoutManager = gridLayoutManager
        recyclerViewGallery.adapter = PhotoAdapter(allUrls, this)
    }


    override fun onStart() {
        super.onStart()

        disposable = Repository().getPhotos()
                ?.subscribe(Consumer {
                    allUrls.addAll(it)
                    recyclerViewGallery.adapter.notifyDataSetChanged()
                    for (unsplashModel in it) {
                        Log.e("aaa", "" + unsplashModel.urls.small)
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }
}
