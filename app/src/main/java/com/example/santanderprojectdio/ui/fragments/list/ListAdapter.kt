package com.example.santanderprojectdio.ui.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.santanderprojectdio.R
import com.example.santanderprojectdio.data.model.Task
import com.example.santanderprojectdio.ui.fragments.list.ListFragmentDirections
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder>() {
    private var taskList = emptyList<Task>()

    class ListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        return ListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ListAdapterViewHolder, position: Int) {
        val task = taskList[position]
        holder.itemView.tv_title.setText(task.title).toString()
        holder.itemView.tv_description.setText(task.description).toString()
        holder.itemView.custom_row.setOnClickListener {
            val actionUpdate = ListFragmentDirections.actionListFragmentToUpdateFragment(task)
            holder.itemView.findNavController().navigate(actionUpdate)
        }
    }

    fun setData(task: List<Task>) {
        this.taskList = task
        notifyDataSetChanged()
    }


}