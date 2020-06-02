package com.one.fruitmanpengepul.adapters.seller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.list_item_in_progress.view.*

class SellerOrderInProgressAdapter (private var orders : MutableList<Order>,
                                    private var context: Context, private var orderViewModel: OrderViewModel)
    : RecyclerView.Adapter<SellerOrderInProgressAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order, context: Context, orderViewModel: OrderViewModel){
            with(itemView){
                if (order.status == "2" && order.arrive == false){
                    tv_desc.text = "${order.collector.name} sedang menuju tempat anda, untuk melakukan transaksi ${order.product.name}"
                    btn_completed.visibility = View.GONE
                    btn_arrived.visibility = View.GONE
                    btn_decline.visibility = View.GONE
                }else if (order.status == "2" && order.arrive == true){
                    tv_desc.text = "status transaksi dengan ${order.collector.name} dengan ${order.product.name} setelah bertemu di tempat"
                    btn_arrived.visibility = View.GONE
                    btn_completed.visibility = View.VISIBLE
                    btn_decline.visibility = View.VISIBLE
                    btn_completed.setOnClickListener {
                        orderViewModel.completed("Bearer ${FruitmanUtil.getToken(context)}", order.id.toString())
                    }
                    btn_decline.setOnClickListener {
                        val role = "seller_id"
                        orderViewModel.reject("Bearer ${FruitmanUtil.getToken(context)}", order.id!!.toInt(), role)
                    }
                }else{
                    tv_desc.visibility = View.GONE
                    btn_arrived.visibility = View.GONE
                    btn_decline.visibility = View.GONE
                    btn_completed.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_in_progress, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, orderViewModel)

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}