package com.startupshowcase.app.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.startupshowcase.app.databinding.FragmentHomeBinding
import com.startupshowcase.app.domain.model.IndustryCategory
import com.startupshowcase.app.presentation.home.adapter.CompanyAdapter
import com.startupshowcase.app.presentation.home.adapter.FeaturedCompanyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    
    private lateinit var featuredAdapter: FeaturedCompanyAdapter
    private lateinit var companyAdapter: CompanyAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        setupCategoryChips()
        setupListeners()
        observeViewModel()
        
        viewModel.loadFeaturedCompanies()
        viewModel.loadCompanies()
    }
    
    private fun setupRecyclerViews() {
        // Featured companies (horizontal scroll)
        featuredAdapter = FeaturedCompanyAdapter { company ->
            // Navigate to company detail
            val action = HomeFragmentDirections.actionHomeToCompanyDetail(company.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerViewFeatured.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredAdapter
        }
        
        // Regular companies
        companyAdapter = CompanyAdapter(
            onCompanyClick = { company ->
                val action = HomeFragmentDirections.actionHomeToCompanyDetail(company.id)
                findNavController().navigate(action)
            },
            onFollowClick = { companyId ->
                viewModel.toggleFollow(companyId)
            }
        )
        
        binding.recyclerViewCompanies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = companyAdapter
        }
    }
    
    private fun setupCategoryChips() {
        binding.chipGroupCategories.removeAllViews()
        
        // Add "All" chip
        val allChip = Chip(requireContext()).apply {
            text = "All"
            isCheckable = true
            isChecked = true
            setOnClickListener {
                viewModel.filterByCategory(null)
            }
        }
        binding.chipGroupCategories.addView(allChip)
        
        // Add category chips
        IndustryCategory.values().forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.name.replace("_", " ")
                isCheckable = true
                setOnClickListener {
                    viewModel.filterByCategory(category)
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }
    
    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }
        
        binding.fabCreateCompany.setOnClickListener {
            // Navigate to create company screen
            findNavController().navigate(HomeFragmentDirections.actionHomeToCreateCompany())
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.featuredCompanies.collect { companies ->
                        featuredAdapter.submitList(companies)
                    }
                }
                
                launch {
                    viewModel.companies.collect { companies ->
                        companyAdapter.submitList(companies)
                    }
                }
                
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.swipeRefreshLayout.isRefreshing = isLoading
                    }
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
