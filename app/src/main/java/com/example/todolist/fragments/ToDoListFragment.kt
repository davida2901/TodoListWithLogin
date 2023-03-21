package com.example.todolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
    private  var dialogFragment: AddTaskDialogFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mTaskList : MutableList<ToDoData>

    private lateinit var builder: AlertDialog.Builder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentToDoListBinding.inflate(inflater,container,false)

        binding.buttonCancelTask.setOnClickListener{
            navController.navigate(R.id.profileFragment)
        }

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
            if (dialogFragment !=null){
                childFragmentManager.beginTransaction().remove(dialogFragment!!).commit()
            }

            dialogFragment = AddTaskDialogFragment()
            dialogFragment!!.setListener(this)
            dialogFragment!!.show(
                childFragmentManager,
                AddTaskDialogFragment.TAG
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

    override fun onCreateTask(todo: String, todoEditText: EditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Task saved successfully", Toast.LENGTH_SHORT).show()
                todoEditText.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEditText.text = null
            dialogFragment!!.dismiss()
        }
    }




    override fun onDeleteTaskClicked(toDoData: ToDoData) {

        builder = AlertDialog.Builder(this.context)

        builder.setTitle("Delete Task")
            .setMessage("You want to delete this task?")
            .setCancelable(true)
            .setPositiveButton("Yes"){
                dialogInterface, it ->
                databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No"){
                dialogInterface, it->
                dialogInterface.cancel()
            }
            .show()

//        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
//            if(it.isSuccessful){
//                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
//            }
//        }

    }


    override fun onUpdateTask(toDoData: ToDoData, todoEditText: EditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEditText.text = null
            dialogFragment!!.dismiss()
        }
    }


    override fun onEditTaskClicked(toDoData: ToDoData) {
        if(dialogFragment !=null){
            childFragmentManager.beginTransaction().remove(dialogFragment!!).commit()

        }
        dialogFragment = AddTaskDialogFragment.newInstance(toDoData.taskId, toDoData.task)
        dialogFragment!!.setListener(this)
        dialogFragment!!.show(childFragmentManager, AddTaskDialogFragment.TAG)
    }



}