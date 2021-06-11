package test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.covid19.data.entity.Country
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
        val onReceiveData: (Boolean) -> Unit = {
            assert(it)
        }
        runBlocking {
            receive<List<Country>> {
                bind(repository.loadAllCountries(), this@runBlocking)
                onReceive = {
                    onReceiveData(!it.isNullOrEmpty())
                }
            }
        }
    }
}