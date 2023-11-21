package id.fishku.consumer.core.utils

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()

object Constants {
    val OTP_TOKEN = dotenv["OTP_TOKEN"] ?: error("OTP_TOKEN is not set in .env")
    val SERVER_KEY = dotenv["SERVER_KEY"] ?: error("SERVER_KEY is not set in .env")
    const val CONTENT_TYPE = "application/json"
}
