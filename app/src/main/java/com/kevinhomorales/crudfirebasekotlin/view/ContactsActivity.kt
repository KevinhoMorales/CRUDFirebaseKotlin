package com.kevinhomorales.crudfirebasekotlin.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.crudfirebasekotlin.R
import com.kevinhomorales.crudfirebasekotlin.view.adapter.ContactAdapter
import com.kevinhomorales.crudfirebasekotlin.view.adapter.OnContactClickListener
import com.kevinhomorales.crudfirebasekotlin.databinding.ActivityContactsBinding
import com.kevinhomorales.crudfirebasekotlin.model.Contact
import com.kevinhomorales.crudfirebasekotlin.viewmodel.ContactsViewModel

class ContactsActivity : AppCompatActivity(), OnContactClickListener {

    lateinit var contactAdapter: ContactAdapter
    lateinit var binding: ActivityContactsBinding
    lateinit var viewmodel: ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewmodel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        setUpRecyclerView()
        setUpActions()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecyclerView() {
        contactAdapter = ContactAdapter(this, this)
        binding.recyclerViewId.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewId.adapter = contactAdapter
        contactAdapter.setListData(viewmodel.getContacts())
        contactAdapter.notifyDataSetChanged()
    }

    private fun setUpActions() {
        binding.floatingButtonId.setOnClickListener {
            callAlert()
        }
    }

    override fun onContactClick(contact: Contact) {
        callDeleteAlert(contact)
    }

    private fun callDeleteAlert(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(getString(R.string.delete_contact_alert_title))
            setMessage(getString(R.string.delete_contact_alert_message))
            setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                viewmodel.deleteContact(contact)
                dialog.dismiss()}
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun callAlert() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.custom_dialog, null)
        with(builder) {
            setTitle(getString(R.string.alert_title))
            setView(dialogLayout)
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                val nameEditText = dialogLayout.findViewById<EditText>(R.id.name_id).text
                val phoneEditText = dialogLayout.findViewById<EditText>(R.id.phone_id).text
                if ((nameEditText.isEmpty()) || (phoneEditText.isEmpty()))  {
                    Toast.makeText(applicationContext, getString(R.string.alert_title), Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                Toast.makeText(applicationContext, "Datos guardados ${nameEditText}", Toast.LENGTH_SHORT).show()
                val id = java.util.UUID.randomUUID().toString()
                val contact = Contact(id, nameEditText.toString(), phoneEditText.toString())
                viewmodel.uploadContact(contact)
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}