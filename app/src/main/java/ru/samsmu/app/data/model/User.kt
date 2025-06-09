/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "user",
    indices = [Index(value = ["id"], unique = true)]
)
data class User(
    @SerializedName("id") @PrimaryKey @ColumnInfo(name = "id") var id: Long,
    @SerializedName("firstName") @ColumnInfo(name = "firstName") var firstName: String?,
    @SerializedName("lastName") @ColumnInfo(name = "lastName") var lastName: String?,
    @SerializedName("maidenName") @ColumnInfo(name = "maidenName") var maidenName: String?,
    @SerializedName("email") @ColumnInfo(name = "email") var email: String?,
    @SerializedName("image") @ColumnInfo(name = "image") var image: String?
) : Parcelable {

    constructor(parcel : Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(maidenName)
        dest.writeString(email)
        dest.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (maidenName != other.maidenName) return false
        if (email != other.email) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (maidenName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)

        return result
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
