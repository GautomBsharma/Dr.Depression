package com.mksolution.depressionreducer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mksolution.depressionreducer.Model.Note

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbnotes"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_NOTE = "note"
        private const val COL_CATEGORY = "category"
        private const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY, $COL_TITLE TEXT, $COL_NOTE TEXT, $COL_CATEGORY TEXT, $COL_DATE TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertN(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, note.title)
            put(COL_NOTE, note.description)
            put(COL_CATEGORY, note.category)
            put(COL_DATE, note.date)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, note.title)
            put(COL_NOTE, note.description)
            put(COL_CATEGORY, note.category)
            put(COL_DATE, note.date)
        }
        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
                val note = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                notesList.add(Note(id, title, note, category, date))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }
}