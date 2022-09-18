package com.example.hewanlist

import Database.GlobalVar
import Model.Hewan
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.hewanlist.databinding.ActivityCreateBinding
import com.example.hewanlist.databinding.ActivityMainBinding

class CreateActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        GetIntent()
        Listener()
    }

    private lateinit var tempHewan: Hewan
    var tempUri: String = ""
    private var position = -1
    private val GetResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                binding.btnProfileHewan.setImageURI(uri)
                if (uri != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        baseContext.getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }
                    tempUri = uri.toString()
                }
            }
        }

    private fun GetIntent() {
        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            binding.textCreateHewan.setText("Edit Movie")
            binding.btnSave.setText("Simpan")
            val tempHewan = GlobalVar.listDataHewan[position]
            Display(tempHewan)
        }
    }

    private fun Display(tempHewan: Hewan?) {
        binding.layoutNamaHewan.editText?.setText((tempHewan?.name))
        binding.layoutJenisHewan.editText?.setText((tempHewan?.species))
        binding.layoutUsiaHewan.editText?.setText((tempHewan?.age))
        binding.btnProfileHewan.setImageURI(Uri.parse(tempHewan?.imageUri))

    }

    private fun Listener() {
        binding.btnSave.setOnClickListener {
            var name = binding.layoutNamaHewan.editText?.text.toString().trim()
            var species = binding.layoutJenisHewan.editText?.text.toString().trim()
            var age = binding.layoutUsiaHewan.editText?.text.toString().trim()

            tempHewan = Hewan(name, species, age)
            checker()
        }

        binding.btnBack.setOnClickListener {
            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnProfileHewan.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myIntent.type = "image/*"
            GetResult.launch(myIntent)
        }
    }


    private fun checker() {
        var isCompleted: Boolean = true
        if (tempHewan.name!!.isEmpty()) {
            binding.layoutNamaHewan.error = "Nama hewan harus diisi"
            isCompleted = false
        } else {
            binding.layoutNamaHewan.error = ""
        }

        if (tempHewan.species!!.isEmpty()) {
            binding.layoutJenisHewan.error = "Jenis hewan harus diisi"
            isCompleted = false
        } else if (tempHewan.species!!.contains(".*[0-9].*".toRegex())) {
            binding.layoutJenisHewan.error = "Jenis hewan tidak boleh ada angka"
            isCompleted = false
        }

        if (tempHewan.age!!.isEmpty()) {
            binding.layoutUsiaHewan.error = "Umur hewan harus diisi"
            isCompleted = false
        } else if (tempHewan.age!!.contains(".*[A-Z].*".toRegex())) {
            binding.layoutUsiaHewan.error = "Umur hewan tidak boleh ada huruf"
            isCompleted = false
        } else if (tempHewan.age!!.contains(".*[a-z].*".toRegex())) {
            binding.layoutUsiaHewan.error = "Umur hewan tidak boleh ada huruf"
            isCompleted = false
        } else {
            binding.layoutUsiaHewan.error = ""
        }

        if (isCompleted) {
            if (position == -1) {
                tempHewan.imageUri = tempUri
                GlobalVar.listDataHewan.add(tempHewan)
                Toast.makeText(this, "Student Successfully Added", Toast.LENGTH_SHORT).show()
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
            } else {
                if (tempUri == GlobalVar.listDataHewan[position].imageUri) {
                    tempHewan.imageUri = GlobalVar.listDataHewan[position].imageUri
                } else if (tempUri == "") {
                    tempHewan.imageUri = GlobalVar.listDataHewan[position].imageUri
                } else {
                    tempHewan.imageUri = tempUri
                }
                GlobalVar.listDataHewan[position] = tempHewan
                Toast.makeText(this, "Class Successfully Edited", Toast.LENGTH_SHORT).show()
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
            }
            finish()
        }
    }
}