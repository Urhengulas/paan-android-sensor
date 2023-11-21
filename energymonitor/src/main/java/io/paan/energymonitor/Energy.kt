package io.paan.energymonitor

import io.realm.kotlin.types.AsymmetricRealmObject
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId


class Energy() : AsymmetricRealmObject {
    @PrimaryKey
    private var _id: ObjectId = BsonObjectId()
    @PersistedName("owner_id")
    var ownerId: String = ""
    private var sensor: String = "android"
    private var target: String = ""
    private var timestamp: RealmInstant = RealmInstant.MIN
    private var power: Double = 0.0

    constructor(name: String, power: Double, time: Long) : this() {
        this.apply {
            this.power = power
            this.target = name
            this.timestamp = RealmInstant.from(time, 0)
        }
    }

    override fun toString(): String {
        return "Energy { _id: $_id, owner_id: $ownerId, sensor: $sensor, target: $target, timestamp: $timestamp, power: $power }"
    }
}


