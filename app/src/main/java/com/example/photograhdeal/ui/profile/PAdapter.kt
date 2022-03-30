package com.example.photograhdeal.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photograhdeal.R

class PAdapter(private val profilelist : ArrayList<ProfileData> ): RecyclerView.Adapter< PAdapter.pViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_item,parent,false)
        return pViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:pViewHolder, position: Int) {
        val index = profilelist[position]

    }

    override fun getItemCount(): Int {

        return profilelist.size

    }
    class  pViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){



    }
    fun getProfileAdapter():ArrayList<ProfileData>{
        return profilelist
    }
    fun getArraylist(): ArrayList<ProfileData> {
        return profilelist
    }


}


