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
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.samsmu.app.core.fragments.SelectableItem

/**
 *
 * @param isFavourite 1 - is favourite, 0 - not in favourite
 * */

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
    @SerializedName("image") @ColumnInfo(name = "image") var image: String?,
    @SerializedName("phone") @ColumnInfo(name = "phone") var phone: String?,
    @SerializedName("address") @ColumnInfo(name = "address") var address: Address?,
    var isFavourite: Int = 0,
    @SerializedName("age") @ColumnInfo(name = "age") var age: Int = -1,
) : Parcelable, SelectableItem {

    constructor(parcel : Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(maidenName)
        dest.writeString(email)
        dest.writeString(image)
        dest.writeString(phone)
        dest.writeParcelable(address, flags)
        dest.writeInt(age)
        dest.writeInt(isFavourite)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun getSelectionId(): Long {
        return id
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
        if (phone != other.phone) return false
        if (address != other.address) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (maidenName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (age.hashCode() ?: 0)

        return result
    }

    override fun toString(): String {
        return "$firstName $lastName $maidenName $email $phone $address $age"
    }

    companion object CREATOR : Parcelable.Creator<User> {

        @TypeConverter
        @JvmStatic
        fun fromAddress( value : Address? ) : String? {
            val type = object: TypeToken<Address>(){}.type
            return Gson().toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAddress( value : String? ) : Address? {
            val type = object: TypeToken<Address>(){}.type
            return Gson().fromJson(value, type)
        }

        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
