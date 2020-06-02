package com.example.starwarskamino.ui.residentDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.example.starwarskamino.general.Result
import com.example.starwarskamino.databinding.ResidentDetailsFragmentBinding
import com.example.starwarskamino.di.FragmentScope
import com.example.starwarskamino.general.injectViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import java.lang.Exception
import javax.inject.Inject

@FragmentScope
class ResidentDetailsFragment : DaggerFragment() {

    private var _binding: ResidentDetailsFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ResidentDetailsViewModel
    // Get the safeArgs using navArgs helper
    private val args : ResidentDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        viewModel = injectViewModel(modelFactory)
        _binding = ResidentDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.image.visibility = View.GONE
        binding.noImgAvailable.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()

        viewModel.getResident(args.residentId).observe(viewLifecycleOwner, Observer { residentResponse ->
            if (_binding == null) return@Observer   // guard against getting updates when we exit the screen
            when (residentResponse) {
                is Result.Success -> {
                    val r = residentResponse.data
                    binding.name.text = r.name
                    binding.height.text = r.height
                    binding.mass.text = r.mass
                    binding.gender.text = r.gender
                    binding.birthYear.text = r.birthYear
                    binding.hairColor.text = r.hairColor
                    binding.skinColor.text = r.skinColor
                    binding.eyeColor.text = r.eyeColor
                    Picasso.get().load(r.imageUrl).into(binding.image, object: Callback{
                        override fun onSuccess() {
                            if (_binding == null) return // guard against getting updates when we exit the screen
                            binding.image.visibility = View.VISIBLE
                            binding.noImgAvailable.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            if (_binding == null) return // guard against getting updates when we exit the screen
                            binding.image.visibility = View.GONE
                            binding.noImgAvailable.visibility = View.VISIBLE
                        }

                    })
                }
            }
        })
    }

}
