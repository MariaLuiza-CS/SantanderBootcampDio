package com.example.santanderprojectdio.ui.fragments.update

import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.santanderprojectdio.R
import com.example.santanderprojectdio.data.model.Task
import com.example.santanderprojectdio.ui.main.viewmodel.TaskViewModel
import com.example.santanderprojectdio.extensions.format
import com.example.santanderprojectdio.ui.fragments.update.UpdateFragmentArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.util.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        listeners(view)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_item -> {
                deleteUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listeners(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_update)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        view.update_til_date.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                update_til_date.editText?.setText(Date(it + offset).format()).toString()
            }

            datePicker.show(childFragmentManager, "tag")
        }

        view.update_til_hour.editText?.setOnClickListener {
            val isSystem24Hour = DateFormat.is24HourFormat(context)
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                update_til_hour.editText?.setText("$hour:$minute")
            }

            timePicker.show(childFragmentManager, "tag")
        }

        view.update_til_title.editText?.setText(args.currentTask.title).toString()
        view.update_til_description.editText?.setText(args.currentTask.description).toString()
        view.update_til_date.editText?.setText(args.currentTask.date).toString()
        view.update_til_hour.editText?.setText(args.currentTask.hour).toString()

        view.update_btn_new_task.setOnClickListener {
            updateItem()
        }

        view.update_btn_cancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun updateItem() {
        val title = update_til_title.editText?.text.toString()
        val description = update_til_description.editText?.text.toString()
        val date = update_til_date.editText?.text.toString()
        val hour = update_til_hour.editText?.text.toString()

        if (inputCheck(title, description, date, hour)) {
            val updateTask = Task(args.currentTask.id, title, hour, date, description)
            mTaskViewModel.updateTask(updateTask)
            Toast.makeText(
                requireContext(),
                getString(R.string.update_toast_btn_update),
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.add_toast_inputcheckerror),
                Toast.LENGTH_SHORT
            ).show()
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

    private fun deleteUser() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.update_dialog_title))
            .setMessage(getString(R.string.update_dialog_message))

        alertDialog.setNeutralButton(getString(R.string.list_dialog_neutral)) { _, _ -> }

        alertDialog.setNegativeButton(getString(R.string.list_dialog_negative)) { _, _ -> }

        alertDialog.setPositiveButton(getString(R.string.list_dialog_positive)) { _, _ ->
            mTaskViewModel.deleteTask(args.currentTask)
            Toast.makeText(
                requireContext(),
                getString(R.string.update_toast_delete),
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        alertDialog.show()
    }

}