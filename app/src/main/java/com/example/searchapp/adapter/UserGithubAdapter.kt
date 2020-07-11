package com.example.searchapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.searchapp.Constans.Companion.VIEW_TYPE_ITEM
import com.example.searchapp.Constans.Companion.VIEW_TYPE_LOADING
import com.example.searchapp.R
import com.example.searchapp.model.UserGithub
import com.example.searchapp.ui.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.usergithub_item.view.*

class UserGithubAdapter(private var activity : Activity, private var listUser: MutableList<UserGithub?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addData(data : MutableList<UserGithub?>){
        this.listUser.addAll(data)
        notifyDataSetChanged()
    }

    fun addLoadingView(){
        android.os.Handler().post {
            listUser.add(null)
            notifyItemInserted(listUser.size - 1)
        }
    }

    fun removeLoadingView(){
        if (listUser.size != 0){
            listUser.removeAt(listUser.size - 1)
            notifyItemRemoved(listUser.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
        return if (viewType == VIEW_TYPE_ITEM){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.usergithub_item,parent,false)
            ItemViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item,parent,false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM){
            holder.itemView.usernameQuery.text = listUser[position]?.username
            val link = listUser[position]?.img
            Picasso.get().load(link).fit().centerCrop().into(holder.itemView.imgQuery)

            holder.itemView.layoutQuery.setOnClickListener {
                val intent = Intent(activity,DetailActivity::class.java)
                intent.putExtra("id",listUser[position]?.id)
                intent.putExtra("username",listUser[position]?.username)
                intent.putExtra("link",listUser[position]?.link)
                intent.putExtra("img",listUser[position]?.img)
                activity.startActivity(intent)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (listUser[position]==null){
            VIEW_TYPE_LOADING
        }else{
            VIEW_TYPE_ITEM
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}