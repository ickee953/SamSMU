/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.data.model

import android.os.Parcel
import android.os.Parcelable

class Address(
    private val address: String?,
    private val city: String?,
    private val state: String?,
    private val stateCode: String?,
    private val postalCode: String?,
    private val coordinates: Coordinates?,
    private val country: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Coordinates::class.java.classLoader),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(address)
        dest.writeString(city)
        dest.writeString(state)
        dest.writeString(stateCode)
        dest.writeString(postalCode)
        dest.writeParcelable(coordinates, flags)
        dest.writeString(country)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (address != other.address) return false
        if (city != other.city) return false
        if (state != other.state) return false
        if (stateCode != other.stateCode) return false
        if (postalCode != other.postalCode) return false
        if (coordinates != other.coordinates) return false
        if (country != other.country) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address?.hashCode() ?: 0
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (stateCode?.hashCode() ?: 0)
        result = 31 * result + (postalCode?.hashCode() ?: 0)
        result = 31 * result + (coordinates?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "$country, $stateCode, $postalCode, $state, $city, $address."
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }



}