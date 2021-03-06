package com.example.starwarskamino.ui.residentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarskamino.R
import com.example.starwarskamino.databinding.ResidentItemBinding
import timber.log.Timber

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

    /**
     * ViewHolder as an inner class to get access of parent class values
     * @param binding ViewBinding of the inflated view to populate
     */
    inner class ResidentViewHolder(private val binding : ResidentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Helper function to bind the view to the data to be displayed
         * @param residentId id of the resident
         */
        fun bind(residentId: String) {
            binding.residentId.text = binding.residentId.context.getString(R.string.resident_id, residentId)
            binding.root.setOnClickListener{ _ -> listenerId.onClick(residentId)}
        }
    }

    interface OnIdClickListener {
        fun onClick(id:String)
    }
}