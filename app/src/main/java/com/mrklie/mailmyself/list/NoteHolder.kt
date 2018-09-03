package com.mrklie.mailmyself.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mrklie.mailmyself.editor.startEditorActivity
import kotlinx.android.synthetic.main.layout_notes_list_item.view.*
import java.util.*

class NoteHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
    val textview_title = view.textview_notes_list_title
    val textview_preview = view.textview_notes_list_preview
    lateinit var note: Note

    fun bind(note: Note) {
        textview_title?.text = "${note.id} - ${formatDate(context, note.date)}"
                textview_preview?.text = note.text

        view.setOnClickListener {
            val position = getAdapterPosition()
            if (position != RecyclerView.NO_POSITION) {
                context.startEditorActivity(note.id!!)
            }
        }
    }

    private fun formatDate(context: Context, date: Date): String {
        val df = android.text.format.DateFormat.getDateFormat(context)
        val tf = android.text.format.DateFormat.getTimeFormat(context)

        return df.format(date.time) + " " + tf.format(date)
    }
}