package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smerkis.weamther.R
import com.smerkis.weamther.databinding.FragmentHistoryBinding
import com.smerkis.weamther.fragments.adapter.HistoryAdapter
import com.smerkis.weamther.viewModels.HistoryViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension

@FlowPreview
class HistoryFragment : BaseFragment(R.layout.fragment_history), HistoryAdapter.CityClickContract {

    private val vModel: HistoryViewModel by inject()
    private val binding: FragmentHistoryBinding by viewBinding(FragmentHistoryBinding::bind)
    private val historyAdapter = HistoryAdapter()

    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createToolbar(binding.tb, title = "you recently watched")
        setRecycler()
        setObserver()
        vModel.loadHistory()
    }

    private fun setRecycler() {
        historyAdapter.countryClickContract = this
        binding.rv.apply {
            adapter = historyAdapter
            setHasFixedSize(true)

        }
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

    @KoinApiExtension
    override fun onCityClicked(city: String) {
        showShortToast(city)
        setFragmentResult(MainFragment.MAIN_FRAGMENT, bundleOf(MainFragment.SELECTED_CITY to city))
        findNavController().popBackStack()
    }

    override fun deleteCity(city: String) {
        vModel.deleteItemHistory(city)
    }

    override fun onDestroy() {
        super.onDestroy()
        historyAdapter.countryClickContract = null
    }

}