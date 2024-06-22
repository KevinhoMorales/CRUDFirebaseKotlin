package com.kevinhomorales.crudfirebasekotlin.contacts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact
import com.kevinhomorales.crudfirebasekotlin.networking.DataBaseManager

class ContactsViewModel: ViewModel() {

    lateinit var contactSelected: Contact

    fun getContacts(context: Context, completion: (MutableList<Contact>) -> Unit) {
        return DataBaseManager.shared.getContacts(context, completion)
    }

    fun uploadContact(contact: Contact, context: Context, completion: () -> Unit) {
        DataBaseManager.shared.uploadContact(contact, context, completion)
    }

    fun deleteContact(contact: Contact, context: Context, completion: () -> Unit) {
        DataBaseManager.shared.deleteContact(contact, context, completion)
    }

}