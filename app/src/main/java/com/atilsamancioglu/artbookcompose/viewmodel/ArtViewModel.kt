package com.atilsamancioglu.artbookcompose.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.atilsamancioglu.artbookcompose.model.Art
import com.atilsamancioglu.artbookcompose.roomdb.ArtDao
import com.atilsamancioglu.artbookcompose.roomdb.ArtDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtViewModel(application: Application) : AndroidViewModel(application) {

    private var artDatabase: ArtDatabase =
        Room.databaseBuilder(getApplication(), ArtDatabase::class.java, "Arts").build()
    private var artDao: ArtDao = artDatabase.artDao()

    val artList = mutableStateOf<List<Art>>(listOf())
    val selectedArt = mutableStateOf<Art>(Art("","","", ByteArray(1)))

    /*
    init {
        getArtList()
    }

     */

    fun getArtList() {
        viewModelScope.launch(Dispatchers.IO) {
            artList.value = artDao.getArtWithNameAndId()
        }
    }

    fun getArt(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val artFromRoom = artDao.getArtById(id = id)
            artFromRoom?.let {
                selectedArt.value = it
            }
        }
    }

    suspend fun saveArt(art: Art) {
        viewModelScope.launch(Dispatchers.IO) {
            artDao.insert(art)
        }
    }

    suspend fun deleteArt(art: Art) {
        viewModelScope.launch(Dispatchers.IO) {
            artDao.delete(art)
        }
    }

}