package uz.qmgroup.logiadmin.features.profile

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import uz.qmgroup.logiadmin.BuildConfig
import uz.qmgroup.logiadmin.R

@Composable
fun ProfileDialog(
    onDismissRequested: () -> Unit
) {
    val activityResultRegistryOwner = LocalActivityResultRegistryOwner.current
    Dialog(onDismissRequest = onDismissRequested) {
        OutlinedCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = Firebase.auth.currentUser?.phoneNumber ?: "Loading ...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        Firebase.auth.signOut()

                        val launcher = activityResultRegistryOwner?.activityResultRegistry?.register("profile-sign-in",
                            FirebaseAuthUIActivityResultContract()
                        ) {
                            if (it.resultCode != ComponentActivity.RESULT_OK) {
                                Log.e("LogiAdmin Auth", "Auth failed due ${it.idpResponse?.error}")
                                it.idpResponse?.error?.let { firebaseUiException ->
                                    Firebase.crashlytics.recordException(
                                        firebaseUiException
                                    )
                                }
                            }
                        }

                        val actionCodeSettings = ActionCodeSettings.newBuilder()
                            .setAndroidPackageName(
                                BuildConfig.APPLICATION_ID,
                                true,
                                null
                            )
                            .setHandleCodeInApp(true) // This must be set to true
                            .setUrl("https://ligiadmin.page.link") // This URL needs to be whitelisted
                            .build()

                        val providers = arrayListOf(
                            AuthUI.IdpConfig.EmailBuilder()
                                .enableEmailLinkSignIn()
                                .setActionCodeSettings(actionCodeSettings)
                                .build(),
                            AuthUI.IdpConfig.PhoneBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                        val signInIntent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.mipmap.ic_launcher_foreground)
                            .setTheme(R.style.Theme_LogiAdmin_Auth)
                            .setIsSmartLockEnabled(true)
                            .enableAnonymousUsersAutoUpgrade()
                            .build()

                        launcher?.launch(signInIntent)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(text = "Exit", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}