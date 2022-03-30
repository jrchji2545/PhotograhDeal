package com.example.photograhdeal.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photograhdeal.R

class GAdapter(private val gallerylist : ArrayList<Gallery> ): RecyclerView.Adapter< GAdapter.GViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_model,parent,false)
        return GViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:GViewHolder, position: Int) {
        val index = gallerylist[position]
        Glide.with(holder.itemView.context).load(index.img).into(holder.img)

    }

    override fun getItemCount(): Int {

        return gallerylist.size

    }
    class  GViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img : ImageView = itemView.findViewById(R.id.img) as ImageView



    }
    fun getGalleryeAdapter():ArrayList<Gallery>{
        return gallerylist
    }



}


