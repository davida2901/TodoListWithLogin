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
import com.example.todolist.databinding.FragmentLoginBinding
import com.example.todolist.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding : FragmentRegisterBinding
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        init(view)
        Register()
    }

    private fun init(view:View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun Register(){

        binding.buttonCancelRegister.setOnClickListener{
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener{
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val phoneNumber = binding.editTextPhone.text.toString().trim()
            val address = binding.editTextAddress.text.toString().trim()

            if (email.isNotEmpty()
                && password.isNotEmpty()
                && password.length>=8
                && name.isNotEmpty()
                && lastName.isNotEmpty()
                && phoneNumber.isNotEmpty()
                && address.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            val currentUser = auth.currentUser
                            val currentUserDB = databaseReference.child((currentUser?.uid!!))

                            currentUserDB.child("name").setValue(name)
                            currentUserDB.child("lastName").setValue(lastName)
                            currentUserDB.child("phoneNumber").setValue(phoneNumber)
                            currentUserDB.child("address").setValue(address)


                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_registerFragment_to_profileFragment)
                        }else{
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }else{
                Toast.makeText(context, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}