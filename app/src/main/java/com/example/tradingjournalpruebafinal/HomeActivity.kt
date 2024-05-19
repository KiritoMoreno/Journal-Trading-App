package com.example.tradingjournalpruebafinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradingjournalpruebafinal.Analysis.AnalysisActivity
import com.example.tradingjournalpruebafinal.Calculator.CalculatorActivity
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.Graphics.GraphicsActivity
import com.example.tradingjournalpruebafinal.Journal.JournalActivity
import com.example.tradingjournalpruebafinal.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(){

    private lateinit var db: MyDatabaseHelper
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabaseHelper(this)

        binding.btnNavGraphics.setOnClickListener {
            navigateToGraphics()
        }
        binding.btnNavJournal.setOnClickListener {
            navigateToJournal()
        }
        binding.btnNavAnalysis.setOnClickListener {
            navigateToAnalysis()
        }
        binding.btnNavCalculator.setOnClickListener {
            navigateToCalculator()
        }

    }
    private fun navigateToCalculator() {
        val intent = Intent(this,CalculatorActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAnalysis() {
        val intent = Intent(this,AnalysisActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToJournal() {
        val intent = Intent(this, JournalActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToGraphics() {
        val intent = Intent(this,GraphicsActivity::class.java)
        startActivity(intent)
    }



}
