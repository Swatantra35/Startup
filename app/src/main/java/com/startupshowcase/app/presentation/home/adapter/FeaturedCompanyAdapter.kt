package com.startupshowcase.app.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.startupshowcase.app.R
import com.startupshowcase.app.databinding.ItemFeaturedCompanyBinding
import com.startupshowcase.app.domain.model.Company

class FeaturedCompanyAdapter(
    private val onCompanyClick: (Company) -> Unit
) : ListAdapter<Company, FeaturedCompanyAdapter.FeaturedCompanyViewHolder>(CompanyDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCompanyViewHolder {
        val binding = ItemFeaturedCompanyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeaturedCompanyViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: FeaturedCompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class FeaturedCompanyViewHolder(
        private val binding: ItemFeaturedCompanyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(company: Company) {
            binding.apply {
                textViewCompanyName.text = company.name
                textViewIndustry.text = company.industry.name.replace("_", " ")
                
                Glide.with(imageViewCover.context)
                    .load(company.coverImage)
                    .placeholder(R.drawable.placeholder_featured)
                    .into(imageViewCover)
                
                Glide.with(imageViewLogo.context)
                    .load(company.logo)
                    .placeholder(R.drawable.placeholder_company)
                    .circleCrop()
                    .into(imageViewLogo)
                
                root.setOnClickListener {
                    onCompanyClick(company)
                }
            }
        }
    }
    
    private class CompanyDiffCallback : DiffUtil.ItemCallback<Company>() {
        override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem == newItem
        }
    }
}
