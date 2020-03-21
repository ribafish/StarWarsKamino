package com.example.starwarskamino.ui.residentList

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.example.starwarskamino.databinding.ResidentListFragmentBinding

class ResidentListFragment : Fragment() {

    private var _binding: ResidentListFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ResidentListFragment()
    }

    private lateinit var viewModel: ResidentListViewModel

    val args : ResidentListFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResidentListFragmentBinding.inflate(inflater, container, false)

        binding.text.text = "Residents: ${args.residentUrlList.size}"

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ResidentListViewModelFactory).get(ResidentListViewModel::class.java)

    }

}
