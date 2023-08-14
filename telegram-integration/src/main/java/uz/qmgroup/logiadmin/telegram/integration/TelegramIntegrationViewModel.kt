package uz.qmgroup.logiadmin.telegram.integration

import android.util.Log
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class TelegramIntegrationViewModel {
    fun authorize() {
        val client = Client.create({ updatePayload ->
            Log.d("TelegramIntegrationViewModel", "authorize: Update received: $updatePayload")
        }, { errorPayload ->
            Log.d("TelegramIntegrationViewModel", "authorize: update Error received: $errorPayload")
        }, { updatePayload ->
            Log.d("TelegramIntegrationViewModel", "authorize: Error received: $updatePayload")
        })

        client.send(TdApi.GetMe()) {
            Log.d("TelegramIntegrationViewModel", "get Me: $it")
        }
    }
}