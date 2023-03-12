package com.example.todolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentLoginBinding
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.fragments.task.CreateTaskListener
import com.example.todolist.fragments.task.ToDoAdapterInterface
import com.example.todolist.utils.ToDoAdapter
import com.example.todolist.utils.ToDoData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ToDoListFragment : Fragment(), CreateTaskListener, ToDoAdapterInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentToDoListBinding
    private lateinit var dialogFragment: AddTaskDialogFragment
    private lateinit var adapter: ToDoAdapter
    private lateinit var mTaskList : MutableList<ToDoData>

    private lateinit var builder: Builder


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
        getDataFromFireBase()
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

        binding.recyclerViewTasks.setHasFixedSize(true)
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(context)
        mTaskList = mutableListOf()
        adapter = ToDoAdapter(mTaskList)
        adapter.setListener(this)
        binding.recyclerViewTasks.adapter = adapter
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

    private fun getDataFromFireBase(){
        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mTaskList.clear()

                for (tasks in snapshot.children){
                    val todoTask = tasks.key?.let{
                        ToDoData(it,tasks.value.toString())
                    }
                    if(todoTask !=null){
                        mTaskList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }




    override fun onDeleteTaskClicked(toDoData: ToDoData) {


        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }





    override fun onEditTaskClicked(toDoData: ToDoData) {
        TODO("Not yet implemented")
    }



}