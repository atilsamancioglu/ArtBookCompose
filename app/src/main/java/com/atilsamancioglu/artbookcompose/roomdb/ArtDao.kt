package com.atilsamancioglu.artbookcompose.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.atilsamancioglu.artbookcompose.model.Art

@Dao
interface ArtDao {

    @Query("SELECT name,id FROM Art")
    suspend fun getArtWithNameAndId(): List<Art>

    @Query("SELECT * FROM Art WHERE id = :id")
    suspend fun getArtById(id: Int): Art?

    @Insert
    suspend fun insert(art: Art)

    @Delete
    suspend fun delete(art: Art)

}