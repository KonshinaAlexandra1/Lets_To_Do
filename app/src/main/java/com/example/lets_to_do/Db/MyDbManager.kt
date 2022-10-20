package com.example.lets_to_do.Db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class MyDbManager(val context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }

    fun insertToDb(name_title:String, name_content:String, uri:String){
        val values = ContentValues().apply {

            put(DbConst.COLUMN_NAME_TITLE, name_title)
            put(DbConst.COLUMN_NAME_CONTENT, name_content)
            put(DbConst.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(DbConst.TABLE_NAME, null, values)

    }

    fun updateItem(name_title:String, name_content:String, uri:String, id:Int){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {

            put(DbConst.COLUMN_NAME_TITLE, name_title)
            put(DbConst.COLUMN_NAME_CONTENT, name_content)
            put(DbConst.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.update(DbConst.TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id:String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(DbConst.TABLE_NAME, selection, null)
    }

    @SuppressLint("Range")
    fun readDbData():ArrayList<ListItem>{
        val dataList = ArrayList<ListItem>()
        val cursor = db?.query(DbConst.TABLE_NAME, null, null, null, null, null, null)
        with(cursor){
            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndex(DbConst.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndex(DbConst.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndex(DbConst.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))

                val item = ListItem()
                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri
                item.id = dataId

                dataList.add(item)
                }
        }
        cursor?.close()
        return dataList
    }

    fun closeDb(){
        myDbHelper.close()
    }
}