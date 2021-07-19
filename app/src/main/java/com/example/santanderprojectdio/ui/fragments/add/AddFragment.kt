package com.example.santanderprojectdio.ui.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.santanderprojectdio.R
import com.example.santanderprojectdio.data.model.Task
import com.example.santanderprojectdio.ui.main.viewmodel.TaskViewModel
import com.example.santanderprojectdio.extensions.format
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.util.*

class AddFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        listeners(view)
        setHasOptionsMenu(true)

        return view
    }

    private fun insertDataToDatabase() {
        val title = til_title.editText?.text.toString()
        val description = til_description.editText?.text.toString()
        val date = til_date.editText?.text.toString()
        val hour = til_hour.editText?.text.toString()

        if (inputCheck(title, description, date, hour)) {

            val task = Task(0, title, hour, date, description)

            mTaskViewModel.addTask(task)

            Toast.makeText(
                requireContext(),
                getString(R.string.add_toast_btn_add),
                Toast.LENGTH_SHORT
            )
                .show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.add_toast_inputcheckerror),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun listeners(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_add)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        view.til_date.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                til_date.editText?.setText(Date(it + offset).format()).toString()
            }

            datePicker.show(childFragmentManager, "tag")
        }

        view.til_hour.editText?.setOnClickListener {
            val isSystem24Hour = is24HourFormat(context)
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                til_hour.editText?.setText("$hour:$minute")
            }

            timePicker.show(childFragmentManager, "tag")
        }


        view.btn_new_task.setOnClickListener {
            insertDataToDatabase()
        }

        view.btn_cancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

    }

    private fun inputCheck(
        title: String,
        description: String,
        date: String,
        hour: String
    ): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(
            date
        ) || TextUtils.isEmpty(hour))
    }

}