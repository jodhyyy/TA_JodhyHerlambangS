package com.example.guru

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guru.absen.Absensi
import com.example.guru.databinding.ActivityDashboardBinding
import com.example.guru.nilai.Nilai
import com.example.guru.ortu.OrangTua
import com.example.guru.siswa.Siswa

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvOrtu.setOnClickListener {
            val intent = Intent(this@Dashboard, OrangTua::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvSiswa.setOnClickListener {
            val intent = Intent(this@Dashboard, Siswa::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvAbsen.setOnClickListener {
            val intent = Intent(this@Dashboard, Absensi::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvNilai.setOnClickListener {
            val intent = Intent(this@Dashboard, Nilai::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvLogout.setOnClickListener {
            val intent = Intent(this@Dashboard, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}