package com.project.qrvault.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.project.qrvault.model.QRHistoryModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "qrvault.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "qr_history"
        const val COLUMN_ID = "id"
        const val COLUMN_CONTACT_NAME = "contact_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_QR_DATA = "qr_data"
        const val COLUMN_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONTACT_NAME + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_QR_DATA + " TEXT, "
                + COLUMN_CREATED_AT + " TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertQRHistory(history: QRHistoryModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTACT_NAME, history.contactName)
            put(COLUMN_PHONE_NUMBER, history.phoneNumber)
            put(COLUMN_EMAIL, history.email)
            put(COLUMN_QR_DATA, history.qrData)
            put(COLUMN_CREATED_AT, history.createdAt)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllQRHistory(): List<QRHistoryModel> {
        val historyList = mutableListOf<QRHistoryModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NAME)
            val phoneIndex = cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)
            val emailIndex = cursor.getColumnIndexOrThrow(COLUMN_EMAIL)
            val dataIndex = cursor.getColumnIndexOrThrow(COLUMN_QR_DATA)
            val dateIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)

            do {
                val model = QRHistoryModel(
                    id = cursor.getLong(idIndex),
                    contactName = cursor.getString(nameIndex) ?: "",
                    phoneNumber = cursor.getString(phoneIndex) ?: "",
                    email = cursor.getString(emailIndex) ?: "",
                    qrData = cursor.getString(dataIndex) ?: "",
                    createdAt = cursor.getString(dateIndex) ?: ""
                )
                historyList.add(model)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }

    fun getRecentHistory(limit: Int): List<QRHistoryModel> {
        val historyList = mutableListOf<QRHistoryModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT $limit"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NAME)
            val phoneIndex = cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)
            val emailIndex = cursor.getColumnIndexOrThrow(COLUMN_EMAIL)
            val dataIndex = cursor.getColumnIndexOrThrow(COLUMN_QR_DATA)
            val dateIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)

            do {
                val model = QRHistoryModel(
                    id = cursor.getLong(idIndex),
                    contactName = cursor.getString(nameIndex) ?: "",
                    phoneNumber = cursor.getString(phoneIndex) ?: "",
                    email = cursor.getString(emailIndex) ?: "",
                    qrData = cursor.getString(dataIndex) ?: "",
                    createdAt = cursor.getString(dateIndex) ?: ""
                )
                historyList.add(model)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }

    fun getQRHistoryById(id: Long): QRHistoryModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COLUMN_ID=?",
            arrayOf(id.toString()), null, null, null
        )

        var model: QRHistoryModel? = null
        if (cursor != null && cursor.moveToFirst()) {
            model = QRHistoryModel(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                contactName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NAME)) ?: "",
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)) ?: "",
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)) ?: "",
                qrData = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QR_DATA)) ?: "",
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)) ?: ""
            )
            cursor.close()
        }
        db.close()
        return model
    }

    fun getHistoryCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }

    fun deleteQRHistory(id: Long): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun clearAllHistory(): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_NAME, null, null)
        db.close()
        return rows
    }
}
