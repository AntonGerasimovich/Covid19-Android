package test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.covid19.data.entity.CountryEntity
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryTest {
    @Test
    fun countriesListNotEmpty() {
        val repository = CovidRepository()
        runBlocking {
            receive<List<CountryEntity>> {
                bind(repository.loadAllCountries(), this@runBlocking)
                onReceive = {
                    assert(!it.isNullOrEmpty())
                }
            }
        }
    }
}