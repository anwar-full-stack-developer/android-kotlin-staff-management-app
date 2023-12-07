package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.data.EmployeeData

import com.example.myapplication.placeholder.PlaceholderContent.PlaceholderItem
import com.example.myapplication.databinding.EmployeeListItemBinding
import com.example.myapplication.placeholder.PlaceholderContent
import java.util.ArrayList

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class EmployeeRecyclerViewAdapter(
    private var values: ArrayList<EmployeeData> = ArrayList()
) : RecyclerView.Adapter<EmployeeRecyclerViewAdapter.ViewHolder>() {

    private var listener: OnClickListener? = null

    // A function to bind the onclickListener.
    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: EmployeeData)
        fun onLongClick(position: Int, model: PlaceholderItem)
        fun onClickToEdit(position: Int, model: EmployeeData)
        fun onClickToDelete(position: Int, model: EmployeeData)
    }

    fun submitList(d: ArrayList<EmployeeData>) {
        values.clear()
        values.addAll(d)
        this.notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            EmployeeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val cpi = PlaceholderContent.createPlaceholderItem(position, item)
        holder.idView.text = cpi.id
        holder.contentView.text = cpi.content
        holder.btnEditView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToEdit(position, item )
            }
        })
        holder.btnDeleteView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToDelete(position, item )
            }
        })
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(position, item )
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: EmployeeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val btnEditView: TextView = binding.buttonEdit
        val btnDeleteView: TextView = binding.buttonDelete

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

//    companion object {
//        fun OnClickListener(function: () -> Unit): EmployeeRecyclerViewAdapter.OnClickListener {
//
//        }
//    }

}