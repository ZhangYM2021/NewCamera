package com.android.newcamera

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.file.Files.createFile

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val FILE_NAME = "photo"
    private val REQUEST_CODE = 42
    private var count = 0
    private var list: ArrayList<File> = ArrayList()
    private val TAG = "newcamera"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var linearLayout = findViewById<LinearLayout>(R.id.container)


        btnTakePicture.setOnClickListener {
            count += 1
            takePhoto()
        }
        btnRemake.setOnClickListener {
            list.removeLast()
            takePhoto()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = getPhotoFile("$FILE_NAME$count")
        list.add(photoFile)
        Log.d(TAG, "The list size is ${list.size}")
        val fileProvider = FileProvider.getUriForFile(this, "com.android.newcamera.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(list[list.size-1].absoluteFile.absolutePath)
            val imageView = ImageView(this)
            imageView.setImageDrawable(takenImage.toDrawable(resources))
            val linearLayout = findViewById<LinearLayout>(R.id.container)
            linearLayout.addView(imageView)
            /*for(i in list){
                val takenImage = BitmapFactory.decodeFile(i.absolutePath)
                val imageView = ImageView(this)
                imageView.setImageDrawable(takenImage.toDrawable(resources))
                val linearLayout = findViewById<LinearLayout>(R.id.container)
                linearLayout.addView(imageView)
            }*/
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
