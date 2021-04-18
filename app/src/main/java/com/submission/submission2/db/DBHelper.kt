package com.submission.submission2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.submission.submission2.db.DBContract.favColumns.Companion.TABLE_NAME

internal class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbgithubfav"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DBContract.favColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DBContract.favColumns.USERNAME} TEXT NOT NULL," +
                "${DBContract.favColumns.NAME} TEXT NOT NULL," +
                "${DBContract.favColumns.AVATAR} TEXT NOT NULL," +
                "${DBContract.favColumns.COMPANY} TEXT NOT NULL," +
                "${DBContract.favColumns.LOCATION} TEXT NOT NULL," +
                "${DBContract.favColumns.REPOSITORY} TEXT NOT NULL)"
    }
}