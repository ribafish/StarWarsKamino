package com.example.starwarskamino.ui.residentDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.example.starwarskamino.general.Result
import com.example.starwarskamino.databinding.ResidentDetailsFragmentBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ResidentDetailsFragment : Fragment() {

    private var _binding: ResidentDetailsFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ResidentDetailsFragment()
    }

    private lateinit var viewModel: ResidentDetailsViewModel
    val args : ResidentDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ResidentDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ResidentListViewModelFactory).get(ResidentDetailsViewModel::class.java)

        binding.image.visibility = View.GONE
        binding.noImgAvailable.visibility = View.VISIBLE

        viewModel.getResident(args.residentId).observe(viewLifecycleOwner, Observer { residentResponse ->
            when (residentResponse) {
                is Result.Success -> {
                    val r = residentResponse.data
                    _binding?.name?.text = r.name
                    _binding?.height?.text = r.height
                    _binding?.mass?.text = r.mass
                    _binding?.gender?.text = r.gender
                    _binding?.birthYear?.text = r.birthYear
                    _binding?.hairColor?.text = r.hairColor
                    _binding?.skinColor?.text = r.skinColor
                    _binding?.eyeColor?.text = r.eyeColor
                    Picasso.get().load(r.imageUrl).into(_binding?.image, object: Callback{
                        override fun onSuccess() {
                            _binding?.image?.visibility = View.VISIBLE
                            _binding?.noImgAvailable?.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            _binding?.image?.visibility = View.GONE
                            _binding?.noImgAvailable?.visibility = View.VISIBLE
                        }

                    })
                }
            }
        })

    }

}