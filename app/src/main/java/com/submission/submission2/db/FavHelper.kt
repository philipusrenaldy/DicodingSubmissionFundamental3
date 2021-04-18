package com.submission.submission2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.submission.submission2.db.DBContract.favColumns.Companion.ID
import com.submission.submission2.db.DBContract.favColumns.Companion.TABLE_NAME
import com.submission.submission2.db.DBContract.favColumns.Companion.USERNAME
import kotlin.jvm.Throws

class FavHelper(context: Context) {

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavHelper? = null
        private lateinit var dataBaseHelper: DBHelper
        private lateinit var database: SQLiteDatabase
        fun getInstance(context: Context): FavHelper =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: FavHelper(context)
                }
    }

    init {
        dataBaseHelper = DBHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll():Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC"
        )
    }

    fun queryByUsername(username: String?):Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(username),
                null,
                null,
                null,
                null
        )
    }

    fun insertData(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateData(username: String?, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(username))
    }

    fun deleteById(id: Int?): Int{
        return database.delete(DATABASE_TABLE, "$ID = '$id'", null)
    }
}