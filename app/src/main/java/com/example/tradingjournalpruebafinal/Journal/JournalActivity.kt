package com.example.tradingjournalpruebafinal.Journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradingjournalpruebafinal.Data.MyDatabaseHelper
import com.example.tradingjournalpruebafinal.Data.Transaction
import com.example.tradingjournalpruebafinal.HomeActivity
import com.example.tradingjournalpruebafinal.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JournalActivity : AppCompatActivity() {
    private lateinit var db :MyDatabaseHelper

    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions : List<Transaction>
    private lateinit var oldTransactions: List<Transaction>
    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager

    //Database class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        db = MyDatabaseHelper(this)


        transactions = emptyList()
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = transactionAdapter

        // Agregar un oyente al adaptador para manejar los clicks en los elementos del RecyclerView
        transactionAdapter.setOnItemClickListener { transaction ->
            navigateToDetailedActivity(transaction.id)
        }

        // Implementa deslizar para eliminar
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean{
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)

        val btnDashBoard : ImageButton = findViewById(R.id.btnDashBoard)
        btnDashBoard.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val addBtn: FloatingActionButton = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            //Inten is a description of an action that want the system to perform
            //It will need two parameters ( Start activity and End activity)
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }

    // Navega a una actividad detallada pasando el ID de la transacción
    private fun navigateToDetailedActivity(transactionId: Int) {
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra("transaction_id", transactionId)
        startActivity(intent)
    }
    // Recupera todas las transacciones de la base de datos y actualiza la interfaz
    private fun fetchAll() {
        GlobalScope.launch(Dispatchers.Main) {
            val transactionsFromDb = db.getAllTransactions()
            transactions = transactionsFromDb
            transactionAdapter.setData(transactions)
            updateDashboard()
        }
    }

    // Actualiza el tablero con el resumen de ganancias y pérdidas
    private fun updateDashboard(){
        val totalAmount= transactions.map{it.percentage}.sum()
        val percentageWin= transactions.filter { it.percentage>0 }.map { it.percentage }.sum()
        val percentageLoss= totalAmount - percentageWin

        val balance: TextView = findViewById(R.id.balance)
        val win: TextView = findViewById(R.id.tvWin)
        val loss: TextView = findViewById(R.id.tvLoss)


        balance.text = "% "+"%.2f".format(totalAmount)
        win.text= "% "+"%.2f".format(percentageWin)
        loss.text="% "+"%.2f".format(percentageLoss)

    }

    // Deshace la eliminación de una transacción y actualiza la interfaz
    private fun undoDelete(){
        GlobalScope.launch {
            db.insert(deletedTransaction)
            transactions = oldTransactions
            runOnUiThread {
                transactionAdapter.setData(transactions)
                showSnackbar()
                updateDashboard()
            }
        }
    }

    // Muestra un Snackbar con la opción de deshacer la eliminación
    private fun showSnackbar(){
        val view = this.findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view,"Trasaction deleted!", Snackbar.LENGTH_LONG)
        snackbar.setAction("undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.background_button))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()

    }
    // Elimina una transacción de la base de datos y actualiza la interfaz
    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.delete(transaction)
            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
                showSnackbar()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}