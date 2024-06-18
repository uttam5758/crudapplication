package com.example.crudapplication.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ImageUtils (var activity :AppCompatActivity){
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    fun launchGallery(context: Context){
        var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(context, permissions)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(permissions), 1)
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    fun registerActivity(callback:(Uri?)->Unit){
        activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                val resultcode = result.resultCode
                val imageData = result.data
                if (resultcode == AppCompatActivity.RESULT_OK && imageData != null) {
                    val imageUri = imageData.data
                    callback(imageUri)


                }


            })
    }
}