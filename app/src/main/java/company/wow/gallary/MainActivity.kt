package company.wow.gallary

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import company.wow.gallary.model.UnsplashModel
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), PhotoAdapter.OnLoadMoreListener {

    var disposable: Disposable? = null
    var allUrls: MutableList<UnsplashModel> = ArrayList()
    lateinit var photoAdapter : PhotoAdapter

    var pageNumber = 1
    var repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var spanCount = 0
        when {
            getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE -> spanCount = 3
            else -> spanCount = 2
        }

        var gridLayoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        //gridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerViewGallery.layoutManager = gridLayoutManager
        photoAdapter = PhotoAdapter(allUrls, this)
        photoAdapter.setOnLoadMoreListner(this);

        val loadMoreSubject = PublishSubject.create<Int>()

        recyclerViewGallery.adapter = photoAdapter
        recyclerViewGallery.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItems = null
                var lastVisibleItemss = gridLayoutManager.findLastVisibleItemPositions(lastVisibleItems)

                lastVisibleItemss.forEach { i ->
                    if (i == allUrls.size - 1 ) {
                        Logger.log("onScrolled to the last item = $i")
                        loadMoreSubject.onNext(i)
                    }
                }

            }
        })


        loadMoreSubject.debounce(100, TimeUnit.MILLISECONDS)
                .flatMap { t ->  repository.getPhotos(++pageNumber)}
                .subscribe(Consumer {
                    allUrls.addAll(it)
                    //val firstInsertedPosition = allUrls.size - it.size - 1
                    //val lastInstertedPosition = allUrls.size - 1
                    //recyclerViewGallery.adapter.notifyItemRangeInserted(firstInsertedPosition, lastInstertedPosition)
                    recyclerViewGallery.adapter.notifyDataSetChanged()
                    Logger.log("new items:")
                    for (unsplashModel in it) {
                        Logger.log(unsplashModel.urls.small)
                    }
                })
    }


    override fun onStart() {
        super.onStart()

        disposable = repository.getPhotos(1)
                ?.subscribe(Consumer {
                    allUrls.addAll(it)
                    recyclerViewGallery.adapter.notifyDataSetChanged()
                    Logger.log("new items:")
                    for (unsplashModel in it) {
                        Logger.log(unsplashModel.urls.small)
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
        photoAdapter.setOnLoadMoreListner(null);
    }

    override fun onLoadMore() {

    }
}
