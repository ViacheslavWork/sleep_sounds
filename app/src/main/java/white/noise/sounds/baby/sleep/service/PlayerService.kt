package white.noise.sounds.baby.sleep.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.MainActivity
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.timer.Times
import white.noise.sounds.baby.sleep.utils.Constants
import java.util.*

private const val TAG = "PlayerService"

class PlayerService : LifecycleService() {
    companion object {
        private val _isTimerRunning = MutableLiveData<Boolean>()
        val isTimerRunning: LiveData<Boolean> = _isTimerRunning

        private val _timerTime = MutableLiveData<LocalTime>()
        val timerTime: LiveData<LocalTime> = _timerTime

        private val _isPause = MutableLiveData<Boolean>(false)
        val isPause: LiveData<Boolean> = _isPause

        private val _isPlayable = MutableLiveData<Boolean>(false)
        val isPlayable: LiveData<Boolean> = _isPlayable

        private var _currentSoundsLD = MutableLiveData<Set<Sound>?>(setOf())
        val currentSoundsLD: LiveData<Set<Sound>?> = _currentSoundsLD

        private val _currentMixLD = MutableLiveData<Mix?>(null)
        val currentMixLD: LiveData<Mix?> = _currentMixLD

        val currentSounds = mutableMapOf<Long, Sound>()

        var launcher: String? = ""

        var currentMix: Mix? = null
    }

    private val curSoundsLocal = mutableSetOf<Sound>()

    private var isFirstRun = true

    lateinit var currentTimer: Job

    private val currentPlayers: HashMap<String, ExoPlayer> = hashMapOf<String, ExoPlayer>()
//    private var isPause = false


    private val mBinder: IBinder = MyBinder()

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.ACTION_START_TIMER -> {
                    Log.d(
                        TAG,
                        "Started or resumed service: time - ${it.extras?.get(Constants.EXTRA_TIME)}"
                    )
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }

                    if (::currentTimer.isInitialized) {
                        currentTimer.cancel()
                    }

                    if (it.extras != null) {
                        val time = (it.extras?.get(Constants.EXTRA_TIME) as Times).time
                        if (time != null) {
                            startTimer(time)
                            _isTimerRunning.postValue(true)
                        } else {
                            _isTimerRunning.postValue(false)
                        }
                    }
                }
                Constants.ACTION_PLAY_OR_STOP_SOUND -> {
                    Log.d(TAG, "Play-pause service")
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
                    _currentMixLD.postValue(it.extras?.get(Constants.EXTRA_MIX) as Mix?)
                    /*if (it.extras?.getString(Constants.LAUNCHER) != launcher) {
                        stopAllSounds()
                        launcher = it.extras?.getString(Constants.LAUNCHER)
                    }*/

                    launcher = it.extras?.getString(Constants.LAUNCHER)
                    playStopSound(sound)
                }
                Constants.ACTION_PLAY_SOUND -> {
                    Log.d(TAG, "Play sound")
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
                    _currentMixLD.postValue(it.extras?.get(Constants.EXTRA_MIX) as Mix?)
                    launcher = it.extras?.getString(Constants.LAUNCHER)
                    playSound(sound)
                }
                Constants.ACTION_STOP_SOUND -> {
                    Log.d(TAG, "Stop sound")
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
                    _currentMixLD.postValue(it.extras?.get(Constants.EXTRA_MIX) as Mix?)
                    launcher = it.extras?.getString(Constants.LAUNCHER)
                    stopSound(sound)
                }
                Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS -> {
                    _isPause.postValue(!isPause.value!!)
                }
                Constants.ACTION_STOP_ALL_SOUNDS -> {
                    stopAllSounds()
                }
                Constants.ACTION_CHANGE_VOLUME -> {
                    changeVolume(it.extras?.get(Constants.EXTRA_SOUND) as Sound)
                }
                Constants.ACTION_STOP_SERVICE -> {
                    Log.d(TAG, "Stop service")
                    stopService()
                }
                else -> Unit
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return mBinder
    }

    //Sound part

    fun changeVolume(sound: Sound) {
        Log.i(TAG, "changeVolume: ${sound.volume}")
        currentPlayers[sound.title]?.volume = sound.volume.toFloat() / 100
    }

    private fun stopAllSounds() {
        currentPlayers.forEach{stopSound(it.key)}
        currentPlayers.clear()
        curSoundsLocal.clear()
        _currentSoundsLD.postValue(curSoundsLocal)
        currentSounds.clear()
        if (currentPlayers.isEmpty()) _isPlayable.postValue(false)
    }

    private fun playStopSound(sound: Sound) {
        if (currentPlayers.keys.contains(sound.title)) {
            stopSound(sound.title)
            currentPlayers.remove(sound.title)

            removeFromCurrentSounds(sound)

            if (currentPlayers.isEmpty()) _isPlayable.postValue(false)
        } else {
            currentPlayers[sound.title] =
                playPlayer(sound).apply { if (isPause.value == true) pause() }

            addToCurrentSounds(sound)

            _isPlayable.postValue(true)
        }
    }

    private fun playSound(sound: Sound) {
        if (!currentPlayers.keys.contains(sound.title)) {
            currentPlayers[sound.title] =
                playPlayer(sound).apply { if (isPause.value == true) pause() }

            addToCurrentSounds(sound)

            _isPlayable.postValue(true)
        }
    }

    private fun stopSound(sound: Sound) {
        if (currentPlayers.keys.contains(sound.title)) {
            stopSound(sound.title)
            currentPlayers.remove(sound.title)

            removeFromCurrentSounds(sound)

            if (currentPlayers.isEmpty()) _isPlayable.postValue(false)
        }
    }

    private fun addToCurrentSounds(sound: Sound) {
        curSoundsLocal.add(sound)
        _currentSoundsLD.postValue(curSoundsLocal)
        currentSounds[sound.id] = sound
    }

    private fun removeFromCurrentSounds(sound: Sound) {
        var numForRemove = -1
        val list = curSoundsLocal.toMutableList()
        list.forEachIndexed {index, snd->
            if (snd.id == sound.id) {
                numForRemove = index
            }
        }
        if (numForRemove >= 0) {
            list.removeAt(numForRemove)
        }
        curSoundsLocal.clear()
        curSoundsLocal.addAll(list)
        _currentSoundsLD.postValue(curSoundsLocal)
        currentSounds.remove(sound.id)
    }

    private fun stopSound(soundTitle: String) {
        Log.i(TAG, "stopSound: ")
        val exoPlayer = currentPlayers[soundTitle]
        exoPlayer?.stop()
        exoPlayer?.release()
    }

    private fun playPlayer(sound: Sound): ExoPlayer {
        Log.i(TAG, "playSound: ")
        val mediaItem = MediaItem.fromUri(
            Uri.parse(
                "file:///android_asset/sounds/${
                    sound.category.toString()
                        .lowercase(Locale.getDefault())
                }/${sound.file}"
            )
        )
        val exoPlayer = ExoPlayer.Builder(applicationContext).build()
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
        return exoPlayer
    }

    //Timer part

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder()
        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())

        timerTime.observe(this) {
            val notification = getNotificationBuilder()
                .setContentText(it.toString())
            notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
        }
        isTimerRunning.observe(this) {
            if (!it) {
                stopService()
            }
        }
        isPause.observe(this) {
            if (it) {
                currentPlayers.values.forEach { exoPlayer -> exoPlayer.pause() }
            } else {
                currentPlayers.values.forEach { exoPlayer -> exoPlayer.play() }
            }
        }
        /*currentSoundsLD.observe(this) {
            it?.forEach { sound -> currentSounds[sound.id] = sound }
            currentSounds.forEach{
                Log.i(TAG, "current sound: ${it.value} ")
            }
        }*/
    }

    private fun getNotificationBuilder() = NotificationCompat.Builder(
        this,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_dynamic)
        .setContentTitle("White noise")
        .setContentText("00:00:00")
        .setContentIntent(getMainActivityPendingIntent())

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java)
        /*.also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        }*/, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startTimer(time: LocalTime) {
        currentTimer = tickerFlow(time, Duration.ofSeconds(1))
            .onEach {
                Log.i(TAG, "startTimer: $it")
                _timerTime.postValue(it)
                if (it == LocalTime.of(0, 0, 0)) {
                    _isTimerRunning.postValue(false)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun tickerFlow(duration: LocalTime, period: Duration) = flow {
        var localRestOfTime = duration
        while (localRestOfTime.isAfter(LocalTime.of(0, 0, 0))) {
            localRestOfTime = localRestOfTime.minusSeconds(1)
            emit(localRestOfTime)
            delay(period.toMillis())
        }
    }

    private fun stopService() {
        currentPlayers.forEach { stopSound(it.key) }
        stopForeground(true)
        stopSelf()
    }

    inner class MyBinder : Binder() {
        // Return this instance of MyService so clients can call public methods
        val service: PlayerService
            get() =// Return this instance of MyService so clients can call public methods
                this@PlayerService
    }

}