package uz.qmgroup.logiadmin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uz.qmgroup.logiadmin.features.app.AppScreen
import uz.qmgroup.logiadmin.features.app.di.appDIModule
import uz.qmgroup.logiadmin.features.shipments.di.shipmentDIModule
import uz.qmgroup.logiadmin.features.startshipment.di.startShipmentDIModule
import uz.qmgroup.logiadmin.features.transports.di.transportsDIModule
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(BuildConfig.Crashlitics)

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        startKoin {
            androidContext(applicationContext)

            modules(shipmentDIModule, appDIModule, transportsDIModule, startShipmentDIModule)
        }

        if (Firebase.auth.currentUser == null) {
            val launcher = registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) {
                if (it.resultCode != RESULT_OK) {
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

            launcher.launch(signInIntent)
        }

        setContent {
            LogiAdminTheme {
                AppScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                )
            }
        }
    }
}