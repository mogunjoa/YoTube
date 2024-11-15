package com.mogun.yotubeapp

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mogun.yotubeapp.databinding.ActivityMainBinding
import com.mogun.yotubeapp.player.PlayerHeader
import com.mogun.yotubeapp.player.PlayerVideoAdapter
import com.mogun.yotubeapp.player.transform

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val videoList: VideoList by lazy {
        readData("videos.json", VideoList::class.java) ?: VideoList(emptyList())
    }

    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter(this) { videoItem ->
            binding.motionLayout.setTransition(R.id.collapse, R.id.expand)
            binding.motionLayout.transitionToEnd()

            val headerModel = PlayerHeader(
                id = "H${videoItem.id}",
                title = videoItem.title,
                channelName = videoItem.channelName,
                viewCount = videoItem.viewCount,
                dateText = videoItem.dateText,
                channelThumb = videoItem.channelThumb,
            )

            val list = listOf(headerModel) + videoList.videos.filter { it.id != videoItem.id }
                .map { it.transform() }
            playerVideoAdapter.submitList(list)
            play(videoItem.videoUrl, videoItem.title)
        }
    }

    private val playerVideoAdapter: PlayerVideoAdapter by lazy {
        PlayerVideoAdapter(this) { playerVideo ->
            play(playerVideo.videoUrl, playerVideo.title)

            val headerModel = PlayerHeader(
                id = "H${playerVideo.id}",
                title = playerVideo.title,
                channelName = playerVideo.channelName,
                viewCount = playerVideo.viewCount,
                dateText = playerVideo.dateText,
                channelThumb = playerVideo.channelThumb,
            )

            val list = listOf(headerModel) + videoList.videos.filter { it.id != playerVideo.id }
                .map { it.transform() }

            playerVideoAdapter.submitList(list) {
                binding.playerRecyclerView.scrollToPosition(0)
            }
        }
    }

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initMotionLayout()
        initPlayerVideoRecyclerView()
        initVideoRecyclerView()
        initControllButton()
        initHideButton()
    }

    private fun initHideButton() {
        binding.hideButton.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.hide)
            player?.pause()
        }
    }

    private fun initPlayerVideoRecyclerView() {
        binding.playerRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playerVideoAdapter
            itemAnimator = null
        }
    }

    private fun initVideoRecyclerView() {
        binding.videoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = videoAdapter
        }

        videoAdapter.submitList(videoList.videos)
    }

    private fun initControllButton() {
        binding.controllButton.setOnClickListener {
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                } else {
                    it.play()
                }
            }
        }
    }

    private fun initMotionLayout() {
        binding.motionLayout.targetView = binding.videoPlayerContainer
        binding.motionLayout.jumpToState(R.id.hide)

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding.playerView.useController = false
            }

            override fun onTransitionCompleted(
                motionLayout: MotionLayout?,
                currentId: Int
            ) {
                binding.playerView.useController = currentId == R.id.expand
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }

    private fun initExoPlayer() {
        player = ExoPlayer.Builder(this).build().also {
            binding.playerView.player = it
            binding.playerView.useController = false

            it.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {

                    if (isPlaying) {
                        binding.controllButton.setImageResource(R.drawable.baseline_pause_24)
                    } else {
                        binding.controllButton.setImageResource(R.drawable.baseline_play_arrow_24)
                    }
                }
            })
        }
    }

    private fun play(videoUrl: String, title: String) {
        player?.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
        player?.prepare()
        player?.play()

        binding.videoTitleTextView.text = title
    }

    override fun onStart() {
        super.onStart()

        if (player == null)
            initExoPlayer()
    }

    override fun onStop() {
        super.onStop()

        player?.pause()
    }

    override fun onResume() {
        super.onResume()

        if (player == null)
            initExoPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.release()
    }
}


