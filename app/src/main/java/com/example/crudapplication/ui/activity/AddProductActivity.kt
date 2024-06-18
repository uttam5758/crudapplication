package com.example.crudapplication.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityAddProductBinding
import com.example.crudapplication.model.ProductModel
import com.example.crudapplication.repository.ProductRepositoryImpl
import com.example.crudapplication.utils.ImageUtils
import com.example.crudapplication.utils.LoadingUtils
import com.example.crudapplication.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    lateinit var loadigUtils: LoadingUtils

    lateinit var addProductBinding: ActivityAddProductBinding


    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    lateinit var imageUtils: ImageUtils
    lateinit var productViewModel: ProductViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Binding
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)

        setContentView(addProductBinding.root)

        //from gist file week eight lesson 2
        loadigUtils = LoadingUtils(this)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity {url->
            url.let {
                imageUri = it
                Picasso.get().load(url).into(addProductBinding.imageBrowser)
            }
        }
        var repo= ProductRepositoryImpl()
        productViewModel=ProductViewModel(repo)

        addProductBinding.imageBrowser.setOnClickListener {
            imageUtils.launchGallery(this)

        }

        addProductBinding.addButton.setOnClickListener {
            if (imageUri != null){
                uploadImage()
            }else{
                Toast.makeText(applicationContext,"please upload image first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun uploadImage() {
        loadigUtils.showLoading()
        val imageName = UUID.randomUUID().toString()
        imageUri?.let {
            productViewModel.uploadImage(imageName,it) { success, imageUrl ->
                if (success) {
                    addProduct(imageUrl.toString(), imageName.toString())
                }
            }
        }
    }

    fun addProduct(url: String, imageName: String) {
        var name: String = addProductBinding.newname.text.toString()
        var price: Int = addProductBinding.newprice.text.toString().toInt()
        var description: String = addProductBinding.newdescription.text.toString()
        var data = ProductModel("",name,price,description,url,imageName)

        productViewModel.addProduct(data){ success, message ->
            if (success){
                loadigUtils.dismiss()
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
                finish()
            }else{
                loadigUtils.dismiss()
                Toast.makeText(applicationContext,message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}

