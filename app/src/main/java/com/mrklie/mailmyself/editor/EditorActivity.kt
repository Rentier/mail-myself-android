package com.mrklie.mailmyself.editor

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.mrklie.mailmyself.R
import com.mrklie.mailmyself.list.Note
import com.mrklie.mailmyself.persistence.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_editor.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class EditorActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
    }

    override fun onResume() {
        super.onResume()
        val id: Long = intent.getLongExtra("id", -1)

        info("Loading note with id [${id}]")

        AppDatabase.getInstance(applicationContext).noteDao().findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            note = it
                            edittext_editor.setText(note.text, TextView.BufferType.EDITABLE)
                        },
                        onError =  { toast(it.message!!) }
                )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                saveNote()
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        note.text = edittext_editor.text.toString()
        Schedulers.io().scheduleDirect {
            AppDatabase.getInstance(applicationContext).noteDao().updateNote(note)
        }
    }

    override fun onBackPressed() {
        saveNote()
        super.onBackPressed()
    }
}

fun Context.startEditorActivity(id: Long) {
    startActivity(intentFor<EditorActivity>("id" to id))
}