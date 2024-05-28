package com.example.monitoradmin.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.monitoradmin.Dashboard
import com.example.monitoradmin.databinding.ActivityUserBinding
import com.example.monitoradmin.user.akunuser.AkunUser
import com.example.monitoradmin.user.guru.Guru
import com.example.monitoradmin.user.orangtua.OrangTua
import com.example.monitoradmin.user.siswa.Siswa

class User : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this@User, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvAkunuser.setOnClickListener {
            val intent = Intent(this@User, AkunUser::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvGuru.setOnClickListener {
            val intent = Intent(this@User, Guru::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvOrangtua.setOnClickListener {
            val intent = Intent(this@User, OrangTua::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvSiswa.setOnClickListener {
            val intent = Intent(this@User, Siswa::class.java)
            startActivity(intent)
            finish()
        }
    }
}