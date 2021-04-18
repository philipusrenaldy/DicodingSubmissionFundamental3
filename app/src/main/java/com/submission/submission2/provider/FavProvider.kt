package com.submission.submission2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.submission.submission2.db.DBContract.AUTHORITY
import com.submission.submission2.db.DBContract.favColumns.Companion.CONTENT_URI
import com.submission.submission2.db.DBContract.favColumns.Companion.TABLE_NAME
import com.submission.submission2.db.FavHelper

class FavProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val del: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteById(uri.lastPathSegment?.toInt())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return del
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val add: Long = when (FAV){
            sUriMatcher.match(uri) -> favHelper.insertData(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$add")
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)){
            FAV -> favHelper.queryAll()
            FAV_ID -> favHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val alter: Int = when (FAV_ID){
            sUriMatcher.match(uri) -> favHelper.updateData(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return alter
    }

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var favHelper: FavHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }
}