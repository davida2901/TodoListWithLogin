package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.FragmentLoginBinding
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.fragments.task.CreateTaskListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ToDoListFragment : Fragment(), CreateTaskListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentToDoListBinding
    private lateinit var dialogFragment: AddTaskDialogFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentToDoListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        addTask()
    }

    private fun addTask() {
        binding.buttonAddNewTask.setOnClickListener{
            dialogFragment = AddTaskDialogFragment()
            dialogFragment.setListener(this)
            dialogFragment.show(
                childFragmentManager,
                "AddTaskDialogFragment"
            )
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())
    }

    override fun onCreateTask(todo: String, todoEditText: EditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Task saved successfully", Toast.LENGTH_SHORT).show()
                todoEditText.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            dialogFragment.dismiss()
        }
    }

}