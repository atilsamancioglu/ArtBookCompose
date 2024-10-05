package com.atilsamancioglu.artbookcompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.atilsamancioglu.artbookcompose.model.Art
import com.atilsamancioglu.artbookcompose.roomdb.ArtDao
import com.atilsamancioglu.artbookcompose.roomdb.ArtDatabase
import kotlinx.coroutines.launch

class ArtViewModel(application: Application) : AndroidViewModel(application) {

    private var artDatabase: ArtDatabase =
        Room.databaseBuilder(getApplication(), ArtDatabase::class.java, "Arts").build()
    private var artDao: ArtDao = artDatabase.artDao()


    suspend fun getArtList(): List<Art> {
        return artDao.getArtWithNameAndId()
    }

    suspend fun getArt(id: Int): Art? {
        return artDao.getArtById(id = id)
    }

    suspend fun saveArt(art: Art) {
        viewModelScope.launch {
            artDao.insert(art)
        }
    }

    suspend fun deleteArt(art: Art) {
        viewModelScope.launch {
            artDao.delete(art)
        }
    }

}