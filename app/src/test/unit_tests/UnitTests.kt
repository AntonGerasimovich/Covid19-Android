package unit_tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.covid19.data.entity.CountryModel
import com.example.covid19.data.entity.CovidCasesEntity
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import com.example.covid19.ui.components.adjustNumber
import com.example.covid19.ui.overview.OverviewViewModel
import com.example.covid19.utils.CountryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UnitTests {
    @Test
    fun countriesListNotEmpty() {
        CountryManager.onCountriesLoaded = {
            assert(!it.isNullOrEmpty())
        }
        runBlocking {
            CountryManager.loadAllCountries(this)
        }
    }

    @Test
    fun covidCasesNotEmpty() {
        val repository = CovidRepository()
        runBlocking {
            receive<List<CovidCasesEntity>> {
                bind(repository.getDataByCountry("belarus"), this@runBlocking)
                onReceive = {
                    assert(!it.isNullOrEmpty())
                }
                onError = { _, _ ->
                    assert(false)
                }
            }
        }
    }

    @Test
    fun covidCasesByCountryNotEmpty() {
        val repository = CovidRepository()
        val viewModel = OverviewViewModel(repository)
        runBlocking {
            viewModel.covidCases.onEach {
                assert(it.country.isNotEmpty())
                assert(it.date.isNotEmpty())
                assert(it.dead >= 0)
                assert(it.recovered >= 0)
                assert(it.infected >= 0)
            }.take(1).flowOn(Dispatchers.Unconfined).launchIn(this)
        }
        viewModel.getCovidCases(CountryModel(name = "Belarus", shortName = "belarus"))
    }

    @Test
    fun isNumberConvertedCorrectly() {
        assert(adjustNumber(1234) == "1.2k")
        assert(adjustNumber(12345) == "12.3k")
        assert(adjustNumber(123456) == "123.4k")
        assert(adjustNumber(1234567) == "1.2m")
        assert(adjustNumber(12345678) == "12.3m")
    }
}