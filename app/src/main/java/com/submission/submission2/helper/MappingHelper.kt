package com.submission.submission2.helper

import android.database.Cursor
import com.submission.submission2.DataUser
import com.submission.submission2.db.DBContract

object MappingHelper {

    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<DataUser>{
        val favList = ArrayList<DataUser>()

        favCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow((DBContract.favColumns.ID)))
                val username = getString(getColumnIndexOrThrow(DBContract.favColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DBContract.favColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DBContract.favColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DBContract.favColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DBContract.favColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DBContract.favColumns.REPOSITORY))
                favList.add(
                    DataUser(
                        id,
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository
                    )
                )
            }
        }
        return favList
    }
}