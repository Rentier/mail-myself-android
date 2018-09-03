package com.mrklie.mailmyself.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mrklie.mailmyself.R


class NoteAdapter(val context: Context, val notes: List<Note>) : RecyclerView.Adapter<NoteHolder>() {
    override fun getItemId(position: Int): Long {
        val note = notes.get(position)
        return note.id!!
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = notes.get(position)
        holder.bind(note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): NoteHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_notes_list_item, parent, false)
        return NoteHolder(context, view)
    }

    override fun getItemCount(): Int = notes.count()
}