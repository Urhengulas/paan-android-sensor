package io.paan.energymonitor

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId


class Energy() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var owner_id: String = ""
    var sensor: String = "android"
    var target: String = ""
    var timestamp: RealmInstant = RealmInstant.MIN
    var power: Double = 0.0

    constructor(name: String, power: Double, time: Long) : this() {
        this.apply {
            this.power = power
            this.target = name
            this.timestamp = RealmInstant.from(time, 0)
        }
    }

    override fun toString(): String {
        return "Energy { _id: $_id, owner_id: $owner_id, sensor: $sensor, target: $target, timestamp: $timestamp, power: $power }"
    }
}


