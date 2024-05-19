package com.example.tradingjournalpruebafinal.Data

import android.os.Parcel
import android.os.Parcelable

data class Transaction(
    val id: Int,
    val instrument: String,
    val day: String,
    val month: String,
    val year: String,
    val strategy: String,
    val entryPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val risk: Double,
    val win_loss: String,
    val percentage: Double,
    val emotion: String,
    val session: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(instrument)
        parcel.writeString(day)
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeString(strategy)
        parcel.writeDouble(entryPrice)
        parcel.writeDouble(stopLoss)
        parcel.writeDouble(takeProfit)
        parcel.writeDouble(risk)
        parcel.writeString(win_loss)
        parcel.writeDouble(percentage)
        parcel.writeString(emotion)
        parcel.writeString(session)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}
