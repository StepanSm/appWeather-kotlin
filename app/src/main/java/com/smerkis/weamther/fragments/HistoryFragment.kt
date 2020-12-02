package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.smerkis.weamther.R
import com.smerkis.weamther.databinding.FragmentHistoryBinding
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.fragments.adapter.HistoryAdapter
import com.smerkis.weamther.viewModels.HistoryViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject

@FlowPreview
class HistoryFragment : BaseFragment(R.layout.fragment_history), HistoryAdapter.CityClickContract {


    private val vModel: HistoryViewModel by inject()
    private val binding: FragmentHistoryBinding by viewBinding(FragmentHistoryBinding::bind)
    private val historyAdapter = HistoryAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyAdapter.countryClickContract = this
        binding.rv.apply {
            adapter = historyAdapter
            setHasFixedSize(true)

        }
        setObserver()
        vModel.loadHistory()
    }


    private fun setObserver() {

        vModel.apply {

            cities.observe(viewLifecycleOwner) {
                historyAdapter.replaceData(it)
                historyAdapter.notifyDataSetChanged()
            }


            errorData.observe(viewLifecycleOwner) {
                handleErrorCode(it)
            }
        }

    }

    override fun onCityClicked(city: String) {
        showShortToast(city)
    }

    override fun deleteCity(city: String) {
        vModel.deleteItemHistory(city)
    }

}