package com.example.businesshub.presentation.companies

import androidx.recyclerview.widget.DiffUtil
import com.example.businesshub.data.data_source.DTO.FirebaseCompanyDTO

class CompanyItemCallback(
    private val oldList: List<FirebaseCompanyDTO>,
    private val newList: List<FirebaseCompanyDTO>,
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id==newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

}