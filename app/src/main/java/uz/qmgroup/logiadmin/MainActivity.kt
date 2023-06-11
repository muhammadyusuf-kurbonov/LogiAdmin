package uz.qmgroup.logiadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uz.qmgroup.logiadmin.features.app.AppScreen
import uz.qmgroup.logiadmin.features.app.di.appDIModule
import uz.qmgroup.logiadmin.features.repositories.AppRepository
import uz.qmgroup.logiadmin.features.repositories.repositoriesDI
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

            modules(
                shipmentDIModule,
                appDIModule,
                transportsDIModule,
                startShipmentDIModule,
                repositoriesDI
            )
        }

        val repository by inject<AppRepository>()

        lifecycleScope.launch {
            repository.initializeAndSynchronize()
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