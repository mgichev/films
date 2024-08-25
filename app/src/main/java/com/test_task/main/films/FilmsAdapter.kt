package com.test_task.main.films

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test_task.R
import com.test_task.databinding.FilmItemBinding

class FilmsAdapter (private val listener: Selectable, private val filmList: List<Film>) :
    RecyclerView.Adapter<FilmsAdapter.FilmHolder>() {

    class FilmHolder(private val itemBinding: FilmItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(holder: FilmHolder, film: Film) {
            with (itemBinding) {

                itemBinding.filmIv.setBackgroundResource(android.R.color.transparent)

                Glide
                    .with(holder.itemView.context)
                    .load(film.posterUrlPreview)
                    .into(filmIv)
                if (film.nameRu.isNullOrEmpty()) {
                    filmNameTv.text = film.nameOriginal
                } else {
                    filmNameTv.text = film.nameRu
                }

                val genre =
                    if (film.genres.isNotEmpty())
                        film.genres.map(Genre::genre).joinToString(", ")
                    else
                        holder.itemView.resources.getString(R.string.empty_genres)

                filmGenresTv.text = genre

                val country =
                    if (film.countries.isNotEmpty())
                        film.countries.map(Country::country).joinToString(", ")
                    else
                        holder.itemView.resources.getString(R.string.no_countries)

                val yearCountryString = "${film.year}, $country"
                yearCountryTv.text = yearCountryString
                scoreTv.text = film.ratingKinopoisk
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmHolder {
        val itemBinding = FilmItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilmHolder(itemBinding)
    }

    override fun getItemCount() = filmList.size

    override fun onBindViewHolder(holder: FilmHolder, position: Int) {
        val film: Film = filmList[position]
        holder.bind(holder, film)

        holder.itemView.setOnClickListener {
            listener.select(film.kinopoiskId)
        }
    }
}