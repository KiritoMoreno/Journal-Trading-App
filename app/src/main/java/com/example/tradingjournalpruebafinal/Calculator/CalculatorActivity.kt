package com.example.tradingjournalpruebafinal.Calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tradingjournalpruebafinal.HomeActivity
import com.example.tradingjournalpruebafinal.R
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round

class CalculatorActivity : AppCompatActivity() {
    // Variables de estado para mantener el estado de la selección de instrumentos
    private var isEurSelected : Boolean = true
    private var isXauSelected : Boolean = false

    // Declaración de vistas
    private lateinit var btnCalculate : AppCompatButton
    private lateinit var viewEur : CardView
    private lateinit var viewXau: CardView
    private lateinit var edtEntryPrice : EditText
    private lateinit var edtStopLoss : EditText
    private lateinit var edtRiskAmount: EditText
    private lateinit var tvRiskAmount: TextView
    private lateinit var tvEntryPrice: TextView
    private lateinit var tvStopLoss : TextView
    private lateinit var tvLotSize : TextView
    private lateinit var btnDashBoard: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)



        initComponents()
        initListener()


    }

    private fun initListener() {
        viewEur.setOnClickListener {
            if (!isEurSelected) {
                isEurSelected = true
                isXauSelected = false
                setInstrumentColor()
            }
        }

        viewXau.setOnClickListener {
            if (!isXauSelected) {
                isXauSelected = true
                isEurSelected = false
                setInstrumentColor()
            }
        }

        btnCalculate.setOnClickListener {
            calculate()
        }
        btnDashBoard.setOnClickListener {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initComponents( ) {
        btnDashBoard = findViewById(R.id.btnDashBoard)
        viewEur = findViewById(R.id.viewEur)
        viewXau = findViewById(R.id.viewXau)
        edtEntryPrice = findViewById(R.id.edtEntryPrice)
        edtStopLoss = findViewById(R.id.edtStopLoss)
        edtRiskAmount = findViewById(R.id.edtRiskAmount)
        tvRiskAmount = findViewById(R.id.tvRiskAmount)
        tvEntryPrice = findViewById(R.id.tvEntryPrice)
        tvStopLoss = findViewById(R.id.tvStopLoss)
        tvLotSize = findViewById(R.id.tvLotSize)
        btnCalculate = findViewById(R.id.btnCalculate)
    }
    private fun setInstrumentColor(){
        viewEur.setCardBackgroundColor(getBackgroundColor(isEurSelected))
        viewXau.setCardBackgroundColor(getBackgroundColor(isXauSelected))
    }
    private fun getBackgroundColor(isSelectedComponent: Boolean): Int {
        return ContextCompat.getColor(
            this,
            if (isSelectedComponent) R.color.background_component else R.color.background_component_selected
        )
    }

    private fun calculate() {
        if (isXauSelected) {
            // Calcular la cantidad de lotes para GOLD
            val precioEntradaGold = edtEntryPrice.text.toString().toDouble()
            val stopLossGold = edtStopLoss.text.toString().toDouble()
            val riesgoPorOperacionGold = edtRiskAmount.text.toString().toDouble()

            // Calcular lotes para GOLD
            val cantidadLotesGold = calcularLotesGold(precioEntradaGold, stopLossGold, riesgoPorOperacionGold)
            // Mostrar el resultado de los lotes recomendados para GOLD en tvLotSize
            tvLotSize.text = "$cantidadLotesGold"

            // Mostrar los inputs introducidos para GOLD en los TextView correspondientes
            tvEntryPrice.text = "$precioEntradaGold"
            tvStopLoss.text = "$stopLossGold"
            tvRiskAmount.text = "$riesgoPorOperacionGold $"
        } else if (isEurSelected) {
            // Calcular la cantidad de lotes para EUR/USD
            val precioEntradaEurUsd = edtEntryPrice.text.toString().toDouble()
            val stopLossEurUsd = edtStopLoss.text.toString().toDouble()
            val riesgoPorOperacionEurUsd = edtRiskAmount.text.toString().toDouble()

            // Calcular lotes para EUR/USD
            val cantidadLotesEurUsd = calcularLotesEurUsd(precioEntradaEurUsd, stopLossEurUsd, riesgoPorOperacionEurUsd)
            // Mostrar el resultado de los lotes recomendados para EUR/USD en tvLotSize
            tvLotSize.text = "$cantidadLotesEurUsd"

            // Mostrar los inputs introducidos para EUR/USD en los TextView correspondientes
            tvEntryPrice.text = "$precioEntradaEurUsd"
            tvStopLoss.text = "$stopLossEurUsd"
            tvRiskAmount.text = "$riesgoPorOperacionEurUsd $"
        }
    }

    private fun calcularLotesGold(precioEntradaGold: Double, stopLossGold: Double, riesgoPorOperacionGold: Double): Double {
        try {
            // Calcular la cantidad de lotes
            var lotes = riesgoPorOperacionGold / abs(precioEntradaGold - stopLossGold)

            // Ajustar la cantidad mínima de operación para GOLD (0.01 lotes representan 100 onzas)
            lotes = max(0.01, lotes)

            // Redondear siempre hacia arriba
            val lotesRedondeados = ceil(lotes * 100) / 100.0

            return lotesRedondeados
        } catch (e: ArithmeticException) {
            return 0.0 // Manejar la división por cero
        }
        // Calcular la cantidad de lotes para operar en GOLD
        val cantidadLotesGold = calcularLotesGold(precioEntradaGold, stopLossGold, riesgoPorOperacionGold)
    }
    private fun calcularLotesEurUsd(precioEntradaEurUsd: Double, stopLossEurUsd: Double, riesgoPorOperacionEurUsd: Double): Double {
        return try {
            // Calcular la cantidad de lotes
            var lotes = riesgoPorOperacionEurUsd / abs(precioEntradaEurUsd - stopLossEurUsd)

            // Ajustar la cantidad mínima de operación para EUR/USD (0.01 lotes representan 1000 unidades)
            lotes = max(0.01, lotes)

            // Convertir unidades a lotes (1000 unidades equivalen a 0.01 lotes)
            lotes *= 0.01 / 1000.0

            // Redondear a la cantidad de lotes más cercana
            val lotesRedondeados = round(lotes * 100) / 100.0 // Redondear a dos decimales

            lotesRedondeados
        } catch (e: ArithmeticException) {
            // Manejar la excepción si hay un error al dividir por cero
            0.0
        }
    }

}