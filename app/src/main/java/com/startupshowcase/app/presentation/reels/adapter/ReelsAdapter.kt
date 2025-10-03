package com.startupshowcase.app.presentation.reels.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.startupshowcase.app.R
import com.startupshowcase.app.databinding.ItemReelBinding
import com.startupshowcase.app.domain.model.Reel

class ReelsAdapter(
    private val onLikeClick: (String) -> Unit,
    private val onShareClick: (Reel) -> Unit,
    private val onCompanyClick: (String) -> Unit,
    private val onCommentClick: (String) -> Unit
) : ListAdapter<Reel, ReelsAdapter.ReelViewHolder>(ReelDiffCallback()) {
    
    private val players = mutableMapOf<Int, ExoPlayer>()
    private var currentPlayingPosition = -1
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val binding = ItemReelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReelViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
    
    inner class ReelViewHolder(
        private val binding: ItemReelBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(reel: Reel, position: Int) {
            binding.apply {
                // Company info
                textViewCompanyName.text = reel.companyName
                textViewTitle.text = reel.title
                textViewDescription.text = reel.description
                
                Glide.with(imageViewCompanyLogo.context)
                    .load(reel.companyLogo)
                    .placeholder(R.drawable.placeholder_company)
                    .circleCrop()
                    .into(imageViewCompanyLogo)
                
                // Stats
                textViewLikes.text = formatCount(reel.likes)
                textViewViews.text = formatCount(reel.views)
                textViewComments.text = formatCount(reel.comments)
                
                // Like button state
                buttonLike.setImageResource(
                    if (reel.isLikedByCurrentUser) R.drawable.ic_heart_filled
                    else R.drawable.ic_heart_outline
                )
                
                // Setup video player
                setupPlayer(position, reel.videoUrl)
                
                // Click listeners
                buttonLike.setOnClickListener {
                    onLikeClick(reel.id)
                }
                
                buttonShare.setOnClickListener {
                    onShareClick(reel)
                }
                
                buttonComment.setOnClickListener {
                    onCommentClick(reel.id)
                }
                
                imageViewCompanyLogo.setOnClickListener {
                    onCompanyClick(reel.companyId)
                }
                
                textViewCompanyName.setOnClickListener {
                    onCompanyClick(reel.companyId)
                }
                
                // Play/pause on tap
                playerView.setOnClickListener {
                    val player = players[position]
                    player?.let {
                        if (it.isPlaying) {
                            it.pause()
                            imageViewPlayPause.visibility = View.VISIBLE
                        } else {
                            it.play()
                            imageViewPlayPause.visibility = View.GONE
                        }
                    }
                }
            }
        }
        
        private fun setupPlayer(position: Int, videoUrl: String) {
            val context = binding.playerView.context
            
            // Create player if not exists
            if (!players.containsKey(position)) {
                val player = ExoPlayer.Builder(context).build().apply {
                    val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                    setMediaItem(mediaItem)
                    prepare()
                    repeatMode = Player.REPEAT_MODE_ONE
                    volume = 1f
                }
                players[position] = player
                binding.playerView.player = player
                
                player.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Player.STATE_READY -> {
                                binding.progressBar.visibility = View.GONE
                            }
                            Player.STATE_ENDED -> {
                                player.seekTo(0)
                                player.play()
                            }
                            else -> {}
                        }
                    }
                })
            } else {
                binding.playerView.player = players[position]
            }
        }
        
        private fun formatCount(count: Int): String {
            return when {
                count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
                count >= 1000 -> String.format("%.1fK", count / 1000.0)
                else -> count.toString()
            }
        }
    }
    
    fun pauseCurrentVideo() {
        players[currentPlayingPosition]?.pause()
    }
    
    fun resumeCurrentVideo() {
        players[currentPlayingPosition]?.play()
    }
    
    fun playVideoAtPosition(position: Int) {
        // Pause current video
        players[currentPlayingPosition]?.pause()
        
        // Play new video
        players[position]?.play()
        currentPlayingPosition = position
    }
    
    fun releaseAllPlayers() {
        players.values.forEach { it.release() }
        players.clear()
    }
    
    private class ReelDiffCallback : DiffUtil.ItemCallback<Reel>() {
        override fun areItemsTheSame(oldItem: Reel, newItem: Reel): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Reel, newItem: Reel): Boolean {
            return oldItem == newItem
        }
    }
}
