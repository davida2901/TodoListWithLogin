package com.example.todolist.utils

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TodoItemBinding
import com.example.todolist.fragments.task.CreateTaskListener
import com.example.todolist.fragments.task.ToDoAdapterInterface

class ToDoAdapter(private val list: MutableList<ToDoData>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var listener : ToDoAdapterInterface? = null


    fun setListener(listener: ToDoAdapterInterface){
        this.listener = listener
    }

    inner class ToDoViewHolder( val binding : TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task

                binding.deleteTask.setOnClickListener{
                    listener?.onDeleteTaskClicked(this)
                }

                binding.editTask.setOnClickListener{
                    listener?.onEditTaskClicked(this)

                }
            }
        }
    }

}