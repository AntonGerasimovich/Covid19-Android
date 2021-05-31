package com.example.covid19.network.result

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}