package com.kevinhomorales.crudfirebasekotlin.contact.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact
import com.kevinhomorales.crudfirebasekotlin.networking.DataBaseManager

class ContactViewModel: ViewModel() {
    lateinit var contact: Contact

    fun updateContact(contactEdit: Contact, context: Context, completion: () -> Unit) {
        val contactTemp = contactEdit
        if (contactEdit.name.isEmpty()) {
            contactTemp.name = contact.name
        }
        if (contactTemp.phone.isEmpty()) {
            contactTemp.phone = contact.phone
        }
        DataBaseManager.shared.updateContact(contactTemp, context, completion)
    }
}