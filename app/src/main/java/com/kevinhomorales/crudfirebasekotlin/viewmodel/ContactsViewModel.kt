package com.kevinhomorales.crudfirebasekotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.crudfirebasekotlin.model.Contact

class ContactsViewModel: ViewModel() {

    fun getContacts(): MutableList<Contact> {
        return mutableListOf(Contact("1", "Pedro", "123456789"), Contact("2", "María", "987654321"))
    }

    fun uploadContact(contact: Contact) {

    }

    fun deleteContact(contact: Contact) {

    }

}