package com.test_task.main.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test_task.R
import com.test_task.databinding.ActivityPageBinding
import com.test_task.main.films.Country
import com.test_task.main.films.Film
import com.test_task.main.films.Frame
import com.test_task.main.films.Genre
import com.test_task.main.network_services.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPageBinding
    private lateinit var adapter: FramesAdapter
    private lateinit var frames: ArrayList<Frame>
    private var isDataLoading = true
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val filmId = intent.getIntExtra(PageActivity::class.simpleName, -1)

        if (filmId != -1) {
            initViews(filmId)
        } else {
            onErrorLoading()
        }
    }

    private fun onErrorLoading() {
        toast?.cancel()
        toast = Toast.makeText(
            this,
            resources.getString(R.string.error_loading),
            Toast.LENGTH_LONG
        )
        toast?.show()
        finish()
    }

    private fun initBackButton() {
        binding.pageBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initFramesRecyclerView(filmId: Int) {
        frames = ArrayList<Frame>()
        binding.framesRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        CoroutineScope(Dispatchers.IO).launch {
            frames.addAll(FrameDataService.getInstance().firstLoad(filmId)!!)
            runOnUiThread {
                adapter = FramesAdapter(frames!!)
                binding.framesRecyclerView.adapter = adapter
                isDataLoading = false
            }
        }
    }

    private fun initViews(filmId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val film = RetrofitClient.getInstance().getFilm(filmId)
            runOnUiThread {
                fillViews(film)
            }
            initFramesRecyclerView(filmId)
        }
    }

    private fun fillViews(film: Film) {
        initImageView(film)
        initTitleView(film)
        initRatingView(film)
        initDescriptionView(film)
        initGenresView(film)
        initYearCountriesView(film)
        initUrlBtn(film)
        initPageLoading()
        initBackButton()
    }

    private fun initUrlBtn(film: Film) {
        binding.pageUrlBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(film.webUrl)
                )
            )
        }
    }

    private fun initYearCountriesView(film: Film) {
        val countries =
            if (film.countries.isNotEmpty())
                film.countries.map(Country::country).joinToString(", ")
            else
                resources.getString(R.string.no_countries)
        val yearText =
            if(!film.startYear.isNullOrEmpty() && !film.endYear.isNullOrEmpty())
                "${film.startYear} - ${film.endYear}"
            else
                film.year
        val yearCountryString = "$yearText, $countries"
        binding.pageYearCountryTv.text = yearCountryString
    }

    private fun initGenresView(film: Film) {
        val genres =
            if (film.genres.isNotEmpty())
                film.genres.map(Genre::genre).joinToString(", ")
            else
                resources.getString(R.string.empty_genres)

        binding.pageGenresTv.text = genres
    }

    private fun initDescriptionView(film: Film) {
        binding.pageDescriptionTextTv.text = film.description
    }

    private fun initRatingView(film: Film) {
        binding.pageScoreTv.text = film.ratingKinopoisk
    }

    private fun initTitleView(film: Film) {
        if (film.nameRu.isNullOrEmpty()) {
            binding.pageNameTv.text = film.nameOriginal
        } else {
            binding.pageNameTv.text = film.nameRu
        }
    }

    private fun initImageView(film: Film) {
        binding.pageIv.setBackgroundResource(android.R.color.transparent)
        val emptyImage = "https://st.kp.yandex.net/images/no-poster.gif"
        if (film.coverUrl.isNullOrEmpty()) {
            Glide
                .with(baseContext)
                .load(emptyImage)
                .into(binding.pageIv)
        } else {
            Glide
                .with(baseContext)
                .load(film.coverUrl)
                .into(binding.pageIv)
        }
    }

    private fun initPageLoading() {
        binding.framesRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollHorizontally(1)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (!isDataLoading) {
                                isDataLoading = true
                                frames?.addAll(
                                    FrameDataService.getInstance().loadNextPage()
                                        ?: ArrayList<Frame>())
                                runOnUiThread {
                                    adapter.notifyDataSetChanged()
                                    toast?.cancel()
                                    toast = Toast.makeText(this@PageActivity,
                                        resources.getString(R.string.data_loaded),
                                        Toast.LENGTH_LONG)
                                    toast?.show()
                                }
                                isDataLoading = false
                            }
                        }
                    }
                }
            })
    }
}