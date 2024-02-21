package com.example.businesshub.presentation.create_company

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentCompanyUsrleBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar

@AndroidEntryPoint
class CompanyDocumentsFragment : Fragment() {

    private var _binding: FragmentCompanyUsrleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompanyCreationViewModel by  navGraphViewModels(R.id.auth_nav_graph_xml)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyUsrleBinding.inflate(inflater, container, false)

        initFields()


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.date.collect { calendar ->
                    calendar?.let {
                        drawDate(it)
                    }
                }
            }
        }

        binding.next.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_companyUSRLEFragment_to_finishCompanyCreatingFragment)
        }
        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.inn.doAfterTextChanged {
            viewModel.setInn(it.toString())
            checkFields()
        }
        binding.kpp.doAfterTextChanged {
            viewModel.setKpp(it.toString())
            checkFields()
        }
        binding.ogrn.doAfterTextChanged {
            viewModel.setOgrp(it.toString())
            checkFields()
        }

        binding.date.setOnClickListener {
            val datePicker = buildDatePicker()
            datePicker.show(parentFragmentManager, null)
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setDate(it)
                checkFields()
            }
        }

        return binding.root
    }

    private fun initFields() {
        binding.inn.setText(viewModel.inn.value ?: "")
        binding.kpp.setText(viewModel.kpp.value ?: "")
        binding.ogrn.setText(viewModel.ogrn.value ?: "")
    }

    private fun buildDatePicker() = MaterialDatePicker.Builder
        .datePicker()
        .setTitleText("Выберите дату")
        .setCalendarConstraints(
            CalendarConstraints
                .Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build()
        )
        .build()

    private fun drawDate(it: GregorianCalendar) {
        val day = it.get(Calendar.DAY_OF_MONTH)
        val month = it.get(Calendar.MONTH) + 1
        val year = it.get(Calendar.YEAR)
        binding.date.setText("$day/$month/$year")
    }

    private fun checkFields() {
        val ogrn = checkOgrn()
        val inn = checkInn()
        val kpp = checkKpp()
        val date = checkDate()
        binding.next.isEnabled = ogrn && inn && kpp && date
    }

    private fun checkInn(): Boolean {
        val inn = binding.inn.text.toString()
        if (inn.isEmpty()) {
            return false
        }
        if (inn.length != 10) {
            binding.inn.error = resources.getResourceName(R.string.inn_length_error)
            return false
        }
        try {
            inn.toInt()
        } catch (e: NumberFormatException) {
            binding.inn.error = resources.getResourceName(R.string.inn_format_error)
            return false
        }
        binding.inn.error = null
        return true
    }

    private fun checkKpp(): Boolean {
        val kpp = binding.kpp.text.toString()
        if (kpp.isEmpty()) {
            return false
        }
        if (kpp.length != 9) {
            binding.kpp.error = resources.getResourceName(R.string.kpp_length_error)
            return false
        }
        try {
            kpp.toInt()
        } catch (e: NumberFormatException) {
            binding.kpp.error = resources.getResourceName(R.string.kpp_format_error)
            return false
        }
        binding.kpp.error = null
        return true
    }

    private fun checkOgrn(): Boolean {
        val ogrn = binding.ogrn.text.toString()
        if (ogrn.isEmpty()) {
            return false
        }
        if (ogrn.length != 13) {
            binding.ogrn.error = resources.getResourceName(R.string.ogrn_length_error)
            return false
        }
        try {
            ogrn.toLong()
        } catch (e: NumberFormatException) {
            binding.ogrn.error = resources.getResourceName(R.string.ogrn_format_error)
            return false
        }
        binding.ogrn.error = null
        return true
    }

    private fun checkDate(): Boolean {
        if (binding.date.text.toString().isNotEmpty()) {
            return true
        }
        return false
    }
}