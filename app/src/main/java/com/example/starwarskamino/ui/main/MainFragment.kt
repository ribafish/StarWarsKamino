package com.example.starwarskamino.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.starwarskamino.R
import com.example.starwarskamino.databinding.MainFragmentBinding
import com.example.starwarskamino.general.Result
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {
    private val PREFERENCES_LIKED = "PREFERENCES_LIKED"

    // ViewBinding variable
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var residentList : List<String>? = null

    // Has the user already liked this planet
    private var liked : Boolean = false

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private var shortAnimationDuration: Int = 0

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
        // Check SharedPreferences if the user has already liked the planet
        liked = activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(PREFERENCES_LIKED, false) == true

        // get the viewModel using a factory
        viewModel = ViewModelProvider(this, MainViewModelFactory).get(MainViewModel::class.java)
        // get the planet data and observe the returned result
        viewModel.getKamino().observe(viewLifecycleOwner, Observer { result ->
            if (_binding == null) return@Observer   // guard against getting updates when we exit the screen
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

                    binding.showResidentsButton.visibility = View.VISIBLE

                    // if the user has already liked the planet, display number of likes
                    if (liked) {
                        displayLikes(result.data.likes)
                    } else {
                        // else display the like button and set up the listeners
                        hideLikes()
                        binding.likeIcon.setOnClickListener { onLikeClick() }
                        binding.likeText.setOnClickListener { onLikeClick() }
                    }
                }
                is Result.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this.context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Observe the result from liking the planet.
        viewModel.getLikeKamino().observe(viewLifecycleOwner, Observer {
            if (_binding == null) return@Observer   // guard against getting updates when we exit the screen
            when (it) {
                is Result.Success -> {
                    displayLikes(it.data.likes)
                    // Persist that the user has liked the planet only when we get a confirmation from the network
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    if (sharedPref != null) {
                        with(sharedPref.edit()) {
                            putBoolean(PREFERENCES_LIKED, true)
                            commit()
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this.context, "Error liking: ${it.error}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getKamino()
        }

        // Hide the showResidentsButton by default -> make it visible when we get the planet data
        binding.showResidentsButton.visibility = View.GONE
        binding.showResidentsButton.setOnClickListener{ v ->
            // Navigate to the ResidentListFragment using navController and safeArgs
            val action = MainFragmentDirections.actionMainFragmentToResidentListFragment(residentList?.toTypedArray() ?: emptyArray())
            v.findNavController().navigate(action)
        }

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        binding.thumb.setOnClickListener { zoomImageFromThumb(binding.thumb) }
    }

    /**
     * Function to call when the like button is clicked
     * It will issue a network request to like the planet.
     */
    private fun onLikeClick() {
        viewModel.likeKamino()
    }

    /**
     * Hide the likes on the planet and display the like button
     */
    private fun hideLikes() {
        binding.likeText.text = getString(R.string.like)
        binding.likeIcon.setImageResource(R.drawable.favorite_border)
    }

    /**
     * Display the likes on the planet and hide the like button
     * @param likes nullable int holding the amount of likes the planet has
     */
    private fun displayLikes(likes:Int?) {
        binding.likeText.text = getString(R.string.likes, likes)
        binding.likeIcon.setImageResource(R.drawable.favorite)
        binding.likeIcon.isClickable = false
        binding.likeText.isClickable = false
    }

    /**
     * Zoom the set thumb to fullscreen.
     * @param thumbView imageView to zoom to fullscreen
     */
    private fun zoomImageFromThumb(thumbView: ImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        val expandedImageView = binding.expandedImage
        expandedImageView.setImageDrawable(thumbView.drawable)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.root.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        expandedImageView.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                expandedImageView,
                View.X,
                startBounds.left,
                finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        expandedImageView.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

}
