package company.wow.gallary

import company.wow.gallary.model.UnsplashModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class Repository {
    fun getPhotos(): Observable<List<UnsplashModel>>? {
        val unsplashhApi = UnsplashhApi.Factory.create();
        return unsplashhApi.search("39de03d8e03ddc8a583ee15227635acc30f176e6912566f49ba122fabd6829d4", 20, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}