package com.one.fruitmanpengepul.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.one.fruitmanpengepul.DetailProductActivity
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Product
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimelineAdapter(private var products : MutableList<Product>, private var context: Context) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product, context: Context){
            with(itemView){
                iv_image.load("http://fruitman-app.herokuapp.com/uploads/product/"+product.image)
                tv_name.text = product.name
                tv_price.text = product.price
                setOnClickListener {
                    context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                        putExtra("PRODUCT", product)
                    })
                    Toast.makeText(context, product.name, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position], context)

    fun changelist(c : List<Product>){
        products.clear()
        products.addAll(c)
        notifyDataSetChanged()
    }
}