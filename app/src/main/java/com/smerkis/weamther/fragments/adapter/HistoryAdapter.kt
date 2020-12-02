package com.smerkis.weamther.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smerkis.weamther.databinding.HistoryItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    private var cities = mutableListOf<String>()

    var countryClickContract: CityClickContract? = null

    fun replaceData(cities: List<String?>) {
        this.cities.clear()
        cities.filterNotNull().forEach { this.cities.add(it) }
    }

    class HistoryHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val city = cities[position]
        holder.binding.apply {
            cityName.text = city
            container.setOnClickListener { countryClickContract?.onCityClicked(city) }
            delete.setOnClickListener { countryClickContract?.deleteCity(city) }
        }

    }

    override fun getItemCount() = cities.size

    interface CityClickContract {
        fun onCityClicked(city: String)
        fun deleteCity(city: String)
    }
}