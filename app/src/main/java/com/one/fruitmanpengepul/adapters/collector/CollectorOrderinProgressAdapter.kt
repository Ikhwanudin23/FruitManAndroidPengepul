package com.one.fruitmanpengepul.adapters.collector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.ui.buyer_in_progress_activity.BuyerInProgressViewModel
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.list_item_in_progress.view.*

class CollectorOrderinProgressAdapter (private var orders : MutableList<Order>, private var context: Context, private var orderViewModel: BuyerInProgressViewModel)
    : RecyclerView.Adapter<CollectorOrderinProgressAdapter.ViewHolder>(){

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

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order, context: Context, orderViewModel: BuyerInProgressViewModel){
            with(itemView){
                if (order.status.equals("2") && !order.arrive!!){
                    tv_desc.text = "${order.seller.name} menunggu di tempat"
                    btn_arrived.setOnClickListener {
                        orderViewModel.arrived("Bearer ${FruitmanUtil.getToken(context)}", order.id.toString())
                    }
                }else if(order.status.equals("2") && order.arrive!!){
                    btn_arrived.visibility = View.GONE
                    btn_completed.visibility = View.VISIBLE
                    btn_decline.visibility = View.VISIBLE
                    btn_completed.setOnClickListener {
                        orderViewModel.completed("Bearer ${FruitmanUtil.getToken(context)}", order.id.toString())
                    }
                }

                btn_completed.setOnClickListener {
                    Toast.makeText(context, "${order.id} siap compeleted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}