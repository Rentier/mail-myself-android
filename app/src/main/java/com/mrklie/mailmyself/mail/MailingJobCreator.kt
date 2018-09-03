package com.mrklie.mailmyself.mail

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


class MailingJobCreator : JobCreator {
    override fun create(tag: String): Job? = when (tag) {
        MailJob.TAG -> MailJob()
        else -> null
    }
}