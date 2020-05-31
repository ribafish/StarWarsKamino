package com.example.starwarskamino.ui.residentList

import android.os.Bundle
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
import com.example.starwarskamino.general.injectViewModel
import com.example.starwarskamino.ui.residentList.ResidentsAdapter.OnIdClickListener
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ResidentListFragment : DaggerFragment() {

    // ViewBinding variable
    private var _binding: ResidentListFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ResidentListViewModel
    private lateinit var viewAdapter: ResidentsAdapter

    // Get the safeArgs using navArgs helper
    private val args : ResidentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        viewModel = injectViewModel(modelFactory)
        _binding = ResidentListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Show the noResidentsData text if we get an empty list
        binding.noResidentsData.visibility = if (args.residentUrlList.isEmpty()) VISIBLE else GONE
        viewModel.setResidentUrls(args.residentUrlList)

        viewAdapter = ResidentsAdapter(object : OnIdClickListener {
            override fun onClick(id: String) {
                // On resident click, naviagate to ResidentDetailsFragment using navController with safeArgs
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
