package com.example.businesshub.presentation.companies

import androidx.recyclerview.widget.RecyclerView
import com.example.businesshub.databinding.CompanyListItemBinding

class CompanyListViewHolder(binding: CompanyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val image = binding.image
    val companyName = binding.companyName
    val post = binding.post
}