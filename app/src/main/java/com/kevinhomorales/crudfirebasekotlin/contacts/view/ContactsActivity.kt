package com.kevinhomorales.crudfirebasekotlin.contacts.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.crudfirebasekotlin.R
import com.kevinhomorales.crudfirebasekotlin.contact.view.ContactActivity
import com.kevinhomorales.crudfirebasekotlin.contacts.view.adapter.ContactAdapter
import com.kevinhomorales.crudfirebasekotlin.contacts.view.adapter.OnContactClickListener
import com.kevinhomorales.crudfirebasekotlin.databinding.ActivityContactsBinding
import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact
import com.kevinhomorales.crudfirebasekotlin.contacts.view.adapter.OnContactDeleteClickListener
import com.kevinhomorales.crudfirebasekotlin.contacts.viewmodel.ContactsViewModel
import com.kevinhomorales.crudfirebasekotlin.utils.Constants
import java.io.Serializable

class ContactsActivity : AppCompatActivity(), OnContactClickListener, OnContactDeleteClickListener {

    lateinit var contactAdapter: ContactAdapter
    lateinit var binding: ActivityContactsBinding
    lateinit var viewModel: ContactsViewModel
    private var alertDialog: AlertDialog? = null

    companion object {
        private const val REQUEST_PHONE_CALL = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        setUpRecyclerView()
        setUpActions()
    }

    private fun setUpRecyclerView() {
        contactAdapter = ContactAdapter(this, this, this)
        binding.recyclerViewId.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewId.adapter = contactAdapter
        getContacts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContacts() {
        callProgressDialog(true)
        viewModel.getContacts(this) { contacts ->
            contactAdapter.setListData(contacts)
            contactAdapter.notifyDataSetChanged()
            callProgressDialog(false)
        }
    }

    private fun setUpActions() {
        binding.floatingButtonId.setOnClickListener {
            callAlert()
        }
    }

    override fun onContactClick(contact: Contact) {
        openContact(contact)
    }

    private fun callDeleteAlert(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(getString(R.string.delete_contact_alert_title))
            setMessage(getString(R.string.delete_contact_alert_message))
            setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                viewModel.deleteContact(contact, applicationContext) {
                    getContacts()
                    dialog.dismiss()
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    @SuppressLint("InflateParams")
    private fun callAlert() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.custom_dialog, null)
        with(builder) {
            setTitle(getString(R.string.alert_title))
            setMessage(getString(R.string.alert_message))
            setView(dialogLayout)
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                val nameEditText = dialogLayout.findViewById<EditText>(R.id.name_id).text
                val phoneEditText = dialogLayout.findViewById<EditText>(R.id.phone_id).text
                if ((nameEditText.isEmpty()) || (phoneEditText.isEmpty()))  {
                    Toast.makeText(applicationContext, getString(R.string.alert_title), Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val id = java.util.UUID.randomUUID().toString()
                val contact = Contact(id, nameEditText.toString(), phoneEditText.toString())
                viewModel.uploadContact(contact, applicationContext) {
                    getContacts()
                    dialog.dismiss()
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun callProgressDialog(show: Boolean) {
        if (show) {
            if (alertDialog == null) {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.progress_dialog, null)
                with(builder) {
                    setTitle(getString(R.string.loading_text))
                    setView(dialogLayout)
                    setCancelable(false)
                    alertDialog = create()
                    alertDialog?.show()
                }
            } else {
                alertDialog?.show()
            }
        } else {
            alertDialog?.dismiss()
            alertDialog = null
        }
    }

    private fun openContact(contact: Contact) {
        viewModel.contactSelected = contact
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            return
        }
        startContactActivity(contact)
    }

    // 2. Handle Permission Request Result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now initiate the call
                val contact = viewModel.contactSelected
                startContactActivity(contact)
            } else {
                Toast.makeText(this, getString(R.string.call_permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startContactActivity(contact: Contact) {
        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra(Constants.CONTACT_KEY, contact as Serializable)
        startActivity(intent)
    }

    override fun onContactDeleteClick(contact: Contact) {
        callDeleteAlert(contact)
    }
}