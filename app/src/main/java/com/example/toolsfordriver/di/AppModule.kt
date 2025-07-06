package com.example.toolsfordriver.di

import android.content.Context
import androidx.room.Room
import com.example.toolsfordriver.data.TFDRoomDAO
import com.example.toolsfordriver.data.TFDRoomDB
import com.example.toolsfordriver.data.model.service.AccountService
import com.example.toolsfordriver.data.model.service.FirestoreService
import com.example.toolsfordriver.data.model.service.StorageService
import com.example.toolsfordriver.data.model.service.impl.AccountServiceImpl
import com.example.toolsfordriver.data.model.service.impl.FirestoreServiceImpl
import com.example.toolsfordriver.data.model.service.impl.StorageServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Binds
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
    fun provideTFDRoomDao(tfdRoomDB: TFDRoomDB): TFDRoomDAO = tfdRoomDB.tfdRoomDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): TFDRoomDB =
        Room.databaseBuilder(
                context,
                TFDRoomDB::class.java,
                "tfd_db"
            ).fallbackToDestructiveMigration(false).build()
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides fun auth(): FirebaseAuth = Firebase.auth
    @Provides fun firestore(): FirebaseFirestore = Firebase.firestore
    @Provides fun storage(): FirebaseStorage = Firebase.storage
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds
    abstract fun provideFirestoreService(impl: FirestoreServiceImpl): FirestoreService
    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}