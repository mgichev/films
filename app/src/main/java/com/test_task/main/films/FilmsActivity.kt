package com.test_task.main.films

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test_task.R
import com.test_task.databinding.ActivityFilmsBinding
import com.test_task.main.page.PageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmsActivity : AppCompatActivity(), Selectable {

    private lateinit var binding: ActivityFilmsBinding
    private var films: ArrayList<Film>? = null
    private lateinit var filmsAdapter: FilmsAdapter

    private var toast: Toast? = null

    private var isDataLoading = false
    private var sortType = SortType.RATING

    val SPINNER_NO_VALUE = "---"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilmsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initActivityViews()
    }

    private fun initActivityViews() {
        initYearsSpinner()
        initFilmsRecyclerView()
        initSwitchSortBtn()
        initLogoutBtn()
        initPageLoading()
        initScreenSwipe()
    }

    private fun initScreenSwipe() {
        binding.filmsSwipeLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (!isDataLoading) {
                    isDataLoading = true
                    val yearString: String = binding.yearsSpinner.selectedItem.toString()
                    val year = if (yearString != SPINNER_NO_VALUE) yearString.toInt() else null
                    val order = sortType.toString()
                    var wordFilter = binding.searchEt.text.toString() ?: ""
                    films?.clear()
                    films?.addAll(FilmDataService.getInstance()
                        .loadDataByParams(SortParams(sortType, year, order, wordFilter))!!)
                    runOnUiThread {
                        showToastDataLoaded()
                        filmsAdapter.notifyDataSetChanged()
                    }
                    isDataLoading = false
                }
                binding.filmsSwipeLayout.isRefreshing = false
            }
        }
    }

    private fun showToastDataLoaded() {
        toast?.cancel()
        toast = Toast.makeText(
            this@FilmsActivity,
            resources.getString(R.string.data_loaded), Toast.LENGTH_SHORT
        )
        toast?.show()
    }


    private fun initPageLoading() {
        binding.filmsRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (!isDataLoading) {
                                isDataLoading = true
                                films?.addAll(
                                    FilmDataService.getInstance().loadNextPage()
                                        ?: ArrayList<Film>())
                                runOnUiThread {
                                    filmsAdapter.notifyDataSetChanged()
                                    showToastDataLoaded()
                                }
                                isDataLoading = false
                            }
                        }
                    }
                }
            })
    }


    private fun initFilmsRecyclerView() {
        binding.filmsRecyclerView.layoutManager = LinearLayoutManager(this)
        films = ArrayList<Film>()
        filmsAdapter = FilmsAdapter(this@FilmsActivity, films!!)
        binding.filmsRecyclerView.adapter = filmsAdapter

        CoroutineScope(Dispatchers.IO).launch {
            isDataLoading = true
            films?.clear()
            films?.addAll(FilmDataService.getInstance().firstLoad()!!)
            runOnUiThread {
                filmsAdapter.notifyDataSetChanged()
            }
            isDataLoading = false
        }
    }

    private fun initYearsSpinner() {
        val startYear = 1950
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var years = (startYear..currentYear)
            .toMutableList().map{ it.toString() }.toMutableList()
        years.add(SPINNER_NO_VALUE)
        years = years.asReversed()
        val yearsAdapter = ArrayAdapter<String>(
            this,
            R.layout.year_item_dropdown,
            R.id.year_tv,
            years
        )
        yearsAdapter.setDropDownViewResource(R.layout.year_item_selected)
        binding.yearsSpinner.adapter = yearsAdapter
    }

    private fun initLogoutBtn() {
        binding.logoutBtn.setOnClickListener {
            finish()
        }
    }

    private fun initSwitchSortBtn() {
        binding.switchSortBtn.setOnClickListener {
            sortType = sortType.next()
            val toastText = resources.getString(R.string.sort_type) + sortType.toString()
            toast?.cancel()
            toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    override fun select(filmId: Int) {
        val intent = Intent(this, PageActivity::class.java)
        intent.putExtra(PageActivity::class.simpleName, filmId)
        startActivity(intent)
    }
}