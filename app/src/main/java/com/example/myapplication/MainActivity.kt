package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.enums.Sections
import com.example.myapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    private var section : Sections = Sections.OVERALL

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()
        setSections()

        binding.tvOverall.isClickable = false

        viewModel.isError.observe(this) {errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        binding.next.setOnClickListener {
            viewModel.loadRandomGif(section)
        }

        binding.tryAgain.setOnClickListener {
            viewModel.loadRandomGif(section)
        }

        binding.previous.setOnClickListener {
           viewModel.loadPreviousGif()
        }

    }

    private fun setSections() {
        val jumpUpAnimation = AnimationUtils.loadAnimation(this, R.anim.jump_up)
        binding.tvOverall.setOnClickListener {
            section = Sections.OVERALL
            with(binding) {
                it.startAnimation(jumpUpAnimation)
                selectTub(tvOverall, firstViewBoard,
                    listOf(Pair(tvKids, secondViewBoard),
                        Pair(tvAdults, thirdViewBoard)))
            }
        }

        binding.tvKids.setOnClickListener {
            section = Sections.KIDS
            with(binding) {
                it.startAnimation(jumpUpAnimation)
                selectTub(tvKids, secondViewBoard,
                    listOf(Pair(tvOverall, firstViewBoard),
                        Pair(tvAdults, thirdViewBoard)))
            }
        }

        binding.tvAdults.setOnClickListener {
            section = Sections.ADULTS
            with(binding) {
                it.startAnimation(jumpUpAnimation)
                selectTub(tvAdults, thirdViewBoard,
                    listOf(Pair(tvKids, secondViewBoard),
                        Pair(tvOverall, firstViewBoard)))
            }
        }
    }

    private fun selectTub(selected : TextView,
                          selectedBoard : View,
                          others : List<Pair<TextView, View>>,
                          ) {

        selected.isClickable = false
        selected.setTextColor(getColor(R.color.main))
        selected.setTypeface(null, Typeface.BOLD)
        selectedBoard.setBackgroundColor(getColor(R.color.main))

        others.forEach { (textView, viewBoard) ->
            textView.isClickable = true
            textView.setTextColor(getColor(R.color.black))
            textView.setTypeface(null, Typeface.NORMAL)
            viewBoard.setBackgroundColor(getColor(android.R.color.transparent))
        }
    }

    private fun observeViewModel() {
        observeInternet()
        observeDisplayGif()
        observeCanGoBack()
        observeProgressBar()
    }

    private fun observeInternet() {
        val mainLayout = binding.cLayoutMain
        val errorLayout = binding.cLayoutError
        viewModel.isInternetWorking.observe(this) {isConnected ->
            mainLayout.visibility = if (isConnected) View.VISIBLE else View.GONE
            errorLayout.visibility = if (isConnected) View.GONE else View.VISIBLE
        }
    }

    private fun observeDisplayGif() {
        viewModel.displayGif.observe(this) { gif ->
            Glide.with(this)
                .asGif()
                .load(gif.images.url.url)
                .listener(object : RequestListener<GifDrawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onResourceReady(
                        resource: GifDrawable,
                        model: Any,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.ivMainGif)
            binding.title.text = gif.title
        }
    }

    private fun observeCanGoBack() {
        viewModel.canGoBack.observe(this) {canGoBack ->
            binding.previous.also {button ->
                button.alpha = if (canGoBack) 1f else 0.7f
                button.isClickable = canGoBack
            }
        }
    }

    private fun observeProgressBar() {
        viewModel.progress.observe(this) { isLoading ->
            with(binding) {
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun newIntent(context : Context) : Intent = Intent(context, MainActivity::class.java)
    }
}