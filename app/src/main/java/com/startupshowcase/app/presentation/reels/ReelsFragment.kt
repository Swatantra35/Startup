package com.startupshowcase.app.presentation.reels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.startupshowcase.app.databinding.FragmentReelsBinding
import com.startupshowcase.app.presentation.reels.adapter.ReelsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReelsFragment : Fragment() {
    
    private var _binding: FragmentReelsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReelsViewModel by viewModels()
    
    private lateinit var reelsAdapter: ReelsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReelsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewPager()
        observeViewModel()
        
        viewModel.loadReels()
    }
    
    private fun setupViewPager() {
        reelsAdapter = ReelsAdapter(
            onLikeClick = { reelId -> viewModel.toggleLike(reelId) },
            onShareClick = { reel -> shareReel(reel) },
            onCompanyClick = { companyId -> navigateToCompany(companyId) },
            onCommentClick = { reelId -> showComments(reelId) }
        )
        
        binding.viewPagerReels.apply {
            adapter = reelsAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.onReelViewed(position)
                    
                    // Load more reels when reaching near end
                    if (position >= reelsAdapter.itemCount - 3) {
                        viewModel.loadMoreReels()
                    }
                }
            })
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.reels.collect { reels ->
                        reelsAdapter.submitList(reels)
                    }
                }
            }
        }
    }
    
    private fun shareReel(reel: com.startupshowcase.app.domain.model.Reel) {
        // Implement share functionality
    }
    
    private fun navigateToCompany(companyId: String) {
        // Navigate to company detail
    }
    
    private fun showComments(reelId: String) {
        // Show comments bottom sheet
    }
    
    override fun onPause() {
        super.onPause()
        // Pause video playback
        reelsAdapter.pauseCurrentVideo()
    }
    
    override fun onResume() {
        super.onResume()
        // Resume video playback
        reelsAdapter.resumeCurrentVideo()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        reelsAdapter.releaseAllPlayers()
        _binding = null
    }
}
