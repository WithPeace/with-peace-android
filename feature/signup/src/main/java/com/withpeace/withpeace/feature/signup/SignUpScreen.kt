package com.withpeace.withpeace.feature.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.permission.ImagePermissionHelper

/*
 TODO("resource:app_logo, string 등 한 곳으로 통일")
 */
@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit,
) {
    SignUpScreen(nickname = viewModel.nickname, viewModel::onNicknameChanged)
}

@Composable
fun SignUpScreen(
    nickname: String = "",
    onNickNameChanged: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(104.dp))
            Text(
                text = stringResource(
                    R.string.create_profile,
                ),
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.title1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.nickname_policy),
                color = WithpeaceTheme.colors.SystemGray1,
                style = WithpeaceTheme.typography.caption,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
            NickNameTextField(
                nickname = nickname,
                onNickNameChanged = onNickNameChanged,
            )
            Spacer(modifier = Modifier.height(72.dp))
        }
        SignUpButton(onClick = onSignUpClick)
    }
}
@Composable
private fun ProfileImage(
    profileImage: String?,
    modifier: Modifier,
    onNavigateToGallery: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePermissionHelper = remember { ImagePermissionHelper(context) }
    val launcher = imagePermissionHelper.getImageLauncher(
        onPermissionGranted = onNavigateToGallery,
        onPermissionDenied = { showDialog = true },
    )
    if (showDialog) {
        imagePermissionHelper.ImagePermissionDialog { showDialog = false }
    }
    val imageModifier = modifier
        .size(120.dp)
        .clip(CircleShape)
    Row(
        modifier = modifier.wrapContentSize(Alignment.Center),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier.clickable {
                imagePermissionHelper.onCheckSelfImagePermission(
                    onPermissionGranted = onNavigateToGallery,
                    onPermissionDenied = {
                        imagePermissionHelper.requestPermissionDialog(launcher)
                    },
                )
            },
        ) {
            // GlideImage(
            //     modifier = imageModifier,
            //     imageModel = { profileImage },
            //     failure = {
            //         Image(
            //             painterResource(id = R.drawable.ic_default_profile),
            //             modifier = imageModifier,
            //             contentDescription = "",
            //         )
            //     },
            // )
            // Image(
            //     modifier = modifier
            //         .align(Alignment.BottomEnd)
            //         .padding(bottom = 6.dp, end = 6.dp),
            //     painter = painterResource(id = R.drawable.ic_editor_pencil),
            //     contentDescription = stringResource(id = R.string.edit_profile),
            // )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NickNameTextField(
    modifier: Modifier = Modifier,
    nickname: String,
    onNickNameChanged: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = nickname, onValueChange = onNickNameChanged,
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            textStyle = WithpeaceTheme.typography.body.copy(textAlign = TextAlign.Center),
            singleLine = true,
            maxLines = 1,
        ) {
            TextFieldDefaults.DecorationBox(
                value = nickname,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.nickname_hint),
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
        Divider(
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .width(140.dp)
                .height(1.dp),
        )
    }
}

@Composable
private fun SignUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(
                bottom = 40.dp,
                end = WithpeaceTheme.padding.BasicHorizontalPadding,
                start = WithpeaceTheme.padding.BasicHorizontalPadding,
            )
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.SystemGray2),
        shape = RoundedCornerShape(9.dp),
    ) {
        Text(
            style = WithpeaceTheme.typography.body,
            text = "가입 완료",
            modifier = Modifier.padding(vertical = 18.dp),
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Preview(widthDp = 400, heightDp = 900, showBackground = true)
@Composable
fun SignUpPreview() {
    SignUpScreen()
}