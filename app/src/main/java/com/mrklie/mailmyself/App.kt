package com.mrklie.mailmyself

import android.app.Application
import com.evernote.android.job.JobManager
import com.mrklie.mailmyself.mail.MailJob
import com.mrklie.mailmyself.mail.MailingJobCreator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(MailingJobCreator())

        MailJob.scheduleJob()
    }
}