package com.example.crudapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapplication.R
import com.example.crudapplication.model.ProductModel
import com.example.crudapplication.ui.activity.UpdateProductActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductAdapter(var context :android.content.Context,var data: ArrayList<ProductModel>):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        var productName: TextView = view.findViewById(R.id.productname)
        var productprice: TextView = view.findViewById(R.id.productPrice)
        var productdescription: TextView = view.findViewById(R.id.productdescription)
        var btnEdit : TextView = view.findViewById(R.id.btnEdit)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar2)
        var image: ImageView =view.findViewById(R.id.imageViewdisplay)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view = LayoutInflater.from(parent.context).
        inflate(R.layout.sample_product,
            parent,
            false
        )

        return ProductViewHolder(view)


    }

    override fun getItemCount(): Int {
        return data.size

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productName.text= data[position].name
        holder.productprice.text= data[position].price.toString()
        holder.productdescription.text= data[position].descriptionn
        var image = data[position].url
        Picasso.get().load(image).into(holder.image, object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility= View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
        holder.btnEdit.setOnClickListener{
            var intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }
    }
    fun getProductID(position: Int):String{
        return data[position].id

    }
    fun getImageName(position: Int):String{
        return data[position].id
    }
    fun updateData(products : List<ProductModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }
}