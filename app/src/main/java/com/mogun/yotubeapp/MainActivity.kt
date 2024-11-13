package com.mogun.yotubeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mogun.yotubeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.videoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = videoAdapter
        }

        val videoList = readData("videos.json", VideoList::class.java) ?: VideoList(emptyList())
        videoAdapter.submitList(videoList.videos)
    }
}
