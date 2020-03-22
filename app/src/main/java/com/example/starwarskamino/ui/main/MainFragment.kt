package com.example.starwarskamino.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.starwarskamino.databinding.MainFragmentBinding
import com.example.starwarskamino.general.Result
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var residentList : List<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModelFactory).get(MainViewModel::class.java)
        viewModel.getKamino().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Result.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefresh.isRefreshing = false

                    binding.name.text = result.data.name
                    binding.population.text = result.data.population
                    binding.climate.text = result.data.climate
                    binding.gravity.text = result.data.gravity
                    binding.terrain.text = result.data.terrain
                    binding.diameter.text = result.data.diameter
                    binding.surfaceWater.text = result.data.surfaceWater
                    binding.rotationPeriod.text = result.data.rotationPeriod
                    binding.orbitalPeriod.text = result.data.orbitalPeriod
                    Picasso.get().load(result.data.imageUrl).into(binding.thumb)

                    residentList = result.data.residents

                    binding.textButton.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this.context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getKamino()
        }

        binding.textButton.visibility = View.GONE
        binding.textButton.setOnClickListener{ v ->
            val action = MainFragmentDirections.actionMainFragmentToResidentListFragment(residentList?.toTypedArray() ?: emptyArray())
            v.findNavController().navigate(action)
        }

    }

}
