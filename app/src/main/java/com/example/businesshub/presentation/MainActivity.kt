package com.example.businesshub.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.businesshub.R
import com.example.businesshub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bar.setOnClickListener{
            if (!isOpen){
                binding.drawer.open()
            }else{
                binding.drawer.close()
            }
            isOpen = !isOpen
        }

        binding.navigation.setNavigationItemSelectedListener {item->
            binding.drawer.close()
            isOpen=false
            return@setNavigationItemSelectedListener when (item.itemId) {
                R.id.company -> {
                    replaceFragment(CompanyFragment())
                    true
                }

                R.id.tasks -> {
                    replaceFragment(TasksFragment())
                    true
                }

                R.id.deal -> {
                    replaceFragment(DealsFragment())
                    true
                }

                R.id.clients -> {
                    replaceFragment(ClientsFragment())
                    true
                }

                R.id.meetings -> {
                    replaceFragment(MeetingsFragment())
                    true
                }

                R.id.chat -> {
                    replaceFragment(ChatFragment())
                    true
                }

                R.id.contacts -> {
                    replaceFragment(ContactsFragment())
                    true
                }

                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }

                R.id.info -> {
                    replaceFragment(InfoFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle? = null) {
        val fm = supportFragmentManager
        while (fm.backStackEntryCount > 1) {
            fm.popBackStack()
        }
        val ft = fm.beginTransaction()
        fragment.arguments=bundle
        ft.replace(R.id.nav_host_fragment, fragment).commit()
    }

}