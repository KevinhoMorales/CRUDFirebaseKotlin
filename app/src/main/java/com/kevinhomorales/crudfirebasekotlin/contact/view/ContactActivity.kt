package com.kevinhomorales.crudfirebasekotlin.contact.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.crudfirebasekotlin.R
import com.kevinhomorales.crudfirebasekotlin.contact.viewmodel.ContactViewModel
import com.kevinhomorales.crudfirebasekotlin.contacts.model.Contact
import com.kevinhomorales.crudfirebasekotlin.contacts.viewmodel.ContactsViewModel
import com.kevinhomorales.crudfirebasekotlin.databinding.ActivityContactBinding
import com.kevinhomorales.crudfirebasekotlin.utils.Constants

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding
    lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        if (intent.extras != null) {
            viewModel.contact = intent.extras!!.get(Constants.CONTACT_KEY) as Contact
        }
        binding.nameId.setHint(viewModel.contact.name)
        binding.phoneId.setHint(viewModel.contact.phone)
        setUpActions()
    }

    private fun setUpActions() {
        binding.updateButtonId.setOnClickListener {
            val nameEditText = binding.nameId.text
            val phoneEditText = binding.phoneId.text
            if ((nameEditText.isEmpty()) && (phoneEditText.isEmpty()))  {
                Toast.makeText(applicationContext, getString(R.string.alert_title), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val contactTemp = Contact(viewModel.contact.id, nameEditText.toString(), phoneEditText.toString())
            viewModel.updateContact(contactTemp, this) {
                finish()
            }
        }
        binding.callButtonId.setOnClickListener {
            val number = viewModel.contact.phone
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
        }
    }
}