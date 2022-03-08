package white.noise.sounds.baby.sleep.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.data.database.mixes.MixesDao
import white.noise.sounds.baby.sleep.data.database.mixes.MixesDatabase
import white.noise.sounds.baby.sleep.data.database.sounds.SoundsDao
import white.noise.sounds.baby.sleep.data.database.sounds.SoundsDatabase

const val SOUNDS_DATABASE_NAME = "Sounds.db"
const val MIXES_DATABASE_NAME = "Sounds.db"

val roomModule = module {
    /**sounds*/
    fun provideSoundsDatabase(context: Context): SoundsDatabase {
        return Room.databaseBuilder(context, SoundsDatabase::class.java, SOUNDS_DATABASE_NAME)
            .fallbackToDestructiveMigration()//dangerous thing!!!
            .build()
    }

    fun provideSoundsDao(database: SoundsDatabase): SoundsDao {
        return database.soundsDao
    }

    single { provideSoundsDatabase(context = androidContext()) }
    single { provideSoundsDao(get()) }

    /**mixes*/
    fun provideMixesDatabase(context: Context): MixesDatabase {
        return Room.databaseBuilder(context, MixesDatabase::class.java, MIXES_DATABASE_NAME)
            .fallbackToDestructiveMigration()//dangerous thing!!!
            .build()
    }

    fun provideMixesDao(database: MixesDatabase): MixesDao {
        return database.mixesDao
    }

    single { provideMixesDatabase(context = androidContext()) }
    single { provideMixesDao(get()) }
}