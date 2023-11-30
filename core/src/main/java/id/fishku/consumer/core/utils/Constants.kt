package id.fishku.consumer.core.utils

import id.fishku.consumer.core.BuildConfig

//val dotenv = dotenv()

object Constants {
    val OTP_TOKEN = BuildConfig.OTP_TOKEN
    val SERVER_KEY = BuildConfig.SERVER_KEY
    const val CONTENT_TYPE = "application/json"
}

//dotenv["OTP_TOKEN"] ?: error("OTP_TOKEN is not set in .env")
//dotenv["SERVER_KEY"] ?: error("SERVER_KEY is not set in .env")