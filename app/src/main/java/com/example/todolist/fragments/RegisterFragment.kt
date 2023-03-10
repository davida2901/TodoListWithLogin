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


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding : FragmentRegisterBinding
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

            if (email.isNotEmpty()&& password.isNotEmpty() && password.length>=8){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
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