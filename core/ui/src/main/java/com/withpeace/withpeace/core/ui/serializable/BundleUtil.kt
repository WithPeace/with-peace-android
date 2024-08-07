package com.withpeace.withpeace.core.ui.serializable

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.extraNotNullSerializable(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getSerializable(name, T::class.java))
} else {
    getSerializable(name) as T
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.extraNotNullParcelable(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getParcelable(name, T::class.java))
} else {
    requireNotNull(getParcelable(name)) as T
}