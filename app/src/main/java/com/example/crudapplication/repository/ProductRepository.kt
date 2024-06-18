package com.example.crudapplication.repository

import android.net.Uri
import com.example.crudapplication.model.ProductModel

interface ProductRepository {

    fun uploadImage(imageName: String, imageUrl: Uri, callback: (Boolean, String?) -> Unit)

    fun addProduct(productModel:ProductModel,callback:(Boolean,String) ->Unit)

    fun getAllProduct(callback: (List<ProductModel>?, Boolean, String) -> Unit)

    fun updateProduct(id:String,data: MutableMap<String,Any>?,callback: (Boolean, String) -> Unit)

    fun deleteData(id:String,callback: (Boolean, String) -> Unit)

    fun deleteImage(imageName:String,callback: (Boolean, String) -> Unit)
}