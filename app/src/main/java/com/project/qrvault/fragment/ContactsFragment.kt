package com.project.qrvault.fragment

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.project.qrvault.QRGeneratorActivity
import com.project.qrvault.R
import com.project.qrvault.adapter.ContactAdapter
import com.project.qrvault.model.ContactModel
import com.project.qrvault.utils.PermissionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsFragment : Fragment() {

    private lateinit var rvContacts: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnGenerateQr: MaterialButton
    private lateinit var layoutPermission: LinearLayout
    private lateinit var btnGrantPermission: MaterialButton
    private lateinit var tvEmptyContacts: TextView

    private var contactAdapter: ContactAdapter? = null
    private val contactList = mutableListOf<ContactModel>()
    private var selectedContact: ContactModel? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            layoutPermission.visibility = View.GONE
            loadDeviceContacts()
        } else {
            layoutPermission.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        rvContacts = view.findViewById(R.id.rvContacts)
        etSearch = view.findViewById(R.id.etSearchContacts)
        btnGenerateQr = view.findViewById(R.id.btnGenerateQr)
        layoutPermission = view.findViewById(R.id.layoutPermission)
        btnGrantPermission = view.findViewById(R.id.btnGrantPermission)
        tvEmptyContacts = view.findViewById(R.id.tvEmptyContacts)

        rvContacts.layoutManager = LinearLayoutManager(requireContext())

        btnGrantPermission.setOnClickListener {
            requestPermissionLauncher.launch(PermissionUtils.READ_CONTACTS_PERMISSION)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter?.filter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnGenerateQr.setOnClickListener {
            val contact = selectedContact ?: contactAdapter?.getSelectedContact()
            if (contact != null) {
                val intent = Intent(requireContext(), QRGeneratorActivity::class.java).apply {
                    putExtra("SELECTED_CONTACT", contact)
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), R.string.select_a_contact_prompt, Toast.LENGTH_SHORT).show()
            }
        }

        checkPermissionAndLoad()

        return view
    }

    private fun checkPermissionAndLoad() {
        if (PermissionUtils.hasReadContactsPermission(requireContext())) {
            layoutPermission.visibility = View.GONE
            loadDeviceContacts()
        } else {
            layoutPermission.visibility = View.VISIBLE
            rvContacts.visibility = View.GONE
        }
    }

    private fun loadDeviceContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = fetchContactsFromContentProvider()
            withContext(Dispatchers.Main) {
                contactList.clear()
                contactList.addAll(contacts)

                if (contactList.isEmpty()) {
                    tvEmptyContacts.visibility = View.VISIBLE
                    rvContacts.visibility = View.GONE
                } else {
                    tvEmptyContacts.visibility = View.GONE
                    rvContacts.visibility = View.VISIBLE

                    contactAdapter = ContactAdapter(contactList) { selected ->
                        selectedContact = selected
                        btnGenerateQr.text = getString(R.string.generate_qr_code_count_btn, 1)
                    }
                    rvContacts.adapter = contactAdapter
                }
            }
        }
    }

    private fun fetchContactsFromContentProvider(): List<ContactModel> {
        val list = mutableListOf<ContactModel>()
        val contentResolver = requireContext().contentResolver

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val addedIds = mutableSetOf<String>()

            do {
                val contactId = cursor.getString(idIndex) ?: continue
                if (addedIds.contains(contactId)) continue
                addedIds.add(contactId)

                val name = cursor.getString(nameIndex) ?: "Unknown"
                val number = cursor.getString(numberIndex) ?: ""
                val email = fetchEmailForContact(contactId)

                list.add(ContactModel(contactId, name, number, email))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return list
    }

    private fun fetchEmailForContact(contactId: String): String {
        var email = ""
        try {
            val emailCursor: Cursor? = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                arrayOf(contactId),
                null
            )
            if (emailCursor != null && emailCursor.moveToFirst()) {
                val emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                if (emailIndex >= 0) {
                    email = emailCursor.getString(emailIndex) ?: ""
                }
                emailCursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return email
    }
}
