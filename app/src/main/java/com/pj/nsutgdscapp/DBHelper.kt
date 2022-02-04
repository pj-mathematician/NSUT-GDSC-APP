package com.pj.nsutgdscapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION) {
    override fun onCreate(db:SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                ITEM_NAME_COL + " TEXT, " +
                ITEM_TIME_COL + " TEXT," +
                ITEM_PRICE_COL + " TEXT" + ")")
        db.execSQL(query)
    }
    override fun onUpgrade(db: SQLiteDatabase, p1:Int, p2:Int) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME)
        onCreate(db)
    }
    fun addItem(itemname : String, itemprice : String, itemtime : String= SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
        Date()
    )){
     val values = ContentValues()
     values.put(ITEM_NAME_COL, itemname)
     values.put(ITEM_PRICE_COL, itemprice)
     values.put(ITEM_TIME_COL, itemtime)
     val db = this.writableDatabase
     db.insert(TABLE_NAME, null, values)
     db.close()
    }
    fun getItem(): Cursor? {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM " + TABLE_NAME,null)

        return result
    }
    fun delItem(itemname: String, itemprice: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, ITEM_NAME_COL + " = \"" + itemname +"\"" + " and " + ITEM_PRICE_COL + " = " + itemprice,null)
        db.close()
        return result
    }
    fun editItem(itemname: String,itemprice: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ITEM_PRICE_COL,itemprice)
        val result = db.update(TABLE_NAME,values, ITEM_NAME_COL + " = \"" + itemname +"\"",null)
        db.close()
        return result
    }
    fun checkexists(itemname: String): Boolean {
        val query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + ITEM_NAME_COL + " = \"" + itemname +"\""
        val db = this.writableDatabase
        val cur = db.rawQuery(query,null)
        if (cur.count <= 0){
            cur.close()
            db.close()
            return false
        }
        cur.close()
        db.close()
        return true
    }
    companion object {
        private val DATABASE_NAME = "NSUT GDSC APP"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "items"
        val ID_COL = "id"
        val ITEM_NAME_COL = "item_name"
        val ITEM_PRICE_COL = "item_price"
        val ITEM_TIME_COL = "item_time"
    }
}