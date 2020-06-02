package com.one.fruitmanpengepul.adapters.seller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.ui.order_in_progress_activity.OrderInViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.list_item_collector_waiting.view.*

class SellerOrderInAdapter(private var orders: MutableList<Order>, private var context: Context, private var orderInViewModel: OrderInViewModel) : RecyclerView.Adapter<SellerOrderInAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_collector_waiting, parent, false))

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, orderInViewModel)

    fun changelist(i: List<Order>) {
        orders.clear()
        orders.addAll(i)
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, context: Context, orderInViewModel: OrderInViewModel) {
            with(itemView) {
                if (order.status.equals("1") && !order.arrive!!) {
                    waiting_desc.text = "${order.collector.name} menawar dengan harga ${order.offer_price} pada ${order.product.name}"
                    setOnClickListener { Toast.makeText(context, "Ya", Toast.LENGTH_LONG).show() }
                    btn_confirmed.visibility = View.VISIBLE
                    btn_confirmed.setOnClickListener {
                        orderInViewModel.confirmed("Bearer ${FruitmanUtil.getToken(context)}", order.id.toString())
                    }
                    btn_decline.setOnClickListener {
                        val role = "seller_id"
                        orderInViewModel.reject("Bearer ${FruitmanUtil.getToken(context)}", order.id!!, role)
                    }
                }
            }
        }
    }
}