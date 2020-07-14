package dev.dmayr.a07_2_1_ejercicio_coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL


class MainActivity : AppCompatActivity() {

  private val job = Job()
  private val coroutineContextElement = CoroutineScope(Dispatchers.Main + job)
  private val urlPictureOfTheDay: String = "https://apod.nasa.gov/apod/image/1908/M61-HST-ESO-S1024.jpg"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onStart() {
    super.onStart()
    downloadImage()
  }

  private fun downloadImageBlocking(): Bitmap? {
    val bmp: Bitmap
    try {
      val inputStream = URL(urlPictureOfTheDay).openStream()
      bmp = BitmapFactory.decodeStream(inputStream)
      return bmp
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return null
  }

  private fun downloadImage() {
    coroutineContextElement.launch {
      progressBar.visibility = View.VISIBLE
      val bitmap: Bitmap? = withContext(Dispatchers.IO) { downloadImageBlocking() }
      if (bitmap != null) {
        imgOfDay.setImageBitmap(bitmap)
      }
      progressBar.visibility = View.GONE
    }
  }
}