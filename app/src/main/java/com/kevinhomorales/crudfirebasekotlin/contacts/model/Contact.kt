package com.kevinhomorales.crudfirebasekotlin.contacts.model

import java.io.Serializable

data class Contact (
    val id: String,
    var name: String,
    var phone: String
): Serializable