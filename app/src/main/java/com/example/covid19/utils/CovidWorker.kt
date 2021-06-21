package com.example.covid19.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.covid19.R
import com.example.covid19.data.entity.CovidCasesEntity
import com.example.covid19.data.entity.CovidCasesModel
import com.example.covid19.data.entity.mapToModel
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class CovidWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {
    private val repository: CovidRepository by inject(CovidRepository::class.java)

    init {
        createNotificationChannel(appContext)
    }

    companion object {
        private const val CHANNEL_ID = "covid-19"
        private const val CHANNEL_NAME = "covid-19-name"
        private const val CHANNEL_DESCRIPTION = "Case update section"
    }

    override suspend fun doWork(): Result {
        var latestCovidCases: CovidCasesModel? = null
        withContext(Dispatchers.IO) {
            CountryManager.getSavedCountry(applicationContext, this, onComplete = {
                receive<List<CovidCasesEntity>> {
                    bind(repository.getDataByCountry(it.shortName), this@withContext)
                    onReceive = { list ->
                        latestCovidCases = if (!list.isNullOrEmpty()) list.last()
                            .mapToModel() else CovidCasesModel(it.name, 0, 0, 0)
                        latestCovidCases?.let { sendNotification(applicationContext, it) }
                    }
                }
            })
            return@withContext if (latestCovidCases != null) Result.success() else Result.failure()
        }
        return if (latestCovidCases != null) Result.success() else Result.failure()
    }

    private fun sendNotification(context: Context, casesModel: CovidCasesModel) {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_virus_64)
            .setContentTitle(
                "${context.getString(R.string.case_update)} ${
                    convertToNormalDate(casesModel.date)
                }, ${casesModel.country}"
            )
            .setContentText(
                "${context.getString(R.string.infected)}: ${casesModel.infected} ${
                    context.getString(R.string.dead)
                }: ${casesModel.dead} ${context.getString(R.string.recovered)}: ${casesModel.recovered}"
            )
            .setPriority(NotificationCompat.PRIORITY_MIN)
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}