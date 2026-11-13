package com.project.qrvault.model

import java.io.Serializable

data class QRHistoryModel(
    val id: Long = 0,
    val contactName: String,
    val phoneNumber: String,
    val email: String,
    val qrData: String,
    val createdAt: String
) : Serializable
