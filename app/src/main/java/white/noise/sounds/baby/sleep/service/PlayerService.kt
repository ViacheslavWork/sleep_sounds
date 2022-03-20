package white.noise.sounds.baby.sleep.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.MainActivity
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.data.Repository
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.utils.Constants
import white.noise.sounds.baby.sleep.utils.Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS
import white.noise.sounds.baby.sleep.utils.Constants.NOTIFICATION_ID


private const val TAG = "PlayerService"

class PlayerService : LifecycleService() {
    companion object {
/*        private val _isTimerRunning = MutableLiveData<Boolean>()
        val isTimerRunning: LiveData<Boolean> = _isTimerRunning

        private val _timerTime = MutableLiveData<LocalTime>()
        val timerTime: LiveData<LocalTime> = _timerTime*/

        private val _isPause = MutableLiveData<Boolean>(false)
        val isPause: LiveData<Boolean> = _isPause

        private val _isPlayable = MutableLiveData<Boolean>(false)
        val isPlayable: LiveData<Boolean> = _isPlayable

        private var _currentSoundsLD = MutableLiveData<Set<Sound>?>(setOf())
        val currentSoundsLD: LiveData<Set<Sound>?> = _currentSoundsLD

        private val _currentMixLD = MutableLiveData<Mix?>(null)
        val currentMixLD: LiveData<Mix?> = _currentMixLD

        private val _currentLauncherLD = MutableLiveData<String>(null)
        val currentLauncherLD: LiveData<String> = _currentLauncherLD

        val currentSounds = mutableMapOf<Long, Sound>()

        var launcher: String? = ""

        var currentMix: Mix? = null

        var currentMixId: Long = Constants.NO_MIX_ID

        private val currentPlayers: HashMap<String, ExoPlayer> = hashMapOf<String, ExoPlayer>()
    }

    private val repository: Repository by inject()

    private val curSoundsLocal = mutableSetOf<Sound>()

    private var isFirstRun = true

    lateinit var currentTimer: Job

    private val mBinder: IBinder = MyBinder()

    override fun onCreate() {
        Log.i(TAG, "onCreate: ")
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        intent?.let {
            when (it.action) {
/*
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
*/
                Constants.ACTION_PLAY_OR_STOP_SOUND -> {
                    Log.d(TAG, "Play-pause service")
                    launcher = it.extras?.getString(Constants.LAUNCHER)
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
                    /*if (it.extras?.getString(Constants.LAUNCHER) != launcher) {
                    stopAllSounds()
                    launcher = it.extras?.getString(Constants.LAUNCHER)
                }*/
                    currentMixId =
                        intent.extras?.getLong(Constants.EXTRA_MIX_ID, -1) ?: Constants.NO_MIX_ID
                    loadMix(currentMixId)
                    Log.i(TAG, "onStartCommand: mix id $currentMixId")
                    _currentLauncherLD.postValue(launcher)
                    playStopSound(sound)
                }
                Constants.ACTION_PLAY_SOUND -> {
                    Log.d(TAG, "Play sound")
                    launcher = it.extras?.getString(Constants.LAUNCHER)
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
                    currentMixId =
                        intent.extras?.getLong(Constants.EXTRA_MIX_ID, Constants.NO_MIX_ID)
                            ?: Constants.NO_MIX_ID
                    loadMix(currentMixId)
                    Log.i(TAG, "onStartCommand: mix id $currentMixId")
                    _currentLauncherLD.postValue(launcher)
                    playSound(sound)
                }
                Constants.ACTION_STOP_SOUND -> {
                    Log.d(TAG, "Stop sound")
                    val sound = it.extras?.get(Constants.EXTRA_SOUND) as Sound
//                    launcher = it.extras?.getString(Constants.LAUNCHER)
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
                Constants.ACTION_QUIT_APP -> quitApp()
                else -> Unit
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return mBinder
    }

    private fun startForegroundService() {
        Log.i(TAG, "startForegroundService: ")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder()
        val notificationView = RemoteViews(packageName, R.layout.notification_player)
        setUpRemoteView(notificationView)
        notificationBuilder.setCustomContentView(notificationView)
        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())

        observeTimer(notificationManager, notificationBuilder, notificationView)
        observePause(notificationManager, notificationBuilder, notificationView)
        observeCurrentMix(notificationManager, notificationBuilder, notificationView)
        observeCurrentLauncher(notificationManager, notificationBuilder)
        /*currentSoundsLD.observe(this) {
                it?.forEach { sound -> currentSounds[sound.id] = sound }
                currentSounds.forEach{
                    Log.i(TAG, "current sound: ${it.value} ")
                }
            }*/
    }

    /*private fun stopTimer() {
        _isTimerRunning.value = false
        _timerTime.value = LocalTime.of(0, 0, 0)
    }*/

    private fun stopService() {
        isFirstRun = true
//        stopTimer()
        stopAllSounds()
        stopForeground(true)
        stopSelf()
    }

    private fun quitApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(MainActivity.FINISH, true)
        stopService()
        startActivity(intent)
    }

    override fun onDestroy() {
        isFirstRun = true
        super.onDestroy()
    }

    //Sound part

    fun changeVolume(sound: Sound) {
        Log.i(TAG, "changeVolume: ${sound.volume}")
        /*      curSoundsLocal.add(sound)
              _currentSoundsLD.postValue(curSoundsLocal)
              currentSounds[sound.id] = sound*/
        currentPlayers[sound.title]?.volume = sound.volume.toFloat() / 100
        currentSounds[sound.id] = sound
    }

    private fun stopAllSounds() {
        Log.i(TAG, "stopAllSounds: ${currentPlayers.map { it.key }}")
        currentPlayers.forEach { stopSound(it.key) }
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
            if (currentPlayers.isEmpty()) stopService()
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
            if (currentPlayers.isEmpty()) stopService()
        }
    }

    private fun addToCurrentSounds(sound: Sound) {
        curSoundsLocal.add(sound)
        _currentSoundsLD.postValue(curSoundsLocal)
        currentSounds[sound.id] = sound
    }

    private fun removeFromCurrentSounds(sound: Sound) {
        currentSounds.remove(sound.id)
        var numForRemove = -1
        val list = curSoundsLocal.toMutableList()
        list.forEachIndexed { index, snd ->
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
    }

    private fun stopSound(soundTitle: String) {
        Log.i(TAG, "stopSound: $soundTitle")
        val exoPlayer = currentPlayers[soundTitle]
        exoPlayer?.stop()
        exoPlayer?.release()
    }

    private fun playPlayer(sound: Sound): ExoPlayer {
        Log.i(TAG, "playSound: ")
        val mediaItem = MediaItem.fromUri(
            RawResourceDataSource.buildRawResourceUri(sound.file)
        )
        val exoPlayer = ExoPlayer.Builder(applicationContext).build()
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.volume = sound.volume.toFloat() / 100
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
        return exoPlayer
    }

    /*private fun checkIsAddSoundAvailable(): Boolean {
        return currentPlayers.size < Constants.MAX_SELECTABLE_SOUNDS
    }*/
    //notification part

    private fun getNotificationBuilder() = NotificationCompat.Builder(
        this,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(false)
        .setOngoing(true)
        .setCategory(Notification.EXTRA_MEDIA_SESSION)
        .setSmallIcon(R.drawable.ic_moon)
        .setColor(resources.getColor(R.color.dark_blue, null))
        .setColorized(true)
        .setContentTitle("White noise")
        .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
        .setContentIntent(getMainActivityPendingIntent())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java)
            .also {
                if (launcher == Constants.MIX_LAUNCHER) it.action = MainActivity.ACTION_SHOW_MIX
                else if (launcher == Constants.SOUNDS_LAUNCHER) it.action =
                    MainActivity.ACTION_SHOW_SOUNDS
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun setUpRemoteView(notificationView: RemoteViews) {
        val playPauseIntent = Intent(this, PlayerService::class.java)
        playPauseIntent.action = ACTION_PLAY_OR_PAUSE_ALL_SOUNDS
        val playPauseIntentPending = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val closeIntent = Intent(this, PlayerService::class.java)
        closeIntent.action = Constants.ACTION_QUIT_APP
        val closeIntentPending = PendingIntent.getService(
            this,
            0,
            closeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationView.setOnClickPendingIntent(
            R.id.notification_play_pause_btn,
            playPauseIntentPending
        );
        notificationView.setOnClickPendingIntent(R.id.notification_cross_btn, closeIntentPending);
    }

    private fun observeCurrentMix(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder,
        notificationView: RemoteViews
    ) {
        currentMixLD.observe(this) {
            Log.i(TAG, "observeCurrentMix: $it")
            for (notification in notificationManager.activeNotifications) {
                if (notification.id == Constants.NOTIFICATION_ID) {
                    if (it != null) {
                        notificationView.setTextViewText(
                            R.id.notification_title_tv,
                            it.title
                        )
                        notificationManager.notify(
                            Constants.NOTIFICATION_ID,
                            notificationBuilder.build()
                        )
                    } else {
                        notificationView.setTextViewText(
                            R.id.notification_title_tv,
                            applicationContext.resources.getString(R.string.playing)
                        )
                        notificationManager.notify(
                            Constants.NOTIFICATION_ID,
                            notificationBuilder.build()
                        )
                    }
                }
            }
        }
    }

    private fun observeCurrentLauncher(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder,
    ) {
        currentLauncherLD.observe(this) {
            Log.i(TAG, "observeCurrentLauncher: ")
            for (notification in notificationManager.activeNotifications) {
                if (notification.id == Constants.NOTIFICATION_ID) {
                    notificationBuilder.setContentIntent(getMainActivityPendingIntent())
                    notificationManager.notify(
                        Constants.NOTIFICATION_ID,
                        notificationBuilder.build()
                    )
                }
            }
        }
    }


    private fun observePause(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder,
        notificationView: RemoteViews
    ) {
        isPause.observe(this) {
            Log.i(TAG, "observePause: ")
            if (it) {
                currentPlayers.values.forEach { exoPlayer -> exoPlayer.pause() }
                notificationView.setImageViewResource(
                    R.id.notification_play_pause_btn,
                    R.drawable.ic_icn_play
                )
                notificationManager.notify(
                    Constants.NOTIFICATION_ID,
                    notificationBuilder.build()
                )
            } else {
                currentPlayers.values.forEach { exoPlayer -> exoPlayer.play() }
                notificationView.setImageViewResource(
                    R.id.notification_play_pause_btn,
                    R.drawable.icn_pause
                )
                notificationManager.notify(
                    Constants.NOTIFICATION_ID,
                    notificationBuilder.build()
                )
            }
        }
    }

    private fun observeTimer(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder,
        notificationView: RemoteViews
    ) {
        TimerService.isTimerStarted.observe(this) {
            if (it) {
                TimerService.timerTime.observe(this) { timerTime ->
                    if (timerTime.isAfter(LocalTime.of(0, 0, 0))) {
                        notificationView.setViewVisibility(
                            R.id.notification_time_tv,
                            View.VISIBLE
                        )
                    } else {
                        notificationView.setViewVisibility(
                            R.id.notification_time_tv,
                            View.GONE
                        )
                    }
                    notificationView.setTextViewText(
                        R.id.notification_time_tv,
                        String.format(
                            applicationContext.getString(
                                R.string.time_format,
                                timerTime.hour,
                                timerTime.minute,
                                timerTime.second
                            )
                        )
                    )
                    if (notificationManager
                            .activeNotifications
                            .map { activeNotification -> activeNotification.id }
                            .contains(NOTIFICATION_ID)
                    ) {
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            notificationBuilder.build()
                        )
                    }
                }
            } else {
                if(!isFirstRun) _isPause.postValue(true)
            }
        }
    }

    //Timer part

    /* private fun startTimer(time: LocalTime) {
         currentTimer = tickerFlow(time, Duration.ofSeconds(1))
             .onEach {
                 Log.i(TAG, "startTimer: $it")
                 _timerTime.postValue(it)
                 if (it == LocalTime.of(0, 0, 0)) {
                     _isTimerRunning.postValue(false)
                 }
             }
             .launchIn(lifecycleScope)
     }*/

    private fun tickerFlow(duration: LocalTime, period: Duration) = flow {
        var localRestOfTime = duration
        while (localRestOfTime.isAfter(LocalTime.of(0, 0, 0))) {
            localRestOfTime = localRestOfTime.minusSeconds(1)
            emit(localRestOfTime)
            delay(period.toMillis())
        }
    }

    inner class MyBinder : Binder() {
        // Return this instance of MyService so clients can call public methods
        val service: PlayerService
            get() =// Return this instance of MyService so clients can call public methods
                this@PlayerService
    }

    //Data part

    private fun loadMix(mixId: Long) {
        if (mixId != Constants.NO_MIX_ID)
            lifecycleScope.launch { _currentMixLD.postValue(repository.getMix(mixId)) }
        else _currentMixLD.postValue(null)
    }
}