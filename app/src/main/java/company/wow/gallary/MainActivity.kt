package company.wow.gallary

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import company.wow.gallary.model.UnsplashModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

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
        recyclerViewGallery.layoutManager = gridLayoutManager
        photoAdapter = PhotoAdapter(allUrls, this)
        photoAdapter.setOnCardClickListner(object : PhotoAdapter.OnImgClickListener{
            override fun onImgClick(model: UnsplashModel) {
                var intent = Intent(applicationContext, FullscreenPhotoActivity::class.java)
                intent.putExtra(FullscreenPhotoActivity.PARAM_MODEL, model as Serializable)
                startActivity(intent)
            }
        })
        recyclerViewGallery.adapter = photoAdapter


        val loadMoreSubject = PublishSubject.create<Int>()
        recyclerViewGallery.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemss = gridLayoutManager.findLastVisibleItemPositions(null)

                lastVisibleItemss.forEach {
                    when (it) {
                        allUrls.size - 1 -> loadMoreSubject.onNext(it)
                    }
                }

            }
        })


        loadMoreSubject.debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({showLoadProgress(true)})
                .observeOn(Schedulers.io())
                .flatMap { t -> repository.getPhotos(++pageNumber)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    allUrls.addAll(it)

                    val firstInsertedPosition = allUrls.size - it.size
                    val lastInstertedPosition = allUrls.size - 1
                    recyclerViewGallery.adapter.notifyItemRangeInserted(firstInsertedPosition, lastInstertedPosition)

                    Logger.log("new items:")
                    for (unsplashModel in it) {
                        Logger.log(unsplashModel.urls.small)
                    }

                    showLoadProgress(false)
                }, { t -> Logger.log(t.toString()) })
    }


    override fun onStart() {
        super.onStart()

        disposable = repository.getPhotos(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
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
    }

    fun showLoadProgress(visible: Boolean) {
        when {
            visible -> loadProgressBar.visibility= View.VISIBLE
            else -> loadProgressBar.visibility= View.GONE
        }
    }
}
