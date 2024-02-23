import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.withpeace.google_login.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GoogleLogin {
    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .build()

    private val request: GetCredentialRequest =
        GetCredentialRequest.Builder().addCredentialOption(
            googleIdOption
        ).build()

    fun googleLogin(
        coroutineScope: CoroutineScope,
        context: Context,
        onSuccess: (String) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        coroutineScope.launch {
            runCatching {
                val result = credentialManager.getCredential(
                    context,
                    request
                )
                handleSignIn(result, onSuccess)
            }.onFailure {
                Log.d("test", it.stackTraceToString())
            }
        }

    }

    private fun handleSignIn(result: GetCredentialResponse, onSuccess: (String) -> Unit) {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)
                onSuccess(googleIdTokenCredential.idToken)
            } catch (e: Exception) {
            }
        } else {
            Log.d("test","test3")
            // Catch any unrecognized custom credential type here.
//                    Log.e(TAG, "Unexpected type of credential")
        }
    }
}