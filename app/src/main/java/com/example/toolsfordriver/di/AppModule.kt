package com.example.toolsfordriver.di

import android.content.Context
import androidx.room.Room
import com.example.toolsfordriver.data.TFDRoomDAO
import com.example.toolsfordriver.data.TFDRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideTFDRoomDao(tfdRoomDB: TFDRoomDB):TFDRoomDAO = tfdRoomDB.tfdRoomDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): TFDRoomDB =
        Room.databaseBuilder(
            context,
            TFDRoomDB::class.java,
            "tfd_db"
        ).fallbackToDestructiveMigration().build()
}