package com.kevinhomorales.crudfirebasekotlin.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.crudfirebasekotlin.databinding.RowContactBinding
import com.kevinhomorales.crudfirebasekotlin.model.Contact

class ContactAdapter(private val context: Context, var itemClickListener: OnContactClickListener): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private lateinit var binding: RowContactBinding
    private var dataList = mutableListOf<Contact>()

    fun setListData(data: MutableList<Contact>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        binding = RowContactBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val list = dataList[position]
        return holder.bind(list)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    inner class ContactViewHolder(private val itemBinding: RowContactBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(contact: Contact) {
            itemBinding.nameId.setText(contact.name)
            itemBinding.phoneId.setText(contact.phone)
            itemView.setOnClickListener { itemClickListener.onContactClick(contact) }
        }
    }
}