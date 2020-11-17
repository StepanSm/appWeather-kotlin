package com.smerkis.weamther.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smerkis.weamther.databinding.ForecastItemBinding
import com.smerkis.weamther.getDateString
import com.smerkis.weamther.getTemperature
import com.smerkis.weamther.getTimeString
import com.smerkis.weamther.model.Forecast

class ForecastsAdapter : ListAdapter<Forecast, ForecastsAdapter.ForecastsViewHolder>(ForecastDiff) {

    private object ForecastDiff : DiffUtil.ItemCallback<Forecast>() {

        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast) =
            oldItem.dt == newItem.dt

        override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastsViewHolder {
        return ForecastsViewHolder(
            ForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForecastsViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class ForecastsViewHolder(private val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(forecast: Forecast?) {
            forecast?.apply {
                binding.tvDate.text = getDateString(dt)
                binding.tvTime.text = getTimeString(dt)
                binding.tvMinTemp.text = getTemperature(main.temp_max)
                binding.tvMaxTemp.text = getTemperature(main.temp_min)
            }
        }
    }
}