package com.project.qrvault.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.project.qrvault.R
import com.project.qrvault.database.DatabaseHelper

class SettingsFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var switchTheme: MaterialSwitch
    private lateinit var tvThemeSubtext: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        dbHelper = DatabaseHelper(requireContext())

        switchTheme = view.findViewById(R.id.switchTheme)
        tvThemeSubtext = view.findViewById(R.id.tvThemeSubtext)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        switchTheme.isChecked = isNightMode
        tvThemeSubtext.text = if (isNightMode) getString(R.string.dark_mode) else getString(R.string.light_mode)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        view.findViewById<LinearLayout>(R.id.itemAbout).setOnClickListener {
            showInfoDialog(
                title = getString(R.string.about_qrvault),
                message = "QRVault is a premium contact sharing application. Effortlessly select contacts from your phone and convert them into standard vCard QR codes for quick scanning and saving."
            )
        }

        view.findViewById<LinearLayout>(R.id.itemClearHistory).setOnClickListener {
            confirmClearHistory()
        }

        view.findViewById<LinearLayout>(R.id.itemPrivacy).setOnClickListener {
            showInfoDialog(
                title = getString(R.string.privacy_policy),
                message = getString(R.string.privacy_desc)
            )
        }

        view.findViewById<LinearLayout>(R.id.itemAppInfo).setOnClickListener {
            showInfoDialog(
                title = getString(R.string.app_information),
                message = "Application: QRVault\nVersion: 1.0.0\nDatabase: SQLite (qrvault.db)\nEngine: ZXing Core QR Generator\nArchitecture: Clean Modular Kotlin"
            )
        }

        return view
    }

    private fun confirmClearHistory() {
        val count = dbHelper.getHistoryCount()
        if (count == 0) {
            Toast.makeText(requireContext(), "History is already empty", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.clear_history_confirm_title)
            .setMessage(R.string.clear_history_confirm_msg)
            .setPositiveButton(R.string.clear) { _, _ ->
                dbHelper.clearAllHistory()
                Toast.makeText(requireContext(), "All QR history cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showInfoDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
