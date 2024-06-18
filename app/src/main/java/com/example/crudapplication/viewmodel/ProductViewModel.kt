package com.example.crudapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudapplication.model.ProductModel
import com.example.crudapplication.repository.ProductRepository

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    fun deleteData(id:String,callback: (Boolean, String) -> Unit){
        repository.deleteData(id, callback)
    }

    fun deleteImage(imageName:String,callback: (Boolean, String) -> Unit){
        repository.deleteImage(imageName,callback)
    }


    fun updateProduct(id:String,data:MutableMap<String,Any>?,callback: (Boolean, String) -> Unit){
        repository.updateProduct(id, data,callback)
    }


    fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        repository.addProduct(productModel, callback)
    }

    fun uploadImage(imageName:String, imageurl: Uri, callback: (Boolean, String?) -> Unit) {
        repository.uploadImage(imageName ,imageurl) { success, imageUrl ->
            callback(success, imageUrl)
        }
    }

    private var _productList = MutableLiveData<List<ProductModel>?>()

    var productList = MutableLiveData<List<ProductModel>?>()
        get() = _productList

    var _loadingState = MutableLiveData<Boolean>()

    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun fetchProduct() {
        _loadingState.value = true
        Log.d("logggggg0","i am here")
        repository.getAllProduct { prod, success, message ->
            Log.d("logggggg1",prod?.size.toString())
            if (prod != null) {
                _loadingState.value = false
                _productList.value = prod
                Log.d("loggggg2",prod?.size.toString())
            }
        }
    }

}

