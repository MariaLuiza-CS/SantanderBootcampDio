package com.example.santanderprojectdio.ui.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.santanderprojectdio.R
import com.example.santanderprojectdio.ui.main.viewmodel.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.empty_state.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = ListAdapter()
        val recyclerView = view.rv
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(viewLifecycleOwner, { task ->
            adapter.setData(task)
            if (task.isNotEmpty()){
                view.rv.visibility = View.VISIBLE
                view.include_empty_state.visibility = View.GONE
            } else if (task.isEmpty()){
                view.include_empty_state.visibility = View.VISIBLE
            }
        })

        listeners(view)
        setHasOptionsMenu(true)

        return view
    }

    private fun listeners(view: View) {

        val toollbar = view.findViewById<Toolbar>(R.id.toolbar_list)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toollbar)

        view.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_item -> {
                deleteAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAll() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.list_dialog_title))
            .setMessage(getString(R.string.list_dialog_message))

        alertDialog.setNeutralButton(getString(R.string.list_dialog_neutral)) { _, _ -> }

        alertDialog.setNegativeButton(getString(R.string.list_dialog_negative)) { _, _ -> }

        alertDialog.setPositiveButton(getString(R.string.list_dialog_positive)) { _, _ ->
            mTaskViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                getString(R.string.list_toast_delete),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        alertDialog.show()
    }
}