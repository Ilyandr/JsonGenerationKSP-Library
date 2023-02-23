package com.example.kspexample.repository.data

import com.example.annotation.source.JsonGenerated


// 1
@JsonGenerated
private fun Sup(
    id: Int?,
    avail: String?,
    description: String?,
    manuf: String?,
    scladId: Int?,
    sclad: String?,
    deliveryPercent: Int?,
    scladUpdated: String?,
    percent: Int?,
    time: String?,
    deliv: String?,
    delivHours: String?,
    price: Int?,
    count: String?,
    checkIcon: String?,
    scladRefundPercent: Int?,
    prepayment: Float = 1f,
    express: Int?,
    expressCityOp: ExpressCityOp?,
    group: Group?,
    isBeingUsed: Int?,
    scladInfo: String?
) = Unit

data class ExpressCityOp(
    var id: String?,
    var address: String?
)

data class Group(
    var id: Int?,
    var manufacture: String,
    var code: String,
    var totalItemsCount: Int,
    var description: String,
    var showMoreCond: Boolean,
    var price: String,
    var original: Boolean,
    var popularity: Float,
    var lastVisibleItemPosition: Int = 0,
    var isMoreLoaded: Boolean = false
)


// 2
@JsonGenerated
data class UserEntity(
    var id: String? = null,
    var abcNumber: String? = null,
    var phoneNumber: String? = null,
    var login: String? = null,
    var fam: String? = null,
    var name: String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var cityName: String? = null,
    var status: String? = null,
    var payMethod: String? = null,
    var type: String? = null,
    var address: String? = null,
    var phoneChecked: Boolean? = null,
    var emailChecked: Boolean? = null,
    var outputPlace: List<OutputPlaceEntity>? = null,
    var notificationsSettings: SettingsNotificationEntity? = null,
    var notificationsSettingsMap: Map<Int, SettingsNotificationEntity> = mapOf(),
)

data class OutputPlaceEntity(
    val id: String = "0",
    val name: String = "",
    val address: String = "",
    val x: Double?,
    val y: Double?
)

data class SettingsNotificationEntity(
    var storeNotification: Boolean?,
    var smsNotification: Boolean?,
    var notAvailableNotification: Boolean?,
    var subscribeNotification: Boolean?
)