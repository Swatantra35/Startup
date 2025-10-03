package com.startupshowcase.app.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.startupshowcase.app.R
import com.startupshowcase.app.databinding.ItemCompanyBinding
import com.startupshowcase.app.domain.model.Company

class CompanyAdapter(
    private val onCompanyClick: (Company) -> Unit,
    private val onFollowClick: (String) -> Unit
) : ListAdapter<Company, CompanyAdapter.CompanyViewHolder>(CompanyDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val binding = ItemCompanyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompanyViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class CompanyViewHolder(
        private val binding: ItemCompanyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(company: Company) {
            binding.apply {
                textViewCompanyName.text = company.name
                textViewTagline.text = company.tagline
                textViewIndustry.text = company.industry.name.replace("_", " ")
                textViewFollowers.text = "${company.followers} followers"
                
                // Load logo
                Glide.with(imageViewLogo.context)
                    .load(company.logo)
                    .placeholder(R.drawable.placeholder_company)
                    .into(imageViewLogo)
                
                // Load cover image
                Glide.with(imageViewCover.context)
                    .load(company.coverImage)
                    .placeholder(R.drawable.placeholder_cover)
                    .into(imageViewCover)
                
                // Verified badge
                imageViewVerified.visibility = if (company.isVerified) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
                
                // Click listeners
                root.setOnClickListener {
                    onCompanyClick(company)
                }
                
                buttonFollow.setOnClickListener {
                    onFollowClick(company.id)
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
