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
import com.example.crudapplication.databinding.ActivityUpdateProductBinding
import com.example.crudapplication.model.ProductModel
import com.example.crudapplication.repository.ProductRepositoryImpl
import com.example.crudapplication.utils.ImageUtils
import com.example.crudapplication.utils.LoadingUtils
import com.example.crudapplication.viewmodel.ProductViewModel
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding
    lateinit var loadigUtils: LoadingUtils
    var id = ""
    var imageName = ""
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
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

    lateinit var imageUtils: ImageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)

        loadigUtils = LoadingUtils(this)

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)
        imageUtils = ImageUtils(this)
        imageUtils.registerActivity {
            imageUri = it
            Picasso.get().load(it).into(updateProductBinding.imageName)
        }

        var products: ProductModel? = intent.getParcelableExtra("product")
        id = products?.id.toString()
        imageName = products?.imageName.toString()
        updateProductBinding.updatename.setText(products?.name)
        updateProductBinding.updatedescription.setText(products?.descriptionn)
        updateProductBinding.updateprice.setText(products?.price.toString())

        Picasso.get().load(products?.url).into(updateProductBinding.imageName)

        updateProductBinding.Update.setOnClickListener {
            if(imageUri == null){
                updateProduct(products?.url.toString())
            }else{
            uploadImage()
                 }
        }
        updateProductBinding.imageName.setOnClickListener {
            imageUtils.launchGallery(this@UpdateProductActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage() {
        loadigUtils.showLoading()
        imageUri?.let {
            productViewModel.uploadImage(imageName, it) { success, imageUrl ->
                if (success) {
                    updateProduct(imageUrl.toString())
                }
            }

        }
    }


    fun updateProduct(url: String) {
        loadigUtils.showLoading()
        var name: String = updateProductBinding.updatename.text.toString()
        var price: Int = updateProductBinding.updateprice.text.toString().toInt()
        var description: String = updateProductBinding.updatedescription.text.toString()

        var data = mutableMapOf<String, Any>()
        data["name"] = name
        data["price"] = price
        data["description"] = description
        data["url"] = url

        productViewModel.updateProduct(id,data){
                success, message ->
            if(success){
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                finish()
                loadigUtils.dismiss()
            }else{
                loadigUtils.dismiss()
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()

            }
        }

    }

}