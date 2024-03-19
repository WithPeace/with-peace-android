package com.withpeace.withpeace.core.designsystem.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val mainpink = Color(0xFFFEA0A1)
val subPink = Color(0xFFF5D6DB)
val subApricot = Color(0xFFFED9C9)
val subBlue1 = Color(0xFFDFF2F9)
val subBlue2 = Color(0xFF0575E6)

val systemBlack = Color(0xFF212529)
val systemWhite = Color.White
val systemGray1 = Color(0xFF3D3D3D)
val systemGray2 = Color(0xFFA7A7A7)
val systemGray3 = Color(0xFFECECEF)
val systemGray4 = Color(0xFF696969)
val systemError = Color(0xFFF0474B)
val systemSuccess = Color(0xFF3BD569)

data class WithPeaceColor(
    val MainPink: Color = mainpink,
    val SubPink: Color = subPink,
    val SubApricot: Color = subApricot,
    val SubBlue1: Color = subBlue1,
    val SubBlue2:Color = subBlue2,
    val SystemBlack: Color = systemBlack,
    val SystemWhite: Color = systemWhite,
    val SystemGray1: Color = systemGray1,
    val SystemGray2: Color = systemGray2,
    val SystemGray3: Color = systemGray3,
    val SystemError: Color = systemError,
    val SystemGray4: Color = systemGray4,
    val SystemSuccess: Color = systemSuccess,
)

val lightColor = WithPeaceColor()
val darkColor = WithPeaceColor()
