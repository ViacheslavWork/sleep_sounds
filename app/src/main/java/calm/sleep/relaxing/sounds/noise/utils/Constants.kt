package calm.sleep.relaxing.sounds.noise.utils

import org.threeten.bp.LocalTime

object Constants {
    const val PREFERENCE_ALARM_IS_SET =
        "white.noise.sounds.baby.sleep.utils.PREFERENCE_ALARM_IS_SET"

    const val CUSTOM_MIX_EXTERNAL_DIRECTORY = "custom_mix_images"
    const val LOG_EXTERNAL_DIRECTORY = "logs"

    const val ACTION_START_TIMER = "ACTION_START_TIMER"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_PLAY_OR_PAUSE_ALL_SOUNDS = "ACTION_PLAY_OR_PAUSE_ALL_SOUNDS"
    const val ACTION_PLAY_OR_STOP_SOUND = "ACTION_PLAY_OR_STOP_SOUND"
    const val ACTION_PLAY_SOUND = "ACTION_PLAY_SOUND"
    const val ACTION_STOP_SOUND = "ACTION_STOP_SOUND"
    const val ACTION_STOP_ALL_SOUNDS = "ACTION_STOP_ALL_SOUNDS"
    const val ACTION_CHANGE_VOLUME = "ACTION_CHANGE_VOLUME"
    const val ACTION_QUIT_APP = "ACTION_QUIT_APP"

    const val LAUNCHER = "LAUNCHER"
    const val SOUNDS_LAUNCHER = "SOUNDS_LAUNCHER"
    const val MIX_LAUNCHER = "MIX_LAUNCHER"
    const val PLAYER_LAUNCHER = "PLAYER_LAUNCHER"

    const val EXTRA_TIME = "EXTRA_TIME"
    const val EXTRA_SOUND = "EXTRA_SOUND"
    const val EXTRA_MIX = "EXTRA_MIX"
    const val EXTRA_MIX_ID = "EXTRA_MIX_ID"


    const val NOTIFICATION_CHANNEL_ID = "player_channel"
    const val NOTIFICATION_CHANNEL_NAME = "player"
    const val PLAYER_NOTIFICATION_ID = 1
    const val BEDTIME_REMINDER_NOTIFICATION_ID = 2
    const val FIREBASE_NOTIFICATION_ID = 3

    const val NO_MIX_ID = -1L

    const val SUBSCRIPTION_ID_MONTH = "month_subscribe"
    const val SUBSCRIPTION_ID_YEAR = "year_subscribe"

    const val ALARM_ID = "ALARM_ID"
    const val CUSTOM_ALARM_ID = 1
    const val EVERY_DAY_ALARM_ID = 2
    val EVERY_DAY_REMINDER_TIME: LocalTime = LocalTime.of(22,0)

    const val IS_ON_BOARDING_KEY = "is_onboarding"
}