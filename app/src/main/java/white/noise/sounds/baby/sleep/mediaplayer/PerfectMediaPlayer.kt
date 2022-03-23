import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.util.Log
import java.io.IOException

/**
 * Created by viksaaskool on 13-04-2017.
 */
class PerfectLoopMediaPlayer {
    private var mContext: Context? = null
    private var mResId = 0
    private var mPath: String? = null
    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    private constructor(context: Context, resId: Int) {
        mContext = context
        mResId = resId
        try {
            val afd = context.resources.openRawResourceFd(mResId)
            mCurrentPlayer = MediaPlayer()
            mCurrentPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mCurrentPlayer!!.setOnPreparedListener { mCurrentPlayer!!.start() }
            mCurrentPlayer!!.prepareAsync()
            createNextMediaPlayerRaw()
        } catch (e: IOException) {
            e.printStackTrace()
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
                mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
            }
            mNextPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private constructor(context: Context, path: String) {
        mContext = context
        mPath = path
        try {
            mCurrentPlayer!!.setDataSource(mPath)
            mCurrentPlayer!!.setOnPreparedListener { mCurrentPlayer!!.start() }
            mCurrentPlayer!!.prepareAsync()
            createNextMediaPlayerPath()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createNextMediaPlayerPath() {
        mNextPlayer = MediaPlayer()
        try {
            mNextPlayer!!.setDataSource(mPath)
            mNextPlayer!!.setOnPreparedListener {
                mNextPlayer!!.seekTo(0)
                mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
                mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
            }
            mNextPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val onCompletionListener =
        OnCompletionListener { mediaPlayer ->
            mCurrentPlayer = mNextPlayer
            createNextMediaPlayerRaw()
            mediaPlayer.release()
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
        private val TAG = PerfectLoopMediaPlayer::class.java.name

        /**
         * Creating instance of the player with given context and raw resource
         *
         * @param context - context
         * @param resId   - raw resource
         * @return new instance
         */
        fun create(context: Context, resId: Int): PerfectLoopMediaPlayer {
            return PerfectLoopMediaPlayer(context, resId)
        }

        /**
         * Creating instance of the player with given context
         * and internal memory/SD path resource
         *
         * @param context - context
         * @param path    - internal memory/SD path to sound resource
         * @return new instance
         */
        fun create(context: Context, path: String): PerfectLoopMediaPlayer {
            return PerfectLoopMediaPlayer(context, path)
        }
    }
}