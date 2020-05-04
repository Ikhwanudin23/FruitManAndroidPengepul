package com.one.fruitmanpengepul.adapters.collector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.OrderWaiting
import kotlinx.android.synthetic.main.list_item_collector_waiting.view.*

class CollectorWaitingAdapter (private var orders : MutableList<OrderWaiting>, private var context : Context) : RecyclerView.Adapter<CollectorWaitingAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(o : OrderWaiting, context: Context){
            with(itemView){
                waiting_desc.text = o.desc
                setOnClickListener {
                    Toast.makeText(context,"Ya", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updateList(i : List<OrderWaiting>){
        orders.clear()
        orders.addAll(i)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_collector_waiting, parent, false)
        )
    }

    override fun getItemCount() = orders.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(orders[position], context)
}