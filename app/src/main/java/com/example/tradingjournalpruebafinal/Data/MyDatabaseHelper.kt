package com.example.tradingjournalpruebafinal.Data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Transactions.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allTransactions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_INSTRUMENT = "instrument"
        private const val COLUMN_DAY = "day"
        private const val COLUMN_MONTH = "month"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_STRATEGY = "strategy"
        private const val COLUMN_ENTRYPRICE = "entryPrice"
        private const val COLUMN_STOPLOSS = "stopLoss"
        private const val COLUMN_TAKEPROFIT = "takeProfit"
        private const val COLUMN_RISK = "risk"
        private const val COLUMN_WIN_LOSS = "win_loss"
        private const val COLUMN_PERCENTAGE = "percentage"
        private const val COLUMN_EMOTION = "emotion"
        private const val COLUMN_SESSION = "session"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_INSTRUMENT TEXT, $COLUMN_DAY TEXT, $COLUMN_MONTH TEXT, $COLUMN_YEAR TEXT, $COLUMN_STRATEGY TEXT, $COLUMN_ENTRYPRICE REAL, $COLUMN_STOPLOSS REAL, $COLUMN_TAKEPROFIT REAL, $COLUMN_RISK REAL, $COLUMN_WIN_LOSS TEXT, $COLUMN_PERCENTAGE REAL, $COLUMN_EMOTION TEXT, $COLUMN_SESSION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insert(newTransaction: Transaction): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMN_INSTRUMENT, newTransaction.instrument)
                put(COLUMN_DAY, newTransaction.day)
                put(COLUMN_MONTH, newTransaction.month)
                put(COLUMN_YEAR, newTransaction.year)
                put(COLUMN_STRATEGY, newTransaction.strategy)
                put(COLUMN_ENTRYPRICE, newTransaction.entryPrice)
                put(COLUMN_STOPLOSS, newTransaction.stopLoss)
                put(COLUMN_TAKEPROFIT, newTransaction.takeProfit)
                put(COLUMN_RISK, newTransaction.risk)
                put(COLUMN_WIN_LOSS, newTransaction.win_loss)
                put(COLUMN_PERCENTAGE, newTransaction.percentage)
                put(COLUMN_EMOTION, newTransaction.emotion)
                put(COLUMN_SESSION, newTransaction.session)
            }
            val result = db.insert(TABLE_NAME, null, values)
            db.close()
            result != -1L
        } catch (e: Exception) {
            Log.e("Database Insert", "Error inserting data", e)
            false
        }
    }

    fun update(transaction: Transaction): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INSTRUMENT, transaction.instrument)
            put(COLUMN_DAY, transaction.day)
            put(COLUMN_MONTH, transaction.month)
            put(COLUMN_YEAR, transaction.year)
            put(COLUMN_STRATEGY, transaction.strategy)
            put(COLUMN_ENTRYPRICE, transaction.entryPrice)
            put(COLUMN_STOPLOSS, transaction.stopLoss)
            put(COLUMN_TAKEPROFIT, transaction.takeProfit)
            put(COLUMN_RISK, transaction.risk)
            put(COLUMN_WIN_LOSS, transaction.win_loss)
            put(COLUMN_PERCENTAGE, transaction.percentage)
            put(COLUMN_EMOTION, transaction.emotion)
            put(COLUMN_SESSION, transaction.session)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(transaction.id.toString())
        val rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
        return rowsUpdated > 0
    }

    @SuppressLint("Range")
    fun getTransactionByID(transactionId: Int): Transaction {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $transactionId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        val instrument = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUMENT))
        val day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY))
        val month = cursor.getString(cursor.getColumnIndex(COLUMN_MONTH))
        val year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR))
        val strategy = cursor.getString(cursor.getColumnIndex(COLUMN_STRATEGY))
        val entryPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_ENTRYPRICE))
        val stopLoss = cursor.getDouble(cursor.getColumnIndex(COLUMN_STOPLOSS))
        val takeProfit = cursor.getDouble(cursor.getColumnIndex(COLUMN_TAKEPROFIT))
        val risk = cursor.getDouble(cursor.getColumnIndex(COLUMN_RISK))
        val win_Loss = cursor.getString(cursor.getColumnIndex(COLUMN_WIN_LOSS))
        val percentage = cursor.getDouble(cursor.getColumnIndex(COLUMN_PERCENTAGE))
        val emotion = cursor.getString(cursor.getColumnIndex(COLUMN_EMOTION))
        val session = cursor.getString(cursor.getColumnIndex(COLUMN_SESSION))
        cursor.close()
        db.close()
        return Transaction(id, instrument, day,month,year, strategy, entryPrice, stopLoss, takeProfit, risk, win_Loss, percentage, emotion,session)
    }

    @SuppressLint("Range")
    fun getAllTransactions(): List<Transaction> {
        val transactionsList = mutableListOf<Transaction>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(COLUMN_ID))
                val instrument = it.getString(it.getColumnIndex(COLUMN_INSTRUMENT))
                val day = it.getString(it.getColumnIndex(COLUMN_DAY))
                val month = it.getString(it.getColumnIndex(COLUMN_MONTH))
                val year = it.getString(it.getColumnIndex(COLUMN_YEAR))
                val strategy = it.getString(it.getColumnIndex(COLUMN_STRATEGY))
                val entryPrice = it.getDouble(it.getColumnIndex(COLUMN_ENTRYPRICE))
                val stopLoss = it.getDouble(it.getColumnIndex(COLUMN_STOPLOSS))
                val takeProfit = it.getDouble(it.getColumnIndex(COLUMN_TAKEPROFIT))
                val risk = it.getDouble(it.getColumnIndex(COLUMN_RISK))
                val winLoss = it.getString(it.getColumnIndex(COLUMN_WIN_LOSS))
                val percentage = it.getDouble(it.getColumnIndex(COLUMN_PERCENTAGE))
                val emotion = it.getString(it.getColumnIndex(COLUMN_EMOTION))
                val session = it.getString(it.getColumnIndex(COLUMN_SESSION))
                val transaction = Transaction(id, instrument, day,month,year, strategy, entryPrice, stopLoss, takeProfit, risk, winLoss, percentage, emotion,session)
                transactionsList.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactionsList
    }

    fun delete(transaction: Transaction): Int {
        val db = writableDatabase
        val affectedRows = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(transaction.id.toString()))
        db.close()
        return affectedRows
    }

    fun getTransactionsFiltered(year: String, month: String, strategy: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_MONTH = ? AND $COLUMN_STRATEGY = ?", arrayOf(year, month, strategy))
    }

    fun getTransactionsFilteredYM(year: String, month: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_MONTH = ?", arrayOf(year, month))
    }

    fun getTransactionsFilteredY(year: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ?", arrayOf(year))
    }

    fun getTransactionsFilteredStrategy(strategy: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_STRATEGY = ?", arrayOf(strategy))
    }

    fun getTransactionsFilteredYSIES(year: String, session: String, instrument: String, emotion: String, strategy: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_SESSION = ? AND $COLUMN_INSTRUMENT = ? AND $COLUMN_EMOTION = ? AND $COLUMN_STRATEGY = ?", arrayOf(year, session, instrument, emotion, strategy))
    }

    fun getTransactionsFilteredYSIE(year: String, session: String, instrument: String, emotion: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_SESSION = ? AND $COLUMN_INSTRUMENT = ? AND $COLUMN_EMOTION = ?", arrayOf(year, session, instrument, emotion))
    }

    fun getTransactionsFilteredYSI(year: String, session: String, instrument: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_SESSION = ? AND $COLUMN_INSTRUMENT = ?", arrayOf(year, session, instrument))
    }

    fun getTransactionsFilteredYS(year: String, session: String): List<Transaction> {
        return getFilteredTransactions("$COLUMN_YEAR = ? AND $COLUMN_SESSION = ?", arrayOf(year, session))
    }

    private fun getFilteredTransactions(selection: String, selectionArgs: Array<String>): List<Transaction> {
        val db = readableDatabase
        return db.use { database ->
            val cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
            mutableListOf<Transaction>().apply {
                while (cursor.moveToNext()) {
                    add(createTransactionFromCursor(cursor))
                }
                cursor.close()
            }
        }
    }


    //----
    @SuppressLint("Range")
    private fun createTransactionFromCursor(cursor: Cursor): Transaction {
        return Transaction(
            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUMENT)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DAY)),
            cursor.getString(cursor.getColumnIndex(COLUMN_MONTH)),
            cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)),
            cursor.getString(cursor.getColumnIndex(COLUMN_STRATEGY)),
            cursor.getDouble(cursor.getColumnIndex(COLUMN_ENTRYPRICE)),
            cursor.getDouble(cursor.getColumnIndex(COLUMN_STOPLOSS)),
            cursor.getDouble(cursor.getColumnIndex(COLUMN_TAKEPROFIT)),
            cursor.getDouble(cursor.getColumnIndex(COLUMN_RISK)),
            cursor.getString(cursor.getColumnIndex(COLUMN_WIN_LOSS)),
            cursor.getDouble(cursor.getColumnIndex(COLUMN_PERCENTAGE)),
            cursor.getString(cursor.getColumnIndex(COLUMN_EMOTION)),
            cursor.getString(cursor.getColumnIndex(COLUMN_SESSION))
        )
    }


}