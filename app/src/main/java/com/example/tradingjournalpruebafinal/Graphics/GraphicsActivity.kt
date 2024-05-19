package com.example.tradingjournalpruebafinal.Graphics

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.HomeActivity
import com.example.tradingjournalpruebafinal.R
import com.example.tradingjournalpruebafinal.databinding.ActivityGraphicsBinding
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight

import com.github.mikephil.charting.utils.MPPointF

class GraphicsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGraphicsBinding
    private lateinit var db: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabaseHelper(this  )

        showPieChart()
        showLineRisk()
        showBarChart()
        binding.btnDashBoard.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showPieChart() {
        val pieChart: PieChart = binding.pieChart

        // Obtener el color del texto desde los recursos
        val textColor = ContextCompat.getColor(this, R.color.black)
        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        // Obtener los datos para el gráfico desde la base de datos
        val dataEntries = getDataEntriesFromDatabase()

        // Configurar los datos para el gráfico
        val dataSet = PieDataSet(dataEntries,"")
        dataSet.colors = getColors(dataEntries.size)
        dataSet.valueTextColor = textColor


        val pieData = PieData(dataSet)

        // Configurar el gráfico
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setBackgroundColor(chartBackgroundColor)
        pieChart.setHoleColor(chartBackgroundColor)
        pieChart.legend.textColor = Color.WHITE

        pieChart.animateY(1000)
        pieChart.invalidate()
    }
    private fun getColors(count: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val random = java.util.Random()
        for (i in 0 until count) {
            val color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
            colors.add(color)
        }
        return colors
    }
    private fun getDataEntriesFromDatabase(): List<PieEntry> {
        val transactions = db.getAllTransactions()

        // Crear un mapa para agrupar las transacciones por instrumento
        val groupedTransactions = transactions.groupBy { it.instrument }

        val dataEntries = mutableListOf<PieEntry>()

        // Calcular el total de transacciones para obtener el porcentaje
        val totalTransactions = transactions.size.toFloat()

        // Iterar sobre cada grupo de transacciones y calcular el porcentaje para cada uno
        groupedTransactions.forEach { (instrument, transactions) ->
            val percentage = (transactions.size.toFloat() / totalTransactions) * 100
            dataEntries.add(PieEntry(percentage, instrument))
        }

        return dataEntries
    }
    //----
    private fun showLineRisk() {
        val lineChartRisk: LineChart = binding.lineChartRisk

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)
        // Obtener los datos para el eje Y (porcentaje total de operaciones)
        val yAxisValues = getPercentageData()
        // Configurar los datos para el gráfico de líneas
        val lineDataSet = LineDataSet(yAxisValues, "Risk")
        lineDataSet.color = Color.BLUE
        lineDataSet.valueTextColor = Color.RED
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)

        val lineData = LineData(lineDataSet)

        // Configurar el gráfico de líneas
        lineChartRisk.data = lineData
        lineChartRisk.description.isEnabled = false
        lineChartRisk.setDrawGridBackground(false)
        lineChartRisk.animateY(1000)
        lineChartRisk.setBackgroundColor(chartBackgroundColor)
        // Setting up a simple marker to display on point tap
        setupSimpleMarker(lineChartRisk)

        // Configurar las etiquetas del eje X según el tipo de filtro
        val xAxis = lineChartRisk.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 12
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        val yAxis = lineChartRisk.axisLeft
        yAxis.textColor = Color.WHITE // Cambiar el color del texto en el eje Y a blanco
        yAxis.setDrawGridLines(true) // Habilitar las líneas de grid en el eje Y

        val yAxisRight = lineChartRisk.axisRight
        yAxisRight.textColor = Color.WHITE // Cambiar el color del texto en el eje Y (lado derecho) a blanco
        yAxisRight.setDrawGridLines(false)

        lineChartRisk.legend.isEnabled = false
        lineChartRisk.invalidate()
    }
    private fun setupSimpleMarker(chart: LineChart) {
        val markerView = object : MarkerView(this, R.layout.marker_view) {
            private val tvContent: TextView = findViewById(R.id.tvContent)

            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                val date = e?.data as? String
                tvContent.text = if (date != null) {
                    String.format("Value: %.2f%%\nDate: %s", e.y, date)
                } else {
                    String.format("Value: %.2f%%", e?.y)
                }
                super.refreshContent(e, highlight)
            }

            override fun getOffset(): MPPointF {
                return MPPointF(-(width / 2f), -height.toFloat())
            }
        }
        chart.marker = markerView
    }

    private fun getPercentageData(): ArrayList<Entry> {
        val transactions = db.getAllTransactions()
        val entries = ArrayList<Entry>()

        for ((index, transaction) in transactions.withIndex()) {
            val entry = Entry(index.toFloat(), transaction.percentage.toFloat())
            // Formatear la fecha para incluir solo mes y año
            val date = "${transaction.day}-${transaction.month}-${transaction.year}"
            entry.data = date
            entries.add(entry)
        }

        return entries
    }


    //---
    private fun showBarChart() {
        val barChartEmotion: HorizontalBarChart = binding.barChartEmotion

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        // Obtener la lista de todas las transacciones de la base de datos
        val transactionsList = db.getAllTransactions()

        // Calcular la frecuencia de cada instrumento y ordenar descendientemente por valor
        val instrumentFrequencyMap = transactionsList.groupingBy { it.emotion }.eachCount()
            .toList() // Convertir a lista para poder ordenar
            .sortedByDescending { it.second } // Ordenar por frecuencia (second) en forma descendente
            .toMap() // Convertir de nuevo a mapa si es necesario

        // Crear una lista de entradas de datos para el gráfico de barras
        val entries = instrumentFrequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat(), entry.key)
        }

        // Obtener la lista de etiquetas de los instrumentos en el mismo orden que las entradas
        val labels = instrumentFrequencyMap.map { it.key }

        // Configurar el conjunto de datos para el gráfico de barras
        val dataSet = BarDataSet(entries, "Instrument Frequency")
        dataSet.colors = getColors(entries.size) // Color de las barras
        dataSet.setValueTextColor(Color.WHITE)

        // Configurar el gráfico de barras
        val data = BarData(dataSet)
        barChartEmotion.data = data
        barChartEmotion.setFitBars(true)
        barChartEmotion.description.isEnabled = false

        // Configurar el eje X
        barChartEmotion.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChartEmotion.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChartEmotion.xAxis.setDrawGridLines(false)
        barChartEmotion.xAxis.textColor = Color.WHITE
        barChartEmotion.xAxis.granularity = 1f // Ensure one label per entry

        // Configurar los ejes Y
        barChartEmotion.axisLeft.setDrawGridLines(false)
        barChartEmotion.axisLeft.textColor = Color.WHITE
        barChartEmotion.axisRight.isEnabled = false // Disable right Y-axis

        // Configurar la leyenda y la animación
        barChartEmotion.legend.isEnabled = false
        barChartEmotion.animateY(1000)
        barChartEmotion.setBackgroundColor(chartBackgroundColor)
        barChartEmotion.invalidate() // Refresh the chart
    }


}