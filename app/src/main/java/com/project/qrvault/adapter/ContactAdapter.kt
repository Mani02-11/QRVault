package com.project.qrvault.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.qrvault.R
import com.project.qrvault.model.ContactModel

class ContactAdapter(
    private var allContacts: List<ContactModel>,
    private val onContactSelected: (ContactModel) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var filteredContacts: MutableList<ContactModel> = allContacts.toMutableList()
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = filteredContacts[position]
        holder.tvContactName.text = contact.name
        holder.tvContactPhone.text = contact.phoneNumber

        if (contact.email.isNotBlank()) {
            holder.tvContactEmail.text = contact.email
            holder.tvContactEmail.visibility = View.VISIBLE
        } else {
            holder.tvContactEmail.visibility = View.GONE
        }

        // Avatar initial
        val initial = if (contact.name.isNotBlank()) contact.name.trim().take(1).uppercase() else "?"
        holder.tvAvatarInitial.text = initial

        // Selection state
        if (contact.isSelected) {
            holder.ivSelectionCheck.visibility = View.VISIBLE
        } else {
            holder.ivSelectionCheck.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            // Single selection toggle
            val prevSelected = selectedPosition
            if (prevSelected != holder.adapterPosition && prevSelected in filteredContacts.indices) {
                filteredContacts[prevSelected].isSelected = false
                notifyItemChanged(prevSelected)
            }

            contact.isSelected = true
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            onContactSelected(contact)
        }
    }

    override fun getItemCount(): Int = filteredContacts.size

    fun updateContacts(newContacts: List<ContactModel>) {
        allContacts = newContacts
        filteredContacts = newContacts.toMutableList()
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val lower = query.lowercase().trim()
        filteredContacts = if (lower.isEmpty()) {
            allContacts.toMutableList()
        } else {
            allContacts.filter {
                it.name.lowercase().contains(lower) || it.phoneNumber.contains(lower)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun getSelectedContact(): ContactModel? {
        return filteredContacts.find { it.isSelected }
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatarInitial: TextView = itemView.findViewById(R.id.tvAvatarInitial)
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tvContactPhone)
        val tvContactEmail: TextView = itemView.findViewById(R.id.tvContactEmail)
        val ivSelectionCheck: ImageView = itemView.findViewById(R.id.ivSelectionCheck)
    }
}
