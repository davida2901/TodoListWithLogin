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


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding : FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        auth = FirebaseAuth.getInstance()
//
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            navController.navigate(R.id.action_loginFragment_to_profileFragment)
//        }

        init(view)
        Login()
    }

    private fun init(view:View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun Login(){

        binding.buttonGotoRegister.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener{
            val email = binding.editTextLoginEmail.text.toString().trim()
            val password = binding.editTextLoginPassword.text.toString().trim()

            if (email.isNotEmpty()&& password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_loginFragment_to_profileFragment)
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