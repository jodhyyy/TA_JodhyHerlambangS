package com.example.monitoradmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.monitoradmin.databinding.LoginAdminBinding

class LoginAdmin : AppCompatActivity() {
    private lateinit var binding: LoginAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LoginAdmin, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
    }
}