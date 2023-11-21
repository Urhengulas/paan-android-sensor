package io.paan.android_sensor

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.annotations.ExperimentalAsymmetricSyncApi
import io.realm.kotlin.mongodb.exceptions.ConnectionException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking
import io.realm.kotlin.mongodb.ext.insert as insert2

class Database(appId: String, token: String) {
    private val app: App = App.create(AppConfiguration.create(appId))

    // WIP: log out for testing, so the user is not cached
    private val user2 = runBlocking { getUser(app, token) }
    init {
        runBlocking { user2.logOut() }
    }

    private val user = runBlocking { getUser(app, token) }
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

suspend fun getUser(app: App, token: String): User {
    return if (app.currentUser != null) {
        Log.d(TAG, "Found cached user")
        app.currentUser!!
    } else {
        Log.d(TAG, "No cached user. Try to log in.")
        try {
            app.login(Credentials.apiKey(token))
        } catch (e: ConnectionException) {
            // TODO: retry connecting, requires constructor to not block
            throw e
        } catch (e: Exception) {
            throw e
        }
    }
}
