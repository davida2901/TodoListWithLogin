package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddTaskDialogBinding
import com.example.todolist.fragments.task.CreateTaskListener

class AddTaskDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentAddTaskDialogBinding
    private lateinit var listener : CreateTaskListener

    fun setListener(listener: CreateTaskListener){
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddTaskDialogBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        createTask()
    }

    private fun createTask() {
        binding.buttonCreateTask.setOnClickListener{
            val todoTask = binding.editTextCreateTask.text.toString().trim()

            if (todoTask.isNotEmpty()){
                listener.onCreateTask(todoTask, binding.editTextCreateTask)
            }else{
                Toast.makeText(context, "Please enter your task", Toast.LENGTH_SHORT).show()
            }
        }
    }





}