package com.atilsamancioglu.artbookcompose.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.atilsamancioglu.artbookcompose.model.Art
import com.atilsamancioglu.artbookcompose.roomdb.ArtDao
import com.atilsamancioglu.artbookcompose.roomdb.ArtDatabase
import kotlinx.coroutines.launch

class ArtViewModel(application: Application) : AndroidViewModel(application) {

        var artList = mutableStateOf<List<Art>>(listOf())
        private var artDatabase : ArtDatabase =
            Room.databaseBuilder(getApplication(), ArtDatabase::class.java, "Arts").build()
        private var artDao : ArtDao = artDatabase.artDao()


        init {
            loadArts()
        }

    suspend fun getArtList(): List<Art> {
        return artDao.getArtWithNameAndId()
    }

    suspend fun getArt(id: Int): Art? {
        return artDao.getArtById(id=id)
    }

        private fun loadArts() {
            viewModelScope.launch {
                val result = getArtList()
                artList.value = result

            }
        }
    }
