package com.example.starwarskamino.ui.residentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarskamino.databinding.ResidentItemBinding

class ResidentsAdapter (private val listenerId:OnIdClickListener) : RecyclerView.Adapter<ResidentsAdapter.ResidentViewHolder>() {

    /**
     * List of posts which runs the specified function when the list is changed.
     */
    var residentIds : List<String> = ArrayList()

    /**
     * Inflates the item, using ViewBinding.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentViewHolder {
        val itemBinding = ResidentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return residentIds.size
    }

    override fun onBindViewHolder(holder: ResidentViewHolder, position: Int) {
        holder.bind(residentIds[position])
    }


    inner class ResidentViewHolder(private val binding : ResidentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(residentId: String) {
            binding.residentId.text = residentId
            binding.root.setOnClickListener{ v -> listenerId.onClick(residentId)}
        }
    }

    interface OnIdClickListener {
        fun onClick(id:String)
    }
}