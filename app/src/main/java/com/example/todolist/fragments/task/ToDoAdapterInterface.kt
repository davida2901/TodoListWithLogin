package com.example.todolist.fragments.task

import android.app.AlertDialog
import com.example.todolist.fragments.ToDoListFragment
import com.example.todolist.utils.ToDoData

interface ToDoAdapterInterface {

    fun onDeleteTaskClicked(toDoData: ToDoData)
    fun onEditTaskClicked(toDoData: ToDoData)
}