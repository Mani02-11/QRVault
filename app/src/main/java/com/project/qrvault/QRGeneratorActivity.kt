package com.project.qrvault

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.project.qrvault.database.DatabaseHelper
import com.project.qrvault.model.ContactModel
import com.project.qrvault.model.QRHistoryModel
import com.project.qrvault.utils.ImageUtils
import com.project.qrvault.utils.QRCodeUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QRGeneratorActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var contact: ContactModel? = null
    private var currentQrBitmap: Bitmap? = null
    private var vCardData: String = ""

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvNameBelowQr: TextView
    private lateinit var ivQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_generator)

        val root = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        contact = intent.getSerializableExtra("SELECTED_CONTACT") as? ContactModel

        if (contact == null) {
            Toast.makeText(this, "No contact selected", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<ImageButton>(R.id.btnGenBack).setOnClickListener { finish() }

        tvName = findViewById(R.id.tvGenName)
        tvPhone = findViewById(R.id.tvGenPhone)
        tvEmail = findViewById(R.id.tvGenEmail)
        tvNameBelowQr = findViewById(R.id.tvGenQrNameBelow)
        ivQr = findViewById(R.id.ivGeneratedQr)

        tvName.text = contact!!.name
        tvPhone.text = contact!!.phoneNumber
        tvEmail.text = contact!!.email.ifEmpty { "No email provided" }
        tvNameBelowQr.text = contact!!.name

        generateAndSaveQr()

        findViewById<MaterialButton>(R.id.btnSaveGallery).setOnClickListener {
            saveQrToGallery()
        }

        findViewById<MaterialButton>(R.id.btnShareQr).setOnClickListener {
            shareQr()
        }

        findViewById<MaterialButton>(R.id.btnRegenerateQr).setOnClickListener {
            generateAndSaveQr()
            Toast.makeText(this, "QR Code Regenerated!", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.btnBackToContacts).setOnClickListener {
            finish()
        }
    }

    private fun generateAndSaveQr() {
        val name = contact?.name ?: "Unknown"
        val phone = contact?.phoneNumber ?: ""
        val email = contact?.email ?: ""

        vCardData = QRCodeUtils.generateVCardData(name, phone, email)
        currentQrBitmap = QRCodeUtils.generateQRCodeBitmap(vCardData, 600, 600)

        if (currentQrBitmap != null) {
            ivQr.setImageBitmap(currentQrBitmap)

            val dateFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(Date())

            val historyModel = QRHistoryModel(
                contactName = name,
                phoneNumber = phone,
                email = email,
                qrData = vCardData,
                createdAt = formattedDate
            )
            dbHelper.insertQRHistory(historyModel)
        } else {
            Toast.makeText(this, R.string.error_generating_qr, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveQrToGallery() {
        val bitmap = currentQrBitmap ?: return
        val fileName = contact?.name?.replace(" ", "_") ?: "contact"

        val uri = ImageUtils.saveBitmapToGallery(this, bitmap, fileName)
        if (uri != null) {
            showSuccessDialog()
        } else {
            Toast.makeText(this, R.string.error_saving_qr, Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareQr() {
        val bitmap = currentQrBitmap ?: return
        val name = contact?.name ?: "Contact"
        ImageUtils.shareBitmap(this, bitmap, name)
    }

    private fun showSuccessDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_qr_saved, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<MaterialButton>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
