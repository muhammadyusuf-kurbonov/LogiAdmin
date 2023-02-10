package uz.qmgroup.logiadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uz.qmgroup.logiadmin.features.app.AppScreen
import uz.qmgroup.logiadmin.features.app.di.appDIModule
import uz.qmgroup.logiadmin.features.shipments.di.shipmentDIModule
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        startKoin {
            androidContext(applicationContext)

            modules(shipmentDIModule, appDIModule)
        }

        setContent {
            LogiAdminTheme {
                AppScreen(
                    modifier = Modifier.fillMaxSize().imePadding()
                )
            }
        }
    }
}