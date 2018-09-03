package com.mrklie.mailmyself.list

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "Note")
data class Note(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name = "text") var text: String,
                @ColumnInfo(name = "date") var date: Date,
                @ColumnInfo(name = "sent") var sent: Boolean) {

    @Ignore
    constructor(text: String):this(null,text, Date(),false)
}

