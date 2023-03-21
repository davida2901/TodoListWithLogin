package com.example.todolist.fragments.task

import android.inputmethodservice.ExtractEditText
import android.widget.CheckBox
import android.widget.EditText
import com.example.todolist.utils.ToDoData

interface CreateTaskListener {

    fun onCreateTask(todo : String, todoEditText: EditText)
    fun onUpdateTask(toDoData: ToDoData, todoEditText: EditText)

}