package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.database.GifDatabase
import com.example.myapplication.database.dao.GifDao
import com.example.myapplication.enums.Sections
import com.example.myapplication.models.Data
import com.example.myapplication.models.Images
import com.example.myapplication.models.Original
import com.example.myapplication.retrofit.ApiFactory
import com.example.myapplication.retrofit.ApiService
import com.example.myapplication.utils.LiveDataTestUtil
import com.example.myapplication.viewmodel.MainViewModel
import io.reactivex.rxjava3.core.Flowable
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    private lateinit var viewModel : MainViewModel
    private val database = mock(GifDatabase::class.java)
    private val gifDao = mock(GifDao::class.java)
    private val apiService = mock(ApiService::class.java)

    @Before
    fun setUp() {
        `when`(database.gifDao()).thenReturn(gifDao)
        `when`(ApiFactory.apiService).thenReturn(apiService)
        viewModel = MainViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testLoadRandomGif() {
        val gif = Data("41324132412342134", "title", Images(Original("https://media3.giphy.com/media/uosJIPSMTcnywFGH8z/giphy_s.gif?")))
        val gifList = listOf(gif)
        `when`(gifDao.getAllGifs()).thenReturn(Flowable.just(gifList))
        viewModel.loadRandomGif(Sections.OVERALL)

        val liveDataTestUtil = LiveDataTestUtil<Data>()
        val value = liveDataTestUtil.getValue(viewModel.displayGif)

        assertEquals(gif, value)
    }
}