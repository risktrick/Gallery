package company.wow.gallary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.squareup.picasso.Picasso
import company.wow.gallary.model.UnsplashModel
import kotlinx.android.synthetic.main.activity_fullscreen_photo.*

class FullscreenPhotoActivity : AppCompatActivity() {

    companion object {
        val PARAM_MODEL = "PARAM_MODEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_photo)
    }

    override fun onResume() {
        super.onResume()

        var model = intent.extras.get(PARAM_MODEL) as UnsplashModel
        var findViewById = findViewById<ImageView>(R.id.img_photo_fullscreen)


        Picasso.get()
                .load(model.urls.regular)
                .into(findViewById)
    }
}
