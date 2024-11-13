package com.mogun.yotubeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mogun.yotubeapp.databinding.ItemVideoBinding

class VideoAdapter(private val context: Context): ListAdapter<VideoItem, VideoAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItem) {
            binding.titleTextView.text = item.title
            binding.subTitleTextView.text = context.getString(
                R.string.sub_title_video_info,
                item.channelName,
                item.viewCount,
                item.dateText
            )
            binding.videoThumbnailImageView.apply {
                Glide.with(this)
                    .load(item.videoThumb)
                    .into(this)
            }
            binding.channelLogoImageView.apply {
                Glide.with(this)
                    .load(item.channelThumb)
                    .circleCrop()
                    .into(this)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(
                oldItem: VideoItem,
                newItem: VideoItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: VideoItem,
                newItem: VideoItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}