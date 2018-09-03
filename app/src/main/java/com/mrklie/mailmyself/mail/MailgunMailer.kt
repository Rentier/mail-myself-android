package com.mrklie.mailmyself.mail

import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class MailgunMailer {
    val client = OkHttpClient()

    fun sendMail(url: String, apiKey: String, email: String, subject: String, content: String) {
        val formBody = FormBody.Builder()
                .add("from", "mailmyself@mrklie.com")
                .add("to", email)
                .add("subject", subject)
                .add("text", content)
                .build()

        val request = Request.Builder()
                .url(url + "/messages")
                .post(formBody)
                .addBasicAuthHeaders("api", apiKey)
                .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Sending email errored, response was ${response.body()?.string()}")
        }
    }
}

fun Request.Builder.addBasicAuthHeaders(username: String, password: String): Request.Builder {
    val credential = Credentials.basic(username, password)
    return header("Authorization", credential)
}