package com.one.fruitmanpengepul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Post
import kotlinx.android.synthetic.main.list_item_post.view.*

class PostAdapter (private var posts : MutableList<Post>, private var context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(posts[position], context)

    fun updateList(ps : List<Post>){
        posts.clear()
        posts.addAll(ps)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(p : Post, context: Context){
            with(itemView){
                post_name.text = p.name
                setOnClickListener {
                    Toast.makeText(context, p.name, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}