package com.example.lets_to_do

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.lets_to_do.Db.MyDbManager
import com.example.lets_to_do.Db.MyIntentConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class EditActivity : AppCompatActivity() {

    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)
    var id = 0
    var isEditState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val mainImageLayout = findViewById<ConstraintLayout>(R.id.mainImageLayout)
        val back = findViewById<ImageView>(R.id.imButtonBack)
        val fbAdd = findViewById<FloatingActionButton>(R.id.fbAdd)
        val fbDelete = findViewById<FloatingActionButton>(R.id.imButtonDeleteImage)
        val fbSetImage = findViewById<FloatingActionButton>(R.id.fbSetImage)
        val fbSave = findViewById<FloatingActionButton>(R.id.fbSave)
        val edTitle = findViewById<TextView>(R.id.edTitle)
        val edDisc = findViewById<TextView>(R.id.edDisc)

        getMyIntents()

        back.setOnClickListener{
            finish()
        }

        fbAdd.setOnClickListener{
            mainImageLayout.visibility = View.VISIBLE
            fbAdd.visibility = View.GONE
        }

        fbDelete.setOnClickListener{
            mainImageLayout.visibility = View.GONE
            fbAdd.visibility = View.VISIBLE
        }

        fbSetImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }

        fbSave.setOnClickListener{
            var myTitle = edTitle.text.toString()
            var myDisc = edDisc.text.toString()

            if (myTitle != "" && myDisc != ""){
                if(isEditState){
                    myDbManager.updateItem(myTitle, myDisc, tempImageUri, id)
                } else {
                    myDbManager.insertToDb(myTitle, myDisc, tempImageUri)
                }

            }
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            val imMyImage = findViewById<ImageView>(R.id.imMyImage)

            imMyImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun getMyIntents(){
        val i = intent

        if(i != null){
            if(i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){
                val edTitle = findViewById<EditText>(R.id.edTitle)
                val edDesc = findViewById<EditText>(R.id.edDisc)
                val mainImageLayout = findViewById<ConstraintLayout>(R.id.mainImageLayout)
                val fbAdd = findViewById<FloatingActionButton>(R.id.fbAdd)
                val imMyImage = findViewById<ImageView>(R.id.imMyImage)
                val fbEdit = findViewById<FloatingActionButton>(R.id.fbSetImage)
                val fbDelete = findViewById<FloatingActionButton>(R.id.imButtonDeleteImage)
                fbAdd.visibility = View.GONE

                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState = true
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if(i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){
                    mainImageLayout.visibility = View.VISIBLE
                    imMyImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    fbDelete.visibility = View.GONE
                    fbEdit.visibility = View.GONE
                }
            }
        }
    }

}