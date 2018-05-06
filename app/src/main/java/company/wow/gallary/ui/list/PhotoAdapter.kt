package company.wow.gallary.ui.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import company.wow.gallary.R
import company.wow.gallary.model.UnsplashModel
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotoAdapter(val allUrls: MutableList<UnsplashModel>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onImgClickListener: OnImgClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return allUrls.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myViewHolder = holder as MyViewHolder
        myViewHolder.show(allUrls.get(position).urls.small)
        myViewHolder.photoImg.setOnClickListener({onImgClickListener.onImgClick(allUrls.get(position))})
    }


    fun setOnCardClickListner(onImgClickListener: OnImgClickListener) {
        this.onImgClickListener = onImgClickListener
    }



    interface OnImgClickListener {
        fun onImgClick(get: UnsplashModel)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoImg = view.photo_image

        fun show(url : String) {
            Picasso.get()
                    .load(url)
                    .into(photoImg)
        }
    }

}

