package company.wow.gallary

import company.wow.gallary.model.UnsplashModel
import io.reactivex.Observable

class Repository(val unsplashhApi: UnsplashhApi = UnsplashhApi.create()) {
    fun getPhotos(page : Int): Observable<List<UnsplashModel>> {
        return unsplashhApi.photos(Constants.API_KEY, page, Constants.PHOTOS_PER_PAGE)
    }
}