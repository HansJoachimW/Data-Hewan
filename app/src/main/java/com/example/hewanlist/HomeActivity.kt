package com.example.hewanlist

import Adapter.ListHewanRVAdapter
import Database.GlobalVar
import Interface.CardListener
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hewanlist.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity(), CardListener {
    private lateinit var binding : ActivityHomeBinding
    private val adapter = ListHewanRVAdapter(GlobalVar.listDataHewan, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupRecyclerView()
        listener()
        hidden()
    }

    private fun hidden(){
        if(GlobalVar.listDataHewan.isNotEmpty()){
            binding.textAddHewan.isInvisible = true
        }
        else if(GlobalVar.listDataHewan.isNotEmpty())
        {
            binding.textAddHewan.isVisible = true
        }
    }
    private fun listener(){
        binding.btnAddHewan.setOnClickListener {
            val myIntent = Intent(this, CreateActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView(){
        val layoutManager = GridLayoutManager(baseContext,2)
        binding.listHewanRV.layoutManager= layoutManager //Set layout
        binding.listHewanRV.adapter=adapter //Set adapter
    }


    override fun onCardClick(check: Boolean, position: Int) {
        if (check) {
            val myIntent = Intent(this, CreateActivity::class.java).apply{
                putExtra("position" , position)
            }

            startActivity(myIntent)
        } else {
            val confirm = AlertDialog.Builder(this)
            confirm.setTitle("Hapus Hewan?").setMessage("Apakah Anda benar-benar yakin dengan keputusan Anda?")

            confirm.setPositiveButton(android.R.string.yes) { _, _ ->
                val success = Snackbar.make(binding.listHewanRV, "Hewan Terhapus", Snackbar.LENGTH_INDEFINITE)

                success.setAction("Dismiss") {
                    success.dismiss()
                    success.setActionTextColor(Color.WHITE)
                    success.setBackgroundTint(Color.GRAY)
                    success.show()
                    GlobalVar.listDataHewan.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }

            confirm.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }
            confirm.show()
        }
    }
}