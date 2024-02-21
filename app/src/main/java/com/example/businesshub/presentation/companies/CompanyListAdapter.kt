package com.example.businesshub.presentation.companies

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO
import com.example.businesshub.databinding.CompanyListItemBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CompanyListAdapter(

    var items: List<FirebaseCompanyDTO>

) :
    RecyclerView.Adapter<CompanyListViewHolder>() {


    fun submitList(newList: List<FirebaseCompanyDTO>) {
        val diffResult = DiffUtil.calculateDiff(CompanyItemCallback(items, newList), true)
        diffResult.dispatchUpdatesTo(this)
        items = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyListViewHolder {
        return CompanyListViewHolder(
            CompanyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CompanyListViewHolder, position: Int) {
        holder.companyName.text = items[position].name
        holder.post.text = items[position].post
        Glide.with(holder.image).load(
            Firebase.storage
                .reference
                .child("company")
                .child(items[position].id)
                .child("profile")
        ).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                holder.image.setImageDrawable(resource)
                return true
            }
        }).submit()
    }

}