package com.kevinhomorales.crudfirebasekotlin.networking

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact

class DataBaseManager {
    companion object {
        val shared = DataBaseManager()
    }
    private val dataBase = Firebase.firestore
    private val PATH = "contacts"

    fun getContacts(context: Context, completion: (MutableList<Contact>) -> Unit) {
        val contacts = mutableListOf<Contact>()
        dataBase.collection(PATH)
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    val id = document.id
                    val name = document.data?.get("name").toString()
                    val phone = document.data?.get("phone").toString()
                    val contact = Contact(id, name, phone)
                    contacts.add(contact)
                }
                completion(contacts)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error -> ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    fun uploadContact(contact: Contact, context: Context, completion: () -> Unit) {
        val contactMap = hashMapOf(
            "id" to contact.id,
            "name" to contact.name,
            "phone" to contact.phone
        )
        dataBase.collection(PATH).document(contact.id).set(contactMap)
            .addOnSuccessListener {
                completion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error -> ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteContact(contact: Contact, context: Context, completion: () -> Unit) {
        dataBase.collection(PATH).document(contact.id).delete()
            .addOnSuccessListener {
                completion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error -> ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateContact(contact: Contact, context: Context, completion: () -> Unit) {
        dataBase.collection(PATH).document(contact.id)
            .update("name", contact.name,
            "phone", contact.phone)
            .addOnSuccessListener {
                completion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error -> ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}