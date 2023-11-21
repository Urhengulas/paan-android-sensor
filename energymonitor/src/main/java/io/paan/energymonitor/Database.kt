package io.paan.energymonitor

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking

class Database(appId: String, token: String) {
    private val app2: App = App.create(AppConfiguration.Builder(appId).build())
    private val user = runBlocking { app2.login(Credentials.apiKey(token)) }
    private val config: SyncConfiguration = initConfig(user)
    private val realm: Realm = Realm.open(config)

    suspend fun insert(data: Energy) {
        // set owner_id to current user
        data.apply { this.owner_id = user.id }

        Log.d(TAG, data.toString())

        // send data to realm
        realm.write {
            copyToRealm(data, UpdatePolicy.ERROR)
        }
    }
}

fun initConfig(user: User): SyncConfiguration {
    return SyncConfiguration
        .Builder(user, setOf(Energy::class))
        .initialSubscriptions { realm ->
            add(
                // TODO: only subscribe to N most recent elements
                realm.query<Energy>("owner_id == $0", user.id),
                "energy_subscription",
            )
        }
        .build()
}