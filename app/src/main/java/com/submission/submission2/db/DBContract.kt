package com.submission.submission2.db

import android.net.Uri
import android.provider.BaseColumns

object DBContract {

    const val AUTHORITY = "com.submission.submission2"
    const val SCHEME = "content"

    class favColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val ID = "id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}