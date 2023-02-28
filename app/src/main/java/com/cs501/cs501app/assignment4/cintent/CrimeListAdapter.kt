package com.cs501.cs501app.assignment4.cintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cs501.cs501app.databinding.ListItemCrimeBinding
import com.cs501.cs501app.databinding.ListItemCrimePoliceBinding



class CrimeHolder(
    private val binding: ListItemCrimeBinding,
    private val requiresPolice: Boolean
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()
        if (!requiresPolice){
            binding.requirePoliceBtn.visibility = View.INVISIBLE
        }

        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${crime.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : CrimeHolder {
        return if (viewType == 0){
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            return CrimeHolder(binding,false)
        }else{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            return CrimeHolder(binding,true)
        }
    }
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }
    override fun getItemCount() = crimes.size

    override fun getItemViewType(position: Int): Int {
        return when(crimes[position].requiresPolice){
            true ->  0
            else ->  1
        }
    }
}