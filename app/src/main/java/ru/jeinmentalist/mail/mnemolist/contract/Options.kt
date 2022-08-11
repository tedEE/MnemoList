package ru.jeinmentalist.mail.mnemolist.contract

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue
import ru.jeinmentalist.mail.domain.type.ITransmitted

@Parcelize
data class Options(
    val volume: @RawValue List<ITransmitted>,
    val openParams: IntArray = intArrayOf()
) : Parcelable