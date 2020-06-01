package com.one.fruitmanpengepul.adapters.collector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.OrderWaiting
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.list_item_collector_waiting.*
import kotlinx.android.synthetic.main.list_item_collector_waiting.view.*

class CollectorWaitingAdapter (private var orders : MutableList<Order>, private var context : Context, private var orderViewModel : OrderViewModel)
    : RecyclerView.Adapter<CollectorWaitingAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order : Order, context: Context, orderViewModel: OrderViewModel){
            with(itemView){
                waiting_desc.text = "Menunggu Konfirmasi dari ${order.seller.name} dengan ${order.product.name}"
                btn_decline.setOnClickListener {
                    val role = "collector_id"
                    orderViewModel.decline("Bearer ${FruitmanUtil.getToken(context)}", order.id!!, role)
                    Toast.makeText(context, "Berhasil Menolak Pesanan", Toast.LENGTH_LONG).show()
                }
                setOnClickListener {
                    Toast.makeText(context,"Ya", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun changelist(i : List<Order>){
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(orders[position], context, orderViewModel)
}