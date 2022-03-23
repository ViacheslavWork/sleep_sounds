package white.noise.sounds.baby.sleep.mediaplayer

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MyPerfectMediaPlayer {
    private var mContext: Context? = null
    private var mResId = 0
    private var mPath: String? = null
    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private constructor(context: Context, resId: Int) {
        mContext = context
        mResId = resId
        try {
            val afd = context.resources.openRawResourceFd(mResId)
            mCurrentPlayer = MediaPlayer()
            mCurrentPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mCurrentPlayer!!.setOnPreparedListener {
                mCurrentPlayer!!.start()
                startNextMediaPlayer(afd, mCurrentPlayer!!.duration)
            }
            mCurrentPlayer!!.prepareAsync()
//            createNextMediaPlayerRaw()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startNextMediaPlayer(afd: AssetFileDescriptor, duration: Int) {
        scope.launch {
            Log.i(TAG, "startNextMediaPlayer: duration = $duration")
            Log.i(TAG, "current media player: $mCurrentPlayer")
            delay(duration.toLong() - 300)
            val previousPlayer = mCurrentPlayer
            mCurrentPlayer = MediaPlayer()
            mCurrentPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mCurrentPlayer!!.setOnPreparedListener {
                mCurrentPlayer!!.start()
                startNextMediaPlayer(afd, mCurrentPlayer!!.duration)
            }
            mCurrentPlayer!!.prepareAsync()
        }
    }

    private fun createNextMediaPlayerRaw() {
        val afd = mContext!!.resources.openRawResourceFd(mResId)
        mNextPlayer = MediaPlayer()
        try {
            mNextPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mNextPlayer!!.setOnPreparedListener {
                mNextPlayer!!.seekTo(0)
                mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
//                mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
            }
            mNextPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @get:Throws(IllegalStateException::class)
    val isPlaying: Boolean
        get() = if (mCurrentPlayer != null) {
            mCurrentPlayer!!.isPlaying
        } else {
            false
        }

    fun setVolume(leftVolume: Float, rightVolume: Float) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer!!.setVolume(leftVolume, rightVolume)
        } else {
            Log.d(TAG, "setVolume()")
        }
    }

    @Throws(IllegalStateException::class)
    fun start() {
        if (mCurrentPlayer != null) {
            Log.d(TAG, "start()")
            mCurrentPlayer!!.start()
        } else {
            Log.d(TAG, "start() | mCurrentPlayer is NULL")
        }
    }

    @Throws(IllegalStateException::class)
    fun stop() {
        if (mCurrentPlayer != null && mCurrentPlayer!!.isPlaying) {
            Log.d(TAG, "stop()")
            mCurrentPlayer!!.stop()
        } else {
            Log.d(
                TAG, "stop() | mCurrentPlayer " +
                        "is NULL or not playing"
            )
        }
    }

    @Throws(IllegalStateException::class)
    fun pause() {
        if (mCurrentPlayer != null && mCurrentPlayer!!.isPlaying) {
            Log.d(TAG, "pause()")
            mCurrentPlayer!!.pause()
        } else {
            Log.d(
                TAG, "pause() | mCurrentPlayer " +
                        "is NULL or not playing"
            )
        }
    }

    fun setWakeMode(c: Context?, mode: Int) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer!!.setWakeMode(c, mode)
            Log.d(TAG, "setWakeMode() | ")
        } else {
            Log.d(
                TAG, "setWakeMode() | " +
                        "mCurrentPlayer is NULL"
            )
        }
    }

    fun setAudioStreamType(audioStreamType: Int) {
        if (mCurrentPlayer != null) {
            mCurrentPlayer!!.setAudioStreamType(audioStreamType)
        } else {
            Log.d(
                TAG, "setAudioStreamType() | " +
                        "mCurrentPlayer is NULL"
            )
        }
    }

    fun release() {
        Log.d(TAG, "release()")
        if (mCurrentPlayer != null) mCurrentPlayer!!.release()
        if (mNextPlayer != null) mNextPlayer!!.release()
    }

    fun reset() {
        if (mCurrentPlayer != null) {
            Log.d(TAG, "reset()")
            mCurrentPlayer!!.reset()
        } else {
            Log.d(
                TAG, "reset() | " +
                        "mCurrentPlayer is NULL"
            )
        }
    }


    companion object {
        private const val TAG = "MyPerfectMediaPlayer"

        fun create(context: Context, resId: Int): MyPerfectMediaPlayer {
            return MyPerfectMediaPlayer(context, resId)
        }
    }

}