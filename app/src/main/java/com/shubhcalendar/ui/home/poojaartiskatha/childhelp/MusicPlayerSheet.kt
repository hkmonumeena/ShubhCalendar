package com.shubhcalendar.ui.home.poojaartiskatha.childhelp

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shubhcalendar.R
import com.shubhcalendar.activities.VideoPlayerConfig
import com.shubhcalendar.databinding.MusicPlayerSheetBinding
import com.shubhcalendar.databinding.PlayingListSheetBinding
import com.shubhcalendar.ui.HomeNewActivity
import com.shubhcalendar.ui.profile.ProfileFragment
import com.trendyol.medusalib.navigator.transitionanimation.TransitionAnimationType
import kotlinx.coroutines.Job

class MusicPlayerSheet : BottomSheetDialogFragment(), View.OnClickListener, PlaybackPreparer,
    PlayerControlView.VisibilityListener,
    Player.EventListener {
    lateinit var bindingParent: MusicPlayerSheetBinding
    lateinit var binding: PlayingListSheetBinding
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetLayout: RelativeLayout? = null
    val job = Job()
    private var player: SimpleExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
    private var startAutoPlay = false
    private var startWindow = 0
    private var startPosition: Long = 0

    private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
    private val KEY_WINDOW = "window"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"
    private val TAG = "ExoPlayerActivity"

    private val KEY_VIDEO_URI = "video_uri"
    var videoUri: String? = null

    //SimpleExoPlayer player;
    var mHandler: Handler? = null
    var mRunnable: Runnable? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            BottomSheetBehavior.from(bottomSheet).peekHeight =
                Resources.getSystem().displayMetrics.heightPixels
            behavior.isDraggable = false


        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingParent = MusicPlayerSheetBinding.inflate(layoutInflater)

        return bindingParent.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBottomSheetLayout = view.findViewById(R.id.relativeLayoutSheet)
        binding = PlayingListSheetBinding.bind(mBottomSheetLayout!!)
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout!!)
        bindingParent.rlDismiss.setOnClickListener(this)
        bindingParent.relativeLayoutMenu.setOnClickListener(this)
        bindingParent.cardViewProfile.setOnClickListener(this)
        binding.playerView.setControllerVisibilityListener(this)
        binding.playerView.setErrorMessageProvider(PlayerErrorMessageProvider())
        binding.playerView.requestFocus()
        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Toast.makeText(
            requireContext(),
            "${requireArguments().get("musicLink")}",
            Toast.LENGTH_SHORT
        ).show()
        videoUri = requireArguments().getString("musicLink")
        binding.songName.text = "Now Playing :".plus(requireArguments().getString("title"))
        setUp()

        if (savedInstanceState != null) {
            trackSelectorParameters =
                savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay =
                savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startWindow =
                savedInstanceState.getInt(KEY_WINDOW)
            startPosition =
                savedInstanceState.getLong(KEY_POSITION)
        } else {
            trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()
            clearStartPosition()
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            bindingParent.rlDismiss -> {
                dismiss()
            }
            bindingParent.relativeLayoutMenu -> {
                dismiss()
                (activity as HomeNewActivity).binding.drawer.openDrawer(GravityCompat.END)
            }
            bindingParent.cardViewProfile -> {
               dismiss()
                (activity as HomeNewActivity).multipleStackNavigator?.start(ProfileFragment(),TransitionAnimationType.RIGHT_TO_LEFT)
            }
        }
    }


    private fun setUp() {
        initializePlayer()
        if (videoUri == null) {
            return
        }
        buildMediaSource(Uri.parse(videoUri))
    }

    private fun initializePlayer() {
        if (player == null) {
            val intent = requireActivity().intent
            // 1. Create a default TrackSelector
            val loadControl: LoadControl = DefaultLoadControl(
                DefaultAllocator(true, 16),
                2 * VideoPlayerConfig.MIN_BUFFER_DURATION,
                2 * VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true
            )
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory()
            trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            player = ExoPlayerFactory.newSimpleInstance(
                requireActivity(),
                DefaultRenderersFactory(requireActivity()),
                trackSelector,
                loadControl
            )
            player?.playWhenReady = startAutoPlay
            player?.addAnalyticsListener(EventLogger(trackSelector))
            binding.playerView.player = player
            binding.playerView.setPlaybackPreparer(this)
        }
    }

    private fun buildMediaSource(mUri: Uri) {
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            requireActivity(),
            Util.getUserAgent(requireActivity(), "Fdkbfj"), bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri)
        // Prepare the player with the source.
        val haveStartPosition = startWindow != C.INDEX_UNSET
        if (haveStartPosition) {
            player!!.seekTo(startWindow, startPosition)
        }
        player!!.prepare(videoSource)
        player!!.playWhenReady = true
        player!!.addListener(this)
    }

    private fun releasePlayer() {
        if (player != null) {
            updateTrackSelectorParameters()
            updateStartPosition()
            player!!.release()
            player = null
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.playbackState
        }
    }

    private fun resumePlayer() {
        if (player != null) {
            player!!.playWhenReady = true
            player!!.playbackState
        }
    }


    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
            }
            Player.STATE_ENDED -> {
            }
            Player.STATE_IDLE -> {
            }
            Player.STATE_READY -> {
            }
            else -> {
            }
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {}

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

    override fun onPlayerError(error: ExoPlaybackException?) {}

    override fun onPositionDiscontinuity(reason: Int) {}

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}

    override fun onSeekProcessed() {}

    override fun preparePlayback() {
        initializePlayer()
    }

    override fun onVisibilityChange(visibility: Int) {
        Log.d("visiblity", "check = $visibility")
        // debugRootView.setVisibility(visibility)
    }

    private class PlayerErrorMessageProvider :
        ErrorMessageProvider<ExoPlaybackException> {
        override fun getErrorMessage(e: ExoPlaybackException): Pair<Int, String> {
            var errorString: String = "FDfbnkjds"
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                val cause = e.rendererException
                if (cause is MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    val decoderInitializationException = cause
                    if (decoderInitializationException.decoderName == null) {
                        if (decoderInitializationException.cause is MediaCodecUtil.DecoderQueryException) {
                            errorString = "Dfsgsd"
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString = "FDsfds"
                        } else {
                            errorString = "FDSfdfd"

                        }
                    } else {
                        errorString = "Dsgs"
                    }
                }
            }
            return Pair.create(0, errorString)
        }
    }


    private fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    fun onNewIntent(intent: Intent?) {
        releasePlayer()
        clearStartPosition()
        requireActivity().intent = intent


    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
            if (binding.playerView != null) {
                binding.playerView.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if (binding.playerView != null) {
                binding.playerView.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (binding.playerView != null) {
                binding.playerView.onPause()
            }
            releasePlayer()
        }
        pausePlayer()
        if (mRunnable != null) {
            mHandler!!.removeCallbacks(mRunnable!!)
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (binding.playerView != null) {
                binding.playerView.onPause()
            }
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //releaseAdsLoader();
        releasePlayer()
    }

    fun onRestart() {

        Log.d("Restart", "Yes")
        resumePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()
        updateStartPosition()
        outState.putParcelable(
            KEY_TRACK_SELECTOR_PARAMETERS,
            trackSelectorParameters
        )
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay)
        outState.putInt(KEY_WINDOW, startWindow)
        outState.putLong(KEY_POSITION, startPosition)
    }


    // Activity input
    // Activity input
    fun dispatchKeyEvent(event: KeyEvent?) {
        // See whether the player view wants to handle media or DPAD keys events.
        // return binding.playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event)
    }

    private fun updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector!!.parameters
        }
    }

    private fun updateStartPosition() {
        if (player != null) {
            startAutoPlay = player!!.playWhenReady
            startWindow = player!!.currentWindowIndex
            startPosition = Math.max(0, player!!.contentPosition)
        }
    }


}