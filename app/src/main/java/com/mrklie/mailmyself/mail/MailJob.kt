package com.mrklie.mailmyself.mail

import android.support.v7.preference.PreferenceManager
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.mrklie.mailmyself.persistence.AppDatabase
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit


class MailJob : Job (), AnkoLogger {
    companion object: AnkoLogger {
        val TAG: String = "MAIL_JOB"

        fun scheduleJob() {
            val numberOfMailJobs = JobManager.instance().getAllJobRequestsForTag(MailJob.TAG).size
            if (numberOfMailJobs > 1) {
                JobManager.instance().cancelAllForTag(MailJob.TAG)
            }

            info("Number of mailing jobs: [${numberOfMailJobs}]")

            if (numberOfMailJobs == 0) {
                info("Scheduling new job request")
                JobRequest.Builder(MailJob.TAG)
                        .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(15))
                        .setRequiredNetworkType(JobRequest.NetworkType.NOT_ROAMING)
                        .setRequirementsEnforced(true)
                        .build()
                        .schedule()
            }
        }
    }

    override fun onRunJob(params: Params): Result   {
        info("Running mail job")
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val email = prefs.getString("pref_email", "")
        val apiKey = prefs.getString("pref_apikey", "")
        val url = prefs.getString("pref_url", "")

        if (email.isNullOrBlank() or apiKey.isNullOrBlank() or url.isNullOrBlank()) {
            error("Could not send mails as some preferences were not set correctly")
            return Result.SUCCESS
        }

        val mailer = MailgunMailer()

        AppDatabase.getInstance(context).noteDao().findAllUnsentAndNonEmpty()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flattenAsObservable { it }
                .doOnNext {
                    val subject = "Mail myself - ${it.date}"
                    val content = it.text

                    mailer.sendMail(url!!, apiKey!!, email!!, subject, content)
                    it.sent = true
                    AppDatabase.getInstance(context).noteDao().updateNote(it)
                }
                .subscribeBy (
                        onNext = {
                            info("Finished sending note with id [${it.id}]")
                        },
                        onError = {
                            error("Sending mail threw exception: [${it.message}]")
                        }
                )

        return Result.SUCCESS
    }
}
