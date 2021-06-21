package com.example.covid19.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.covid19.data.entity.CountryEntity
import com.example.covid19.data.entity.CountryModel
import com.example.covid19.data.entity.mapToModel
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

val Context.dataStore by preferencesDataStore("follows")

object CountryManager {
    private val covidRepository: CovidRepository by inject(CovidRepository::class.java)
    private var countries: List<CountryModel> = listOf()
    var onCountriesLoaded: (List<CountryModel>) -> Unit = {}

    fun loadAllCountries(scope: CoroutineScope) {
        receive<List<CountryEntity>> {
            bind(covidRepository.loadAllCountries(), scope)
            onReceive = {
                countries = it.map { countryEntity -> countryEntity.mapToModel() }
                onCountriesLoaded(countries)
            }
        }
    }

    fun getSavedCountry(
        context: Context,
        scope: CoroutineScope,
        onComplete: (CountryModel) -> Unit
    ) {
        val dataStore = context.dataStore
        dataStore.data.onEach {
            onComplete(
                CountryModel(
                    name = it[UserScheme.FIELD_NAME] ?: "",
                    shortName = it[UserScheme.FIELD_SHORT_NAME] ?: "",
                    following = it[UserScheme.FIELD_FOLLOWING] ?: false
                )
            )
        }.take(1).flowOn(Dispatchers.IO).launchIn(scope)
    }

    fun overwriteCountry(context: Context, scope: CoroutineScope, countryModel: CountryModel) {
        val dataStore = context.dataStore
        scope.launch {
            dataStore.edit {
                it[UserScheme.FIELD_NAME] = countryModel.name
                it[UserScheme.FIELD_SHORT_NAME] = countryModel.shortName
                it[UserScheme.FIELD_FOLLOWING] = countryModel.following
            }
        }
    }

    fun isCountryFollowed(
        context: Context,
        scope: CoroutineScope,
        countryModel: CountryModel,
        onComplete: (Boolean) -> Unit
    ) {
        val dataStore = context.dataStore
        dataStore.data.onEach {
            val isFollowing = if (it[UserScheme.FIELD_NAME] == countryModel.name) {
                it[UserScheme.FIELD_FOLLOWING]
            } else false
            if (isFollowing != null) {
                onComplete(isFollowing)
            }
        }.take(1).flowOn(Dispatchers.IO).launchIn(scope)
    }

    object UserScheme {
        val FIELD_NAME = stringPreferencesKey("name")
        val FIELD_SHORT_NAME = stringPreferencesKey("shortName")
        val FIELD_FOLLOWING = booleanPreferencesKey("following")
    }
}