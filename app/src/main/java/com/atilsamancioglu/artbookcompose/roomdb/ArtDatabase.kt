package com.atilsamancioglu.artbookcompose.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atilsamancioglu.artbookcompose.model.Art

@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}