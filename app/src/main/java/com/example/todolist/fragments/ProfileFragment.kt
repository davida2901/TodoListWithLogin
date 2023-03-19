package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.FragmentProfileBinding
import com.example.todolist.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding : FragmentProfileBinding
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        init(view)
        loadProfile()
        closeSession()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun closeSession() {
        binding.buttonGoToTask.setOnClickListener{
            navController.navigate(R.id.action_profileFragment_to_toDoListFragment)
        }
        binding.buttonCloseSession.setOnClickListener{
            auth.signOut()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun loadProfile(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.emailTextView.text = user?.email

                binding.nameTextView.text = snapshot.child("name").value.toString()
                binding.lastNameTextView.text = snapshot.child("lastName").value.toString()
                binding.phoneNumberTextView.text = snapshot.child("phoneNumber").value.toString()
                binding.addressTextView.text = snapshot.child("address").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.buttonGoToUpdateProfile.setOnClickListener{
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }
    }



}