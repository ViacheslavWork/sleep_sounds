package white.noise.sounds.baby.sleep.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import white.noise.sounds.baby.sleep.data.database.sounds.SoundsDao
import white.noise.sounds.baby.sleep.data.database.sounds.SoundsDatabase

const val DATABASE_NAME = "Sounds.db"

val roomModule = module {
    fun provideDatabase(context: Context): SoundsDatabase {
        return Room.databaseBuilder(context, SoundsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()//dangerous thing!!!
            .build()
    }

    fun provideSoundsDao(database: SoundsDatabase): SoundsDao {
        return database.recipeDao
    }

    single { provideDatabase(context = androidContext()) }
    single { provideSoundsDao(get()) }
}