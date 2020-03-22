package com.example.starwarskamino.data.resident

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.starwarskamino.data.resident.response.ResidentResponse
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
 * Unit tests for [ResidentsRepository].
 */
@ExperimentalCoroutinesApi
open class ResidentsRepositoryTest {
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

    private lateinit var repo : ResidentsRepository

    @Before
    fun setup() {
        repo = ResidentsRepository.getInstance(fakeApi, contextProvider)
    }

    @Test
    fun testGetResident() = runBlockingTest {
        // All coroutines start as paused. To start them, call runCurrent()
        pauseDispatcher {
            val data = repo.getResident(this, "10", false)
            // Coroutine is paused, so we're gonna see loading
            assertThat(data.value, instanceOf(Result.Loading::class.java))

            // run the coroutine
            runCurrent()

            // api returns error by default
            assertThat(data.value, instanceOf(Result.Error::class.java))

            // Create a success response on api and get Planet again
            val response = ResidentResponse()
            response.name = "Test1"
            fakeApi.createSuccessResponse(FakeApi.Type.Resident, response)
            repo.getResident(this, "10", false)
            runCurrent()
            assertThat(data.value, instanceOf(Result.Success::class.java))
            assertEquals((data.value as Result.Success).data, response)
            assertEquals((data.value as Result.Success).data.name, response.name)

            /* Create another response on api, get planet again, but since it's cached and we don't
                force a refresh, it should still return the first one.
             */

            val response2 = ResidentResponse()
            response2.name = "Test2"
            fakeApi.createSuccessResponse(FakeApi.Type.Resident, response2)
            repo.getResident(this, "10", false)
            runCurrent()
            assertThat(data.value, instanceOf(Result.Success::class.java))
            assertEquals((data.value as Result.Success).data, response)
            assertEquals((data.value as Result.Success).data.name, response.name)

            /*
            Since we now force a refresh, it should reference the second one
             */
            repo.getResident(this, "10", true)
            runCurrent()
            assertThat(data.value, instanceOf(Result.Success::class.java))
            assertEquals((data.value as Result.Success).data, response2)
            assertEquals((data.value as Result.Success).data.name, response2.name)
        }
    }

    @After
    fun teardown() {
        ResidentsRepository.destroyInstance()
    }
}