package com.kevinhomorales.crudfirebasekotlin.view.adapter

import com.kevinhomorales.crudfirebasekotlin.model.Contact

interface OnContactClickListener {
    fun onContactClick(contact: Contact)
}