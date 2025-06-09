/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.samsmu.app.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun favorites(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(user: User)

    @Delete
    suspend fun delete(user: User)

}