/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.samsmu.app.data.model.User

@Database(
    version = 1,
    entities = [User::class]
)
abstract class SamSmuDB : RoomDatabase() {

    companion object {
        private var INSTANCE: SamSmuDB? = null
        private const val DB_NAME = "samsmu_db.db"

        fun getDatabase( context: Context): SamSmuDB {
            if( INSTANCE == null ){
                synchronized( SamSmuDB::class.java ){
                    if( INSTANCE == null ){
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SamSmuDB::class.java, DB_NAME
                        ).build()
                    }
                }
            }

            return INSTANCE!!
        }
    }

}