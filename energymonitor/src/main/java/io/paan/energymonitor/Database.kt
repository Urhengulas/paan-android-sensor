package io.paan.energymonitor

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.annotations.ExperimentalAsymmetricSyncApi
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking
import io.realm.kotlin.mongodb.ext.insert as insert2

class Database(appId: String, token: String) {
    private val app2: App = App.create(AppConfiguration.Builder(appId).build())
    private val user = runBlocking { app2.login(Credentials.apiKey(token)) }
    private val config: SyncConfiguration = SyncConfiguration.create(user, setOf(Energy::class))
    private val realm: Realm = Realm.open(config)

    @OptIn(ExperimentalAsymmetricSyncApi::class)
    suspend fun insert(data: Energy) {
        // set owner_id to current user
        data.apply { this.ownerId = user.id }

        Log.d(TAG, data.toString())

        // send data to realm
        realm.write {
            insert2(data)
        }
    }
}
