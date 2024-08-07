package com.withpeace.withpeace.feature.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.NoTitleDialog
import com.withpeace.withpeace.core.designsystem.ui.TitleBar
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiEvent
import com.withpeace.withpeace.feature.mypage.uistate.ProfileInfoUiModel
import com.withpeace.withpeace.feature.mypage.uistate.ProfileUiState

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit = {},
    onEditProfile: (nickname: String, profileImageUrl: String) -> Unit,
    onLogoutSuccess: () -> Unit,
    onWithdrawSuccess: () -> Unit,
    onAuthExpired: () -> Unit,
    onDibsOfPolicyClick: () -> Unit,
) {
    val profileInfo by viewModel.profileUiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel.myPageUiEvent) {
        viewModel.myPageUiEvent.collect {
            when (it) {
                MyPageUiEvent.UnAuthorizedError -> {
                    onAuthExpired()
                }

                MyPageUiEvent.ResponseError -> {
                    onShowSnackBar("서버 통신 중 오류가 발생했습니다. 다시 시도해주세요.")
                }

                MyPageUiEvent.Logout -> onLogoutSuccess()
                MyPageUiEvent.WithdrawSuccess -> {
                    onShowSnackBar("계정삭제 되었습니다. 14일이내 복구 가능합니다.")
                    onWithdrawSuccess()
                }
            }
        }
    }
    MyPageScreen(
        onEditProfile = {
            onEditProfile(it.nickname, it.profileImage)
        },
        onLogoutClick = {
            viewModel.logout()
        },
        onWithdrawClick = viewModel::withdraw,
        profileInfo = profileInfo,
        onDibsOfPolicyClick = onDibsOfPolicyClick
    )
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onEditProfile: (ProfileInfoUiModel) -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    profileInfo: ProfileUiState,
    onDibsOfPolicyClick: () -> Unit,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(WithpeaceTheme.colors.SystemWhite)) {
        TitleBar(title = stringResource(R.string.my_page))
        when (profileInfo) {
            is ProfileUiState.Success -> {
                MyPageContent(
                    modifier,
                    profileInfo.profileInfoUiModel,
                    onEditProfile,
                    onLogoutClick,
                    onWithdrawClick,
                    onDibsOfPolicyClick = onDibsOfPolicyClick
                )
            }

            is ProfileUiState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                }
            }

            is ProfileUiState.Failure -> {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "네트워크 상태를 확인해주세요.",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }

    }
}

@Composable
private fun MyPageContent(
    modifier: Modifier,
    profileInfo: ProfileInfoUiModel,
    onEditProfile: (ProfileInfoUiModel) -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onDibsOfPolicyClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState)) {
        Column(modifier = modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val imageModifier = modifier
                        .size(54.dp)
                        .border(
                            BorderStroke(0.dp, Color.Transparent),
                            shape = CircleShape,
                        )
                    GlideImage(
                        modifier = imageModifier.clip(CircleShape),
                        imageModel = { profileInfo.profileImage },
                        failure = {
                            Image(
                                painterResource(id = R.drawable.ic_default_profile),
                                modifier = imageModifier,
                                contentDescription = "",
                            )
                        },
                    )
                    Text(
                        style = WithpeaceTheme.typography.body,
                        text = profileInfo.nickname,
                        modifier = modifier.padding(start = 8.dp),
                    )
                }
                TextButton(
                    onClick = { onEditProfile(profileInfo) },
                ) {
                    Text(
                        color = WithpeaceTheme.colors.MainPurple,
                        text = stringResource(R.string.edit_profile),
                        style = WithpeaceTheme.typography.caption,
                    )
                }
            }
        }
        Spacer(modifier = modifier.height(16.dp))
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(WithpeaceTheme.colors.SystemGray3),
        )
        MyPageSections(
            modifier = modifier,
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick,
            email = profileInfo.email,
            onDibsOfPolicyClick = onDibsOfPolicyClick,
        )
    }
}

@Composable
fun MyPageSections(
    modifier: Modifier,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onDibsOfPolicyClick: () -> Unit,
    email: String,
) {
    Column(modifier = modifier) {
        AccountSection(modifier, email = email)
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        FavoriteSection(onDibsOfPolicyClick = onDibsOfPolicyClick)
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        EtcSection(modifier, onLogoutClick = onLogoutClick, onWithdrawClick = onWithdrawClick)
    }
}

@Composable
private fun AccountSection(modifier: Modifier, email: String) {
    Section(
        title = stringResource(R.string.account),
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.connected_account),
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemBlack,
            )
            Text(
                text = email,
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray2,
            )
        }
    }
    Spacer(modifier = modifier.height(16.dp))
}

@Composable
private fun FavoriteSection(
    modifier: Modifier = Modifier,
    onDibsOfPolicyClick: () -> Unit,
) {
    Section(title = stringResource(R.string.interested_list)) {
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = stringResource(R.string.my_dibs_of_policy),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onDibsOfPolicyClick()
                }
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)
                .padding(vertical = 8.dp),
        )
    }
    Spacer(modifier = modifier.height(8.dp))
}

@Composable
private fun EtcSection(
    modifier: Modifier,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    Section(title = stringResource(R.string.etc)) {
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = stringResource(R.string.logout),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onLogoutClick()
                }
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)
                .padding(vertical = 8.dp),
        )
        Text(
            text = stringResource(R.string.withdraw),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    showDialog = true
                }
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)
                .padding(vertical = 8.dp),
        )
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            NoTitleDialog(
                onClickPositive = { showDialog = false },
                onClickNegative = {
                    onWithdrawClick()
                    showDialog = false
                },
                contentText = stringResource(R.string.withdraw_info_content),
                positiveText = stringResource(R.string.withdraw_undo),
                negativeText = stringResource(id = R.string.withdraw),
            )
        }
    }
}

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column(modifier = modifier) {
        Spacer(modifier = modifier.height(16.dp))
        Text(text = title, style = WithpeaceTheme.typography.caption, color = Color(0xFF858585), modifier = modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding))
        content()
    }

}

@Preview(widthDp = 400, heightDp = 900)
@Composable
fun MyPagePreview() {
    WithpeaceTheme {
        MyPageScreen(
            onEditProfile = {
            },
            modifier = Modifier,
            onLogoutClick = {},
            onWithdrawClick = {},
            profileInfo = ProfileUiState.Success(ProfileInfoUiModel("닉네임닉네임", "", "abc@gmail.com")),
            onDibsOfPolicyClick = {}
        )
    }
}