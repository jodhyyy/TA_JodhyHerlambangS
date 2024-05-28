package com.example.jodhy_monitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.jodhy_monitor.biodatasiswa.BiodataSiswa
import com.example.jodhy_monitor.databinding.ActivityDashboardBinding
import com.example.jodhy_monitor.home.Home
import com.example.jodhy_monitor.jadwal.Jadwal
import com.example.jodhy_monitor.laporansiswa.LaporanSiswa
import com.example.jodhy_monitor.walikelas.WaliKelas
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null){
            replaceFragment(Home())
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> replaceFragment(Home())
            R.id.nav_biodatasiswa -> replaceFragment(BiodataSiswa())
            R.id.nav_laporansiswa -> replaceFragment(LaporanSiswa())
            R.id.nav_jadwal -> replaceFragment(Jadwal())
            R.id.nav_walikelas -> replaceFragment(WaliKelas())
            R.id.nav_logout -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
    private fun logout() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}