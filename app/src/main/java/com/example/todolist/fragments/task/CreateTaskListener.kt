package com.example.todolist.fragments.task

import android.inputmethodservice.ExtractEditText
import android.widget.EditText

interface CreateTaskListener {

    fun onCreateTask(todo : String, todoEditText: EditText)
}