package com.one.fruitmanpengepul.adapters.collector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import kotlinx.android.synthetic.main.list_item_collector_in_place.view.*

class CollectorInProgressInPlaceAdapter(private var datas : MutableList<String>, private var context: Context) : RecyclerView.Adapter<CollectorInProgressInPlaceAdapter.ViewHolder>(){
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(data : String, context: Context){
            with(itemView){
                in_place_desc.text = data
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_collector_in_place, parent, false))
    }

    fun updateList(x : List<String>){
        datas.clear()
        datas.addAll(x)
        notifyDataSetChanged()
    }

    override fun getItemCount() = datas.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(datas[position], context)
}