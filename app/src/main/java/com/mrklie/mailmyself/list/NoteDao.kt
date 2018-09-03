package com.mrklie.mailmyself.list

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface NoteDao {
    @Query("SELECT * from Note WHERE id = (:id)")
    fun findById(id: Long): Single<Note>

    @Query("SELECT * from Note ORDER BY date DESC")
    fun findAll(): Flowable<List<Note>>

    @Query("SELECT * from Note WHERE NOT(sent) ORDER BY date DESC")
    fun findAllUnsent(): Flowable<List<Note>>

    @Query("SELECT * from Note WHERE NOT(sent) AND length(text) > 0 ORDER BY date ASC")
    fun findAllUnsentAndNonEmpty(): Single<List<Note>>

    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)
}