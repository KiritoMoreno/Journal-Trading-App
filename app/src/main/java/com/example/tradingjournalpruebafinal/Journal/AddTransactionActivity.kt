package com.example.tradingjournalpruebafinal.Journal
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.tradingjournalpruebafinal.R
import com.example.tradingjournalpruebafinal.databinding.ActivityAddTransactionBinding
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.Data.Transaction
class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding

    private lateinit var  db:MyDatabaseHelper

    // Se crea la base de datos una vez y se reutiliza en toda la aplicación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initChangedListeners()

        db = MyDatabaseHelper(this)

        // Establecer el listener del botón para agregar una transacción
        binding.btnAddTransaction.setOnClickListener {
            val instrument: String = binding.instrumentInput.text.toString()
            val day: String = binding.dayInput.text.toString()
            val month: String = binding.monthInput.text.toString()
            val year: String = binding.yearInput.text.toString()
            val strategy: String = binding.strategyInput.text.toString()
            val entryPrice = binding.entryPriceInput.text.toString().toDoubleOrNull()
            val stopLoss: Double? = binding.stopLossInput.text.toString().toDoubleOrNull()
            val profitPrice: Double? = binding.takeProfitInput.text.toString().toDoubleOrNull()
            val risk: Double? = binding.riskInput.text.toString().toDoubleOrNull()
            val win_loss: String = binding.winLossInput.text.toString()
            val percentage: Double? = binding.percentageInput.text.toString().toDoubleOrNull()
            val emotion: String = binding.emotionInput.text.toString()
            val session : String = binding.sessionInput.text.toString()

            if (instrument.isEmpty()) {
                binding.instrumentLayout.error = "Please enter a valid instrument"
            } else if (!isValidDay(day)||day.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid day (1-31)"
            }else if (!isValidMonth(month)||month.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid month (1-12)"
            }else if (!isValidYear(year)||year.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid year (1900-2100)"
            }
            else if (strategy.isEmpty()) {
                binding.strategyLayout.error = "Please enter a valid end Strategy"
            } else if (entryPrice == null) {
                binding.entryPriceLayout.error = "Please enter a valid entry price"
            } else if (!isValidWinLoss(win_loss)|| win_loss.isEmpty()){
                binding.winLossLayout.error = "Please enter a valid win or loss"}
            else if (stopLoss == null) {
                binding.stopLossLayout.error = "Please enter a valid stop loss"
            } else if (risk == null) {
                binding.riskLayout.error = "Please enter a valid number"
            } else if (percentage == null) {
                binding.percentageLayout.error = "Please enter a valid number"
            } else if (emotion.isEmpty()) {
                binding.emotionLayout.error = "Please enter a valid string"
            }else if (!isValidSession(session) || session.isEmpty()) {
                binding.sessionLayout.error = "Invalid session. Enter one of: Asia, London, NY, New York"
            }
            else {
                val transaction = Transaction(
                    0,
                    instrument,
                    day,
                    month,
                    year,
                    strategy,
                    entryPrice ?: 0.0, // Use 0.0 as default if conversion fails
                    stopLoss ?: 0.0,
                    profitPrice ?: 0.0,
                    risk ?: 0.0,
                    win_loss,
                    percentage ?: 0.0,
                    emotion,
                    session
                )
                insert(transaction)
            }

        }
            val closeBtn: ImageButton = findViewById(R.id.btnClose)
            closeBtn.setOnClickListener {
                finish()
            }
        }

    // Inserta una nueva transacción en la base de datos
    private fun insert(transaction: Transaction) {
        try {
            if (db.insert(transaction)) {
                // Insertion successful
                showMessage("Data saved successfully")
                finish()
            } else {
                // Insertion failed
                // Optionally, you can handle the failure here
                showMessage("Insertion failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    // Funciones de validación para los campos de entrada
    private fun isValidDay(day: String): Boolean = day.toIntOrNull()?.let { it in 1..31 } ?: false
    private fun isValidMonth(month: String): Boolean = month.toIntOrNull()?.let { it in 1..12 } ?: false
    private fun isValidYear(year: String): Boolean = year.toIntOrNull()?.let { it in 1900..2100 } ?: false

    private fun isValidSession(session: String) = session.toLowerCase() in listOf("asia", "london", "ny", "new york")
    private fun isValidWinLoss(win_loss: String) = win_loss.toLowerCase() in listOf("win", "loss")


    // Inicializa los listeners para cambios de texto y manejo de errores de validación
    private fun initChangedListeners() {
        //Establece los listeners de cambio de texto para los campos de entrada y maneja los errores de validación según sea necesario.

        binding.instrumentInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.instrumentLayout.error = null
            }
        }
        binding.dayInput.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.dayLayout.error = null
            }
        }
        binding.monthInput.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.monthLayout.error = null
            }
        }
        binding.yearInput.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.yearLayout.error = null
            }
        }

        binding.strategyInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.strategyLayout.error = null
            }
        }
        binding.entryPriceInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.entryPriceLayout.error = null
            }
        }
        binding.stopLossInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.stopLossLayout.error = null
            }
        }
        binding.riskInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.riskLayout.error = null
            }
        }
        binding.percentageInput.addTextChangedListener {
            if(it?.length ?: 0 > 0){
                binding.percentageLayout.error = null
            }
        }
        binding.emotionInput.addTextChangedListener{
            if(it?.length ?: 0 > 0){
                binding.emotionLayout.error = null
            }
        }
        binding.sessionInput.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                if (isValidSession(it.toString())) {
                    binding.sessionLayout.error = null
                }
            }
        }

    }

}

