package com.example.starwarskamino.data.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.starwarskamino.data.main.response.LikeResponse
import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.server.FakeApi
import com.example.starwarskamino.testutils.MainCoroutineRule
import com.example.starwarskamino.testutils.TestCoroutineContextProvider
import com.example.starwarskamino.general.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [PlanetRepository].
 */
@ExperimentalCoroutinesApi
open class PlanetRepositoryTest {
    // Run tasks synchronously. Needed to test code with LiveData
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val mainCoroutineRule = MainCoroutineRule()

    // Use a Fake DataSource so we have all necessary control over it
    private val fakeApi = FakeApi()
    private val contextProvider = TestCoroutineContextProvider()

    private lateinit var repo : PlanetRepository

    @Before
    fun setup() {
        repo = PlanetRepository.getInstance(fakeApi, contextProvider)
    }

    @Test
    fun testGetPlanet() = runBlockingTest {
        // All coroutines start as paused. To start them, call runCurrent()
        pauseDispatcher {
            val data = repo.getPlanet(this, 10)
            // Coroutine is paused, so we're gonna see loading
            assertThat(data.value, instanceOf(Result.Loading::class.java))

            // run the coroutine
            runCurrent()

            // api returns error by default
            assertThat(data.value, instanceOf(Result.Error::class.java))

            // Create a success response on api and get Planet again
            val response = PlanetResponse()
            fakeApi.createSuccessResponse(FakeApi.Type.Planet, response)
            repo.getPlanet(this, 10)
            runCurrent()
            assertThat(data.value, instanceOf(Result.Success::class.java))
            assertEquals((data.value as Result.Success).data, response)
        }
    }

    @Test
    fun testLikePlanet() = runBlockingTest {
        pauseDispatcher {
            val data = repo.likePlanet(this, 10)
            // Coroutine is paused, so we're gonna see loading
            assertThat(data.value, instanceOf(Result.Loading::class.java))

            // run the coroutine
            runCurrent()

            // api returns error by default
            assertThat(data.value, instanceOf(Result.Error::class.java))

            val response = LikeResponse()
            fakeApi.createSuccessResponse(FakeApi.Type.Like, response)
            repo.likePlanet(this, 10)
            runCurrent()
            assertThat(data.value, instanceOf(Result.Success::class.java))
            assertEquals((data.value as Result.Success).data, response)
        }
    }

    @After
    fun teardown() {
        PlanetRepository.destroyInstance()
    }
}