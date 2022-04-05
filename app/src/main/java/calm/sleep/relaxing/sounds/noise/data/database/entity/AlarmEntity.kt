package calm.sleep.relaxing.sounds.noise.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import calm.sleep.relaxing.sounds.noise.model.Alarm

@Entity(tableName = "alarms")
data class AlarmEntity(
    @field:PrimaryKey val alarmId: Int,
    val hour: Int,
    val minute: Int,
    val title: String,
    var started: Boolean,
    val isCustom: Boolean,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
) {
    companion object {
        fun fromAlarm(alarm: Alarm): AlarmEntity {
            return AlarmEntity(
                alarmId = alarm.alarmId,
                hour = alarm.hour,
                minute = alarm.minute,
                title = alarm.title,
                started = alarm.started,
                isCustom = alarm.isCustom,
                monday = alarm.monday,
                tuesday = alarm.tuesday,
                wednesday = alarm.wednesday,
                thursday = alarm.thursday,
                friday = alarm.friday,
                saturday = alarm.saturday,
                sunday = alarm.sunday
            )
        }
    }
}

fun AlarmEntity.toAlarm(): Alarm {
    return Alarm(
        alarmId = alarmId,
        hour = hour,
        minute = minute,
        title = title,
        started = started,
        isCustom = isCustom,
        monday = monday,
        tuesday = tuesday,
        wednesday = wednesday,
        thursday = thursday,
        friday = friday,
        saturday = saturday,
        sunday = sunday
    )
}
