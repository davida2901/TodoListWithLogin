package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.FragmentProfileBinding
import com.example.todolist.databinding.FragmentUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class UpdateProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding : FragmentUpdateProfileBinding
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater, container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        init(view)
        onLoad()
        onSave()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun onLoad(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nameUpdateTextView.setText(snapshot.child("name").value.toString())
                binding.lastNameUpdateTextView.setText(snapshot.child("lastName").value.toString())
                binding.phoneNumberUpdateTextView.setText(snapshot.child("phoneNumber").value.toString())
                binding.addressUpdateTextView.setText(snapshot.child("address").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun onSave(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        binding.buttonSaveProfile.setOnClickListener{
            val name = binding.nameUpdateTextView.text.toString().trim()
            val lastName = binding.lastNameUpdateTextView.text.toString().trim()
            val phoneNumber = binding.phoneNumberUpdateTextView.text.toString().trim()
            val address = binding.addressUpdateTextView.text.toString().trim()

            if (name.isNotEmpty()
                && lastName.isNotEmpty()
                && phoneNumber.isNotEmpty()
                && address.isNotEmpty()){

                userReference?.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userReference.child("name").setValue(name)
                        userReference.child("lastName").setValue(lastName)
                        userReference.child("phoneNumber").setValue(phoneNumber)
                        userReference.child("address").setValue(address)
                        Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.profileFragment)

                    }


                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.buttonCancelUpdate.setOnClickListener{
            navController.navigate(R.id.profileFragment)
        }
    }

}