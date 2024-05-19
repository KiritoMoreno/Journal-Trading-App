package com.example.tradingjournalpruebafinal.Journal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tradingjournalpruebafinal.Data.Transaction
import com.example.tradingjournalpruebafinal.R


class TransactionAdapter(private var transactions:List<Transaction>): RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {


    private var itemClickListener: ((Transaction) -> Unit)? = null

    fun setData(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Transaction) -> Unit) {
        itemClickListener = listener
    }
    // Asocia cada elemento de datos con una vista.
    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        // combined viewholder is called to associate viewholder with data instance and fill each field.
        val transaction: Transaction = transactions[position]
        val context: Context = holder.amount.context

        // Configura el texto y el color del texto del TextView `amount` dependiendo de si el porcentaje es positivo o negativo.
        if(transaction.percentage >= 0){
            holder.amount.text= "+ %.2f".format(transaction.percentage)
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green))
        }else{
            holder.amount.text= "- %.2f".format(Math.abs(transaction.percentage))
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.red))
        }
        holder.label.text = transaction.instrument

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(transaction)
        }
    }

    // ViewHolder que contiene las vistas donde se mostrarán los datos de las transacciones.
    class TransactionHolder(view: View): RecyclerView.ViewHolder(view){
        val label : TextView = view.findViewById(R.id.instrumentLabel)
        val amount : TextView = view.findViewById(R.id.percentageAmount)

    }
    // Crea nuevas vistas (invocado por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        // Aquí se infla el layout para cada elemento de la lista.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent, false)
        return TransactionHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}