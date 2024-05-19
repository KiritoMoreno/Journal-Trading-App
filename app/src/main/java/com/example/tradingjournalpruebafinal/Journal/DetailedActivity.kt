package com.example.tradingjournalpruebafinal.Journal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.Data.Transaction
import com.example.tradingjournalpruebafinal.R
import com.example.tradingjournalpruebafinal.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailedBinding
    // Se crea la base de datos una vez y se reutiliza en toda la aplicación
    private lateinit var  transaction: Transaction

    private lateinit var  db:MyDatabaseHelper
    private var transactionId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabaseHelper(this)

        transactionId = intent.getIntExtra("transaction_id",-1)
        if(transactionId == -1){
            finish()
            return
        }

        // Obtener los detalles de la transacción desde la base de datos.
        val transaction = db.getTransactionByID(transactionId)

        // Configurar los campos de texto con la información de la transacción.
        binding.instrumentInput.setText(transaction.instrument)
        binding.dayInput.setText(transaction.day.toString())
        binding.monthInput.setText(transaction.month.toString())
        binding.yearInput.setText(transaction.year.toString())
        binding.strategyInput.setText(transaction.strategy)
        binding.entryPriceInput.setText(transaction.entryPrice.toString())
        binding.stopLossInput.setText(transaction.stopLoss.toString())
        binding.takeProfitInput.setText(transaction.takeProfit.toString())
        binding.riskInput.setText(transaction.risk.toString())
        binding.winLosInput.setText(transaction.win_loss)
        binding.percentageInput.setText(transaction.percentage.toString())
        binding.emotionInput.setText(transaction.emotion)
        binding.sessionInput.setText(transaction.session)


        // Configurar un listener para ocultar el teclado cuando se toca fuera de los campos de entrada.
        val rootView: View = findViewById(android.R.id.content)
        rootView.setOnClickListener{
            this.window.decorView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }

        initListener()


        // Listener para el botón de actualizar que actualiza los datos de la transacción.
        binding.btnUpdate.setOnClickListener {
            val instrument: String = binding.instrumentInput.text.toString()
            val day: String = binding.dayInput.text.toString()
            val month: String = binding.monthInput.text.toString()
            val year: String = binding.yearInput.text.toString()
            val strategy: String = binding.strategyInput.text.toString()
            val entryPrice: Double? = binding.entryPriceInput.text.toString().toDoubleOrNull()
            val stopLoss: Double? = binding.stopLossInput.text.toString().toDoubleOrNull()
            val takeProfit: Double? = binding.takeProfitInput.text.toString().toDoubleOrNull()
            val risk: Double? = binding.riskInput.text.toString().toDoubleOrNull()
            val win_loss: String = binding.winLosInput.text.toString()
            val percentage: Double? = binding.percentageInput.text.toString().toDoubleOrNull()
            val emotion : String = binding.emotionInput.text.toString()
            val session : String = binding.sessionInput.text.toString()

            if (instrument.isEmpty()){
                binding.instrumentLayout.error = "Please enter a valid instrument"}
            else if (!isValidDay(day)||day.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid day (1-31)"
            }else if (!isValidMonth(month)||month.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid month (1-12)"
            }else if (!isValidYear(year)||year.isEmpty()) {
                binding.dayLayout.error = "Please enter a valid year (1900-2100)"
            }
            else if(strategy.isEmpty()){
                binding.strategyLayout.error = "Please enter a valid end strategy"
            }
            else if(entryPrice== null){
                binding.entryPriceLayout.error = "Please enter a valid entry price"
            }
            else if(stopLoss== null){
                binding.stopLossLayout.error = "Please enter a valid stop loss"
            }
            else if(takeProfit== null){
                binding.takeProfitLayout.error = "Please enter a valid take profit"
            }
            else if (risk == null){
                binding.riskLayout.error = "Please enter a valid number"}
            else if (!isValidWinLoss(win_loss)|| win_loss.isEmpty()){
                binding.winLossLayout.error = "Please enter a valid win or loss"}
            else if (percentage == null){
                binding.percentageLayout.error = "Please enter a valid number"}
            else if (emotion.isEmpty()){
                binding.emotionLayout.error = "Please enter a valid String"}
            else if (!isValidSession(session) || session.isEmpty()) {
                binding.sessionLayout.error = "Invalid session. Please enter one of: Asia, London, NY, New York"
            }
            else {
                val updatedTransaction = Transaction(
                    transactionId, // Use the transactionId here
                    instrument,
                    day,
                    month,
                    year,
                    strategy,
                    entryPrice,
                    stopLoss,
                    takeProfit,
                    risk,
                    win_loss,
                    percentage,
                    emotion,
                    session
                )
                update(updatedTransaction)
            }
        }
        val closeBtn: ImageButton = findViewById(R.id.btnClose)
        closeBtn.setOnClickListener {
            finish()
        }
    }
    private fun isValidDay(day: String): Boolean = day.toIntOrNull()?.let { it in 1..31 } ?: false
    private fun isValidMonth(month: String): Boolean = month.toIntOrNull()?.let { it in 1..12 } ?: false
    private fun isValidYear(year: String): Boolean = year.toIntOrNull()?.let { it in 1900..2100 } ?: false

    private fun isValidSession(session: String) = session.toLowerCase() in listOf("asia", "london", "ny", "new york")
    private fun isValidWinLoss(win_loss: String) = win_loss.toLowerCase() in listOf("win", "loss")

    private fun initListener(){
        binding.instrumentInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.instrumentLayout.error = null
            }
        }
        binding.dayInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.dayLayout.error = null
            }
        }
        binding.monthInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.monthLayout.error = null
            }
        }
        binding.yearInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.yearLayout.error = null
            }
        }
        binding.strategyInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.strategyLayout.error = null
            }
        }
        binding.entryPriceInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.entryPriceLayout.error = null
            }
        }
        binding.stopLossInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.stopLossLayout.error = null
            }
        }
        binding.takeProfitInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.takeProfitLayout.error = null
            }
        }
        binding.riskInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.riskLayout.error = null
            }
        }
        binding.winLosInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.winLossLayout.error = null
            }
        }
        binding.percentageInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
        }
        binding.emotionInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.emotionLayout.error = null
            }
        }
        binding.sessionInput.addTextChangedListener {
            binding.btnUpdate.visibility = View.VISIBLE
            if(it?.length ?: 0 > 0){
                binding.sessionLayout.error = null
            }
        }
    }

    // Actualizar los datos de la transacción en la base de datos y manejar el resultado.
    private fun update(transaction: Transaction) {
        try {
            val isUpdateSuccessful = db.update(transaction)
            if (isUpdateSuccessful) {
                // Update successful
                showMessage("Datos actualizados exitosamente")
                finish()
            } else {
                // Update failed
                showMessage("La actualización falló")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}