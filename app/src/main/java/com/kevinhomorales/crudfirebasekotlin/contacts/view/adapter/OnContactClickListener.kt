package com.kevinhomorales.crudfirebasekotlin.contacts.view.adapter

import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact

interface OnContactClickListener {
    fun onContactClick(contact: Contact)
}