package com.example.monitoradmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.monitoradmin.databinding.ActivityDashboardBinding
import com.example.monitoradmin.jadwal.Jadwal
import com.example.monitoradmin.matpel.MataPelajaran
import com.example.monitoradmin.softskill.SoftSkill
import com.example.monitoradmin.user.User

class Dashboard : AppCompatActivity(){
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvJadwal.setOnClickListener {
            val intent = Intent(this@Dashboard, Jadwal::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvMatpel.setOnClickListener {
            val intent = Intent(this@Dashboard, MataPelajaran::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvSoftskill.setOnClickListener {
            val intent = Intent(this@Dashboard, SoftSkill::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvUser.setOnClickListener {
            val intent = Intent(this@Dashboard, User::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvLogout.setOnClickListener {
            val intent = Intent(this@Dashboard, LoginAdmin::class.java)
            startActivity(intent)
            finish()
        }
    }
}