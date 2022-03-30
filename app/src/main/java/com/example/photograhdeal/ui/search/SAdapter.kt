package com.example.photograhdeal.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photograhdeal.R
import com.example.photograhdeal.ui.profile.ProfileData

class SAdapter(private val listSearch: ArrayList<ProfileData>):
    RecyclerView.Adapter<SAdapter.SViewHolder>() {
    private lateinit var search_listener: onClickListener

    interface onClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnSearchClickListener(listener: onClickListener) {
        search_listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.search_model, parent, false)
        return SViewHolder(itemView,search_listener)
    }

    override fun onBindViewHolder(holder: SViewHolder, position: Int) {
        val index = listSearch[position]
        Glide.with(holder.itemView.context).load(index.profile).into(holder.profileimg)
        holder.name.text = index.name

    }

    override fun getItemCount(): Int {
        return listSearch.size
    }

    class SViewHolder(itemView: View, listener: onClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val profileimg: ImageView = itemView.findViewById(R.id.profile_search)
        val name: TextView = itemView.findViewById(R.id.search_name)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    fun getArraylist(): ArrayList<ProfileData> {
        return listSearch
    }
}