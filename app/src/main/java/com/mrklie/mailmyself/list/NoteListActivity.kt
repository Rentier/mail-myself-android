package com.mrklie.mailmyself.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.mrklie.mailmyself.R
import com.mrklie.mailmyself.persistence.AppDatabase
import com.mrklie.mailmyself.settings.startSettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notes_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class NoteListActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerview_notes_list.layoutManager = linearLayoutManager
        recyclerview_notes_list.adapter = NoteAdapter(this, emptyList())

        AppDatabase.getInstance(applicationContext).noteDao().findAllUnsent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            recyclerview_notes_list.swapAdapter( NoteAdapter(this, it), true)
                        },
                        onError = { toast(it.message!!) }
                )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            startSettingsActivity()
            true
        }

        R.id.action_add_note -> {
            Schedulers.io().scheduleDirect {
                val note = Note("")
                AppDatabase.getInstance(applicationContext).noteDao().insertNote(note)
            }
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
