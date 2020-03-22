package com.example.starwarskamino.ui.residentList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.starwarskamino.databinding.ResidentListFragmentBinding
import com.example.starwarskamino.ui.residentList.ResidentsAdapter.OnIdClickListener

class ResidentListFragment : Fragment() {

    private var _binding: ResidentListFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ResidentListFragment()
    }

    private lateinit var viewModel: ResidentListViewModel
    private lateinit var viewAdapter: ResidentsAdapter

    val args : ResidentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = ResidentListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ResidentListViewModelFactory).get(ResidentListViewModel::class.java)

        binding.noResidentsData.visibility = if (args.residentUrlList.isEmpty()) VISIBLE else GONE
        viewModel.setResidentUrls(args.residentUrlList)

        viewAdapter = ResidentsAdapter(object : OnIdClickListener {
            override fun onClick(id: String) {
                val action = ResidentListFragmentDirections.actionResidentListFragmentToResidentDetailsFragment(id)
                findNavController().navigate(action)
            }
        })
        binding.recyclerView.adapter = viewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        viewModel.residentIdsLiveData.observe(viewLifecycleOwner, Observer { idList ->
            viewAdapter.residentIds = idList
            viewAdapter.notifyDataSetChanged()
        })

    }

}
