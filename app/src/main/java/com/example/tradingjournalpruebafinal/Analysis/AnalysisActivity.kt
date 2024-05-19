package com.example.tradingjournalpruebafinal.Analysis

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.HomeActivity
import com.example.tradingjournalpruebafinal.R
import com.example.tradingjournalpruebafinal.databinding.ActivityAnalysisBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

import java.util.Random

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private lateinit var db: MyDatabaseHelper
    private lateinit var lineChartYSIES: LineChart
    private lateinit var lineChartSYMRisk: LineChart
    private lateinit var lineChartYSIE: LineChart
    private lateinit var lineChartYSI: LineChart
    private lateinit var lineChartYS: LineChart
    private lateinit var lineChartY: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabaseHelper(this)
        lineChartYSIES = binding.lineChartSYMRisk
        lineChartSYMRisk = binding.lineChartSYMRisk
        lineChartYSIE = binding.lineChartSYMRisk
        lineChartYSI = binding.lineChartSYMRisk
        lineChartYS = binding.lineChartSYMRisk
        lineChartY = binding.lineChartSYMRisk

        showBarChart()
        showBarChartSYM()
        lineChartSYMRisk()
        initListeners()



    }
    private fun initListeners(){
        binding.btnDashBoard.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.btnInstrumentFilter.setOnClickListener {
            val instrumentInput = binding.instrumentInputFilter.text.toString().trim()
            if(instrumentInput.isEmpty()){
                showBarChart()
            }else{
                filterAndShowBarChart(instrumentInput)
            }

        }

        binding.btnYMFilter.setOnClickListener {

            val strategyInput = binding.strategyInputFilter.text.toString().trim()
            val yearInput = binding.yearInputFilter.text.toString().trim()
            val monthInput = binding.monthInputFilter.text.toString().trim()

            when{
                yearInput.isNotEmpty() && monthInput.isNotEmpty() && strategyInput.isNotEmpty() -> showBarChartFilterSYM(yearInput,monthInput,strategyInput)
                yearInput.isNotEmpty() && monthInput.isNotEmpty() && strategyInput.isEmpty() -> showBarChartFilterYM(yearInput,monthInput)
                yearInput.isNotEmpty() && monthInput.isEmpty() && strategyInput.isEmpty() -> showBarChartFilterY(yearInput)
                strategyInput.isNotEmpty() && yearInput.isEmpty() && monthInput.isEmpty() -> showBarChartFilterStrategy(strategyInput)
                yearInput.isEmpty() && strategyInput.isEmpty() && monthInput.isNotEmpty() ->showBarChartFilterSYM(yearInput,monthInput,strategyInput)
                yearInput.isNotEmpty() && strategyInput.isNotEmpty() && monthInput.isEmpty() ->showBarChartFilterSYM(yearInput,monthInput,strategyInput)
                else -> showBarChartFilterSYM(yearInput,monthInput,strategyInput)
            }

        }

        binding.btnYMFilterRisk.setOnClickListener {
            val strategyInput = binding.strategyInputFilterRisk.text.toString().trim()
            val yearInput = binding.yearInputFilterRisk.text.toString().trim()
            val sessionInput = binding.sessionInputFilterRisk.text.toString().trim()
            val instrumentInput = binding.instrumentInputFilterRisk.text.toString().trim()
            val emotionInput = binding.emotionInputFilterRisk.text.toString().trim()

            when{
                yearInput.isNotEmpty() && sessionInput.isNotEmpty() && instrumentInput.isNotEmpty() && emotionInput.isNotEmpty() && strategyInput.isNotEmpty() -> showLineChartYSIES(yearInput,sessionInput,instrumentInput,emotionInput,strategyInput)
                yearInput.isNotEmpty() && sessionInput.isNotEmpty() && instrumentInput.isNotEmpty() && emotionInput.isNotEmpty() && strategyInput.isEmpty()-> showLineChartYSIE(yearInput,sessionInput,instrumentInput,emotionInput)
                yearInput.isNotEmpty() && sessionInput.isNotEmpty() && instrumentInput.isNotEmpty() && emotionInput.isEmpty() && strategyInput.isEmpty()-> showLineChartYSI(yearInput,sessionInput,instrumentInput)
                yearInput.isNotEmpty() && sessionInput.isNotEmpty() && instrumentInput.isEmpty() && emotionInput.isEmpty() && strategyInput.isEmpty()-> showLineChartYS(yearInput,sessionInput)
                yearInput.isNotEmpty() && sessionInput.isEmpty() && instrumentInput.isEmpty() && emotionInput.isEmpty() && strategyInput.isEmpty()-> showLineChartY(yearInput)
                else -> showLineChartYSIES(yearInput,sessionInput,instrumentInput,emotionInput,strategyInput)
            }

        }

    }


    // Método para obtener diferentes colores para cada barra
    private fun getColors(count: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val random = Random()
        for (i in 0 until count) {
            val color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
            colors.add(color)
        }
        return colors
    }

    private fun showBarChart() {
        val horizontalBarChart: HorizontalBarChart = binding.barChart

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        // Obtener la lista de todas las transacciones de la base de datos
        val transactionsList = db.getAllTransactions()

        // Calcular la frecuencia de cada instrumento y ordenar descendientemente por valor
        val instrumentFrequencyMap = transactionsList.groupingBy { it.instrument }.eachCount()
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
        horizontalBarChart.data = data
        horizontalBarChart.setFitBars(true)
        horizontalBarChart.description.isEnabled = false

        // Configurar el eje X
        horizontalBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        horizontalBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        horizontalBarChart.xAxis.setDrawGridLines(false)
        horizontalBarChart.xAxis.textColor = Color.WHITE
        horizontalBarChart.xAxis.granularity = 1f // Ensure one label per entry

        // Configurar los ejes Y
        horizontalBarChart.axisLeft.setDrawGridLines(false)
        horizontalBarChart.axisLeft.textColor = Color.WHITE
        horizontalBarChart.axisRight.isEnabled = false // Disable right Y-axis

        // Configurar la leyenda y la animación
        horizontalBarChart.legend.isEnabled = false
        horizontalBarChart.animateY(1000)
        horizontalBarChart.setBackgroundColor(chartBackgroundColor)
        horizontalBarChart.invalidate() // Refresh the chart
    }

    private fun filterAndShowBarChart(instrument: String) {
        val horizontalBarChart: HorizontalBarChart = binding.barChart

        // Obtener la lista de todas las transacciones de la base de datos filtradas por instrumento
        val filteredTransactions = db.getAllTransactions().filter { it.instrument == instrument }

        // Calcular la frecuencia de cada instrumento
        val instrumentFrequencyMap = filteredTransactions.groupingBy { it.instrument }.eachCount()

        // Crear una lista de entradas de datos para el gráfico de barras
        val entries = instrumentFrequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat(), entry.key)
        }

        // Configurar el conjunto de datos para el gráfico de barras
        val dataSet = BarDataSet(entries, "Instrument Frequency")
        dataSet.colors = getColors(entries.size)
        dataSet.setValueTextColor(Color.WHITE)

        // Configurar el gráfico de barras
        val data = BarData(dataSet)
        horizontalBarChart.data = data
        horizontalBarChart.setFitBars(true)
        horizontalBarChart.animateY(1000)
        horizontalBarChart.description.isEnabled = false
        horizontalBarChart.invalidate()
    }

    //-----
    private fun showBarChartSYM() {
        val barChart: BarChart = binding.barChartSYM

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        // Obtener la lista de todas las transacciones de la base de datos
        val transactionsList = db.getAllTransactions()

        // Calcular la frecuencia de cada instrumento
        val instrumentFrequencyMap = transactionsList.groupingBy { it.strategy }.eachCount()

        // Crear una lista de entradas de datos para el gráfico de barras
        val entries = instrumentFrequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat(), entry.key)
        }

        // Obtener la lista de etiquetas de los instrumentos
        val labels = instrumentFrequencyMap.keys.toList()

        // Configurar el conjunto de datos para el gráfico de barras
        val dataSet = BarDataSet(entries, "Instrument Frequency")
        dataSet.colors = getColors(entries.size) // Color de las barras
        dataSet.valueTextColor = Color.WHITE

        // Configurar el gráfico de barras
        val data = BarData(dataSet)
        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        barChart.xAxis.isEnabled = true  // eje X
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textColor = Color.WHITE

        barChart.axisLeft.isEnabled = true  // eje Y
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE

        // Configurar la leyenda
        barChart.legend.isEnabled = false
        barChart.animateY(1000)
        barChart.setBackgroundColor(chartBackgroundColor)
        barChart.invalidate()

    }

    private fun showBarChartFilterSYM(year: String, month: String, strategy: String) {
        val barChart: BarChart = binding.barChartSYM

        // Verificar las condiciones de entrada antes de proceder
        if (year.isEmpty() && strategy.isEmpty() && month.isNotEmpty()) {
            Toast.makeText(this, "Error: Year and Strategy cannot be empty when Month is specified.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
            return
        }else if (year.isNotEmpty() && strategy.isNotEmpty() && month.isEmpty()){
            Toast.makeText(this, "Error: Month cannot be empty when Year and Strategy are specified.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
            return
        }

        // Continuar solo si los datos son válidos
        if (year.isNotEmpty() && month.isNotEmpty() && strategy.isNotEmpty()) {
            val transactionsList = db.getTransactionsFiltered(year, month, strategy)

            if (transactionsList.isEmpty()) {
                Toast.makeText(this, "Please enter a valid filters", Toast.LENGTH_LONG).show()
                barChart.clear()
                barChart.invalidate()
                return
            }

            // Calcular la frecuencia de cada estrategia
            val frequencyMap = transactionsList.groupingBy { it.strategy }.eachCount()

            // Crear entradas para el gráfico
            val entries = frequencyMap.entries.mapIndexed { index, entry ->
                BarEntry(index.toFloat(), entry.value.toFloat())
            }

            // Configurar el conjunto de datos y actualizar el gráfico
            val dataSet = BarDataSet(entries, "Strategy Frequency")
            dataSet.colors = getColors(entries.size)
            dataSet.valueTextColor = Color.WHITE

            val data = BarData(dataSet)
            barChart.data = data
            barChart.setFitBars(true)
            barChart.description.isEnabled = false
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(frequencyMap.keys.toList())
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.textColor = Color.WHITE
            barChart.axisLeft.setDrawGridLines(false)
            barChart.axisLeft.textColor = Color.WHITE
            barChart.axisRight.isEnabled = false
            barChart.legend.isEnabled = false
            barChart.animateY(1000)
            barChart.invalidate()
        } else {
            Toast.makeText(this, "Please provide year, month, and strategy.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
        }
    }
    private fun showBarChartFilterYM(year:String , month: String) {
        val barChart: BarChart = binding.barChartSYM

        // Fetch filtered transactions
        val transactionsList = db.getTransactionsFilteredYM(year,month)

        // Check if there are transactions to display
        if (transactionsList.isEmpty()) {
            Toast.makeText(this, "No data available for year: $year, month: $month.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
            return
        }

        // Calculate the frequency of each strategy
        val frequencyMap = transactionsList.groupingBy { it.strategy }.eachCount()

        // Create a list of bar entries
        val entries = frequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        // Labels for the xAxis
        val labels = frequencyMap.keys.toList()

        // Setup the dataset and styles
        val dataSet = BarDataSet(entries, "Strategy Frequency")
        dataSet.colors = getColors(entries.size)
        dataSet.valueTextColor = Color.WHITE

        val data = BarData(dataSet)
        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }
    private fun showBarChartFilterY(year:String) {
        val barChart: BarChart = binding.barChartSYM

        // Fetch filtered transactions
        val transactionsList = db.getTransactionsFilteredY(year)

        // Check if there are transactions to display
        if (transactionsList.isEmpty()) {
            Toast.makeText(this, "No data available for year: $year.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
            return
        }

        // Calculate the frequency of each strategy
        val frequencyMap = transactionsList.groupingBy { it.strategy }.eachCount()

        // Create a list of bar entries
        val entries = frequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        // Labels for the xAxis
        val labels = frequencyMap.keys.toList()

        // Setup the dataset and styles
        val dataSet = BarDataSet(entries, "Strategy Frequency")
        dataSet.colors = getColors(entries.size)
        dataSet.valueTextColor = Color.WHITE

        val data = BarData(dataSet)
        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }
    private fun showBarChartFilterStrategy(strategy: String) {
        val barChart: BarChart = binding.barChartSYM

        // Fetch filtered transactions
        val transactionsList = db.getTransactionsFilteredStrategy(strategy)

        // Check if there are transactions to display
        if (transactionsList.isEmpty()) {
            Toast.makeText(this, "No data available for strategy: $strategy.", Toast.LENGTH_LONG).show()
            barChart.clear()
            barChart.invalidate()
            return
        }

        // Calculate the frequency of each strategy
        val frequencyMap = transactionsList.groupingBy { it.strategy }.eachCount()

        // Create a list of bar entries
        val entries = frequencyMap.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        // Labels for the xAxis
        val labels = frequencyMap.keys.toList()

        // Setup the dataset and styles
        val dataSet = BarDataSet(entries, "Strategy Frequency")
        dataSet.colors = getColors(entries.size)
        dataSet.valueTextColor = Color.WHITE

        val data = BarData(dataSet)
        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

    //----
    private fun setupSimpleMarker(chart: LineChart) {
        val markerView = object : MarkerView(this, R.layout.marker_view) {
            private val tvContent: TextView = findViewById(R.id.tvContent)

            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                tvContent.text = String.format("Value: %.2f", e?.y)
                super.refreshContent(e, highlight)
            }

            override fun getOffset(): MPPointF {
                return MPPointF(-(width / 2f), -height.toFloat())
            }
        }
        chart.marker = markerView
    }
// Método para obtener datos porcentuales y mostrarlos en un gráfico de líneas configurado
    private fun lineChartSYMRisk() {
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
        lineChartSYMRisk.data = lineData
        lineChartSYMRisk.description.isEnabled = false
        lineChartSYMRisk.setDrawGridBackground(false)
        lineChartSYMRisk.animateY(1000)
        lineChartSYMRisk.setBackgroundColor(chartBackgroundColor)
        // Setting up a simple marker to display on point tap
        setupSimpleMarker(lineChartSYMRisk)

        // Configurar las etiquetas del eje X según el tipo de filtro
        val xAxis = lineChartSYMRisk.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 12
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        val yAxis = lineChartSYMRisk.axisLeft
        yAxis.textColor = Color.WHITE // Cambiar el color del texto en el eje Y a blanco
        yAxis.setDrawGridLines(true) // Habilitar las líneas de grid en el eje Y

        val yAxisRight = lineChartSYMRisk.axisRight
        yAxisRight.textColor = Color.WHITE // Cambiar el color del texto en el eje Y (lado derecho) a blanco
        yAxisRight.setDrawGridLines(false)

        lineChartSYMRisk.legend.isEnabled = false
        lineChartSYMRisk.invalidate()
    }
    private fun getPercentageData( ): ArrayList<Entry> {
        val transactions = db.getAllTransactions() // Obtener todas las transacciones de la base de datos
        val entries = ArrayList<Entry>()

        // Iterar sobre las transacciones y agregar los valores de percentage a la lista de entradas
        for ((index, transaction) in transactions.withIndex()) {
            // Aquí debes ajustar cómo accedes a la propiedad percentage según tu clase Transaction
            val percentage = transaction.percentage.toFloat() // Asegúrate de convertir el valor a Float si es necesario
            entries.add(Entry(index.toFloat(), percentage))
        }

        return entries
    }


    private fun showLineChartYSIES(year: String, session: String, instrument: String, emotion: String, strategy: String) {

        // Continuar solo si los datos son válidos
        if (year.isNotEmpty() && session.isNotEmpty() && instrument.isNotEmpty()&& emotion.isNotEmpty()&& strategy.isNotEmpty()) {

            val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

            val yAxisValues = getPercentageDataYSIES(year, session, instrument, emotion, strategy)

            val lineDataSet = LineDataSet(yAxisValues, "Risk")
            lineDataSet.color = Color.BLUE
            lineDataSet.setDrawCircles(true)
            lineDataSet.setDrawValues(false)

            val lineData = LineData(lineDataSet)
            lineChartYSIES.data = lineData
            lineChartYSIES.description.isEnabled = false
            lineChartYSIES.setDrawGridBackground(false)
            lineChartYSIES.animateY(1000)
            lineChartYSIES.setBackgroundColor(chartBackgroundColor)
            setupSimpleMarker(lineChartYSIES)

            val xAxis = lineChartYSIES.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = 12
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)

            val yAxis = lineChartYSIES.axisLeft
            yAxis.textColor = Color.WHITE
            yAxis.setDrawGridLines(true)

            val yAxisRight = lineChartYSIES.axisRight
            yAxisRight.isEnabled = false

            lineChartYSIES.legend.isEnabled = false
            lineChartYSIES.invalidate()
        }else{
            Toast.makeText(this, "Error: Please fill all the fields.", Toast.LENGTH_LONG).show()
            lineChartYSIES.clear()
            lineChartYSIES.invalidate()
            return
        }

    }
    private fun getPercentageDataYSIES(year: String, session: String, instrument: String, emotion: String, strategy: String): ArrayList<Entry> {
        val transactions = db.getTransactionsFilteredYSIES(year, session, instrument, emotion, strategy)
        val entries = ArrayList<Entry>()

        // Check if there are transactions to display
        if (transactions.isEmpty()) {
            Toast.makeText(this, "No data available for Year: $year, Session: $session, Instrument: $instrument, Emotion: $emotion, Strategy: $strategy.", Toast.LENGTH_LONG).show()
            lineChartYSIES.clear()
            lineChartYSIES.invalidate()

        }else{
            for ((index, transaction) in transactions.withIndex()) {
                val percentage = transaction.percentage.toFloat()
                entries.add(Entry(index.toFloat(), percentage))
            }
        }


        return entries
    }
    //__
    private fun showLineChartYSIE(year: String, session: String, instrument: String, emotion: String) {

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        val yAxisValues = getPercentageDataYSIE(year, session, instrument, emotion)

        val lineDataSet = LineDataSet(yAxisValues, "Risk")
        lineDataSet.color = Color.BLUE
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawValues(false)

        val lineData = LineData(lineDataSet)
        lineChartYSIE.data = lineData
        lineChartYSIE.description.isEnabled = false
        lineChartYSIE.setDrawGridBackground(false)
        lineChartYSIE.animateY(1000)
        lineChartYSIE.setBackgroundColor(chartBackgroundColor)
        setupSimpleMarker(lineChartYSIE)

        val xAxis = lineChartYSIE.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 12
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        val yAxis = lineChartYSIE.axisLeft
        yAxis.textColor = Color.WHITE
        yAxis.setDrawGridLines(true)

        val yAxisRight = lineChartYSIE.axisRight
        yAxisRight.isEnabled = false

        lineChartYSIE.legend.isEnabled = false
        lineChartYSIE.invalidate()
    }
    private fun getPercentageDataYSIE(year: String, session: String, instrument: String, emotion: String): ArrayList<Entry> {
        val transactions = db.getTransactionsFilteredYSIE(year, session, instrument, emotion)
        val entries = ArrayList<Entry>()

        // Check if there are transactions to display
        if (transactions.isEmpty()) {
            Toast.makeText(this, "No data available for Year: $year, Session: $session, Instrument: $instrument, Emotion: $emotion.", Toast.LENGTH_LONG).show()
            lineChartYSIE.clear()
            lineChartYSIE.invalidate()

        }else{
            for ((index, transaction) in transactions.withIndex()) {
                val percentage = transaction.percentage.toFloat()
                entries.add(Entry(index.toFloat(), percentage))
            }
        }

        return entries
    }
    //__
    private fun showLineChartYSI(year: String, session: String, instrument: String) {

    val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

    val yAxisValues = getPercentageDataYSI(year, session, instrument)

    val lineDataSet = LineDataSet(yAxisValues, "Risk")
    lineDataSet.color = Color.BLUE
    lineDataSet.setDrawCircles(true)
    lineDataSet.setDrawValues(false)

    val lineData = LineData(lineDataSet)
    lineChartYSI.data = lineData
    lineChartYSI.description.isEnabled = false
    lineChartYSI.setDrawGridBackground(false)
    lineChartYSI.animateY(1000)
    lineChartYSI.setBackgroundColor(chartBackgroundColor)
    setupSimpleMarker(lineChartYSI)

    val xAxis = lineChartYSI.xAxis
    xAxis.valueFormatter = IndexAxisValueFormatter()
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.labelCount = 12
    xAxis.granularity = 1f
    xAxis.setDrawGridLines(false)

    val yAxis = lineChartYSI.axisLeft
    yAxis.textColor = Color.WHITE
    yAxis.setDrawGridLines(true)

    val yAxisRight = lineChartYSI.axisRight
    yAxisRight.isEnabled = false

    lineChartYSI.legend.isEnabled = false
    lineChartYSI.invalidate()
}
    private fun getPercentageDataYSI(year: String, session: String, instrument: String): ArrayList<Entry> {
        val transactions = db.getTransactionsFilteredYSI(year, session, instrument)
        val entries = ArrayList<Entry>()

        // Check if there are transactions to display
        if (transactions.isEmpty()) {
            Toast.makeText(this, "No data available for Year: $year, Session: $session, Instrument: $instrument.", Toast.LENGTH_LONG).show()
            lineChartYSI.clear()
            lineChartYSI.invalidate()

        }else{
            for ((index, transaction) in transactions.withIndex()) {
                val percentage = transaction.percentage.toFloat()
                entries.add(Entry(index.toFloat(), percentage))
            }
        }

        return entries
    }
    //__
    private fun showLineChartYS(year: String, session: String) {

    val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

    val yAxisValues = getPercentageDataYS(year, session)

    val lineDataSet = LineDataSet(yAxisValues, "Risk")
    lineDataSet.color = Color.BLUE
    lineDataSet.setDrawCircles(true)
    lineDataSet.setDrawValues(false)

    val lineData = LineData(lineDataSet)
    lineChartYS.data = lineData
    lineChartYS.description.isEnabled = false
    lineChartYS.setDrawGridBackground(false)
    lineChartYS.animateY(1000)
    lineChartYS.setBackgroundColor(chartBackgroundColor)
    setupSimpleMarker(lineChartYS)

    val xAxis = lineChartYS.xAxis
    xAxis.valueFormatter = IndexAxisValueFormatter()
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.labelCount = 12
    xAxis.granularity = 1f
    xAxis.setDrawGridLines(false)

    val yAxis = lineChartYS.axisLeft
    yAxis.textColor = Color.WHITE
    yAxis.setDrawGridLines(true)

    val yAxisRight = lineChartYS.axisRight
    yAxisRight.isEnabled = false

    lineChartYS.legend.isEnabled = false
    lineChartYS.invalidate()
}
    private fun getPercentageDataYS(year: String, session: String): ArrayList<Entry> {
        val transactions = db.getTransactionsFilteredYS(year, session)
        val entries = ArrayList<Entry>()

        // Check if there are transactions to display
        if (transactions.isEmpty()) {
            Toast.makeText(this, "No data available for Year: $year, Session: $session.", Toast.LENGTH_LONG).show()
            lineChartYS.clear()
            lineChartYS.invalidate()

        }else{
            for ((index, transaction) in transactions.withIndex()) {
                val percentage = transaction.percentage.toFloat()
                entries.add(Entry(index.toFloat(), percentage))
            }
        }

        return entries
    }
    //__
    private fun showLineChartY(year: String) {

        val chartBackgroundColor = ContextCompat.getColor(this, R.color.background_component_selected)

        val yAxisValues = getPercentageDataY(year)

        val lineDataSet = LineDataSet(yAxisValues, "Risk")
        lineDataSet.color = Color.BLUE
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawValues(false)

        val lineData = LineData(lineDataSet)
        lineChartY.data = lineData
        lineChartY.description.isEnabled = false
        lineChartY.setDrawGridBackground(false)
        lineChartY.animateY(1000)
        lineChartY.setBackgroundColor(chartBackgroundColor)
        setupSimpleMarker(lineChartY)

        val xAxis = lineChartY.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 12
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        val yAxis = lineChartY.axisLeft
        yAxis.textColor = Color.WHITE
        yAxis.setDrawGridLines(true)

        val yAxisRight = lineChartY.axisRight
        yAxisRight.isEnabled = false

        lineChartY.legend.isEnabled = false
        lineChartY.invalidate()
    }
    private fun getPercentageDataY(year: String): ArrayList<Entry> {
        val transactions = db.getTransactionsFilteredY(year)
        val entries = ArrayList<Entry>()

        // Check if there are transactions to display
        if (transactions.isEmpty()) {
            Toast.makeText(this, "No data available for Year: $year.", Toast.LENGTH_LONG).show()
            lineChartY.clear()
            lineChartY.invalidate()

        }else{
            for ((index, transaction) in transactions.withIndex()) {
                val percentage = transaction.percentage.toFloat()
                entries.add(Entry(index.toFloat(), percentage))
            }
        }

        return entries
    }
}

