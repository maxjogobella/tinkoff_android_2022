package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.database.GifDatabase
import com.example.myapplication.database.dao.GifDao
import com.example.myapplication.models.Data
import com.example.myapplication.models.Images
import com.example.myapplication.models.Original
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var gifDao: GifDao
    private lateinit var db: GifDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GifDatabase::class.java).build()
        gifDao = db.gifDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGif() {
        var allGifs = gifDao.getAllGifs().blockingFirst()
        assertEquals(0, allGifs.size)
        val gif = Data("41324132412342134", "title", Images(Original("https://media3.giphy.com/media/uosJIPSMTcnywFGH8z/giphy_s.gif?")))
        gifDao.insertGif(gif)

        allGifs = gifDao.getAllGifs().blockingFirst()
        assertEquals(1, allGifs.size)
        assertEquals(allGifs[0], gif)
    }

    @Test
    @Throws(Exception::class)
    fun removeGif() {
        var allGifs = gifDao.getAllGifs().blockingFirst()
        assertEquals(0, allGifs.size)

        val gif = Data("41324132412342134", "title", Images(Original("https://media3.giphy.com/media/uosJIPSMTcnywFGH8z/giphy_s.gif?")))
        gifDao.insertGif(gif)

        allGifs = gifDao.getAllGifs().blockingFirst()
        assertEquals(1, allGifs.size)
        assertEquals(allGifs[0], gif)

        gifDao.deleteGif(gif.id).blockingAwait()
        allGifs = gifDao.getAllGifs().blockingFirst()
        assertEquals(0, allGifs.size)
    }
}

