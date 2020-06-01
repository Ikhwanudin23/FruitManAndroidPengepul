package com.one.fruitmanpengepul.adapters.seller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Product
import kotlinx.android.synthetic.main.item_myproduct.view.*

class SellerMyProductAdapter(private var products : MutableList<Product>, private var context: Context)
    : RecyclerView.Adapter<SellerMyProductAdapter.ViewHolder>(){
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product, context: Context){
            with(itemView){
                iv_image.load(""+product.image)
                tv_name.text = product.name
                tv_price.text = product.price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myproduct, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(products[position], context)

    fun changelist(c : List<Product>){
        products.clear()
        products.addAll(c)
        notifyDataSetChanged()
    }
}