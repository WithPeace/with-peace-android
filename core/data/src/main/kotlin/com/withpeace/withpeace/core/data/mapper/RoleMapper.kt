package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.role.Role

internal fun String.roleToDomain(): Role {
    return when (this) {
        "GUEST" -> Role.GUEST
        "USER" -> Role.USER
        else -> Role.UNKNOWN
    }
}