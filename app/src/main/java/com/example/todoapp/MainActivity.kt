package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listofTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
        override fun onItemLongClicked(position: Int) {
            //1. Remove item from the list
            listofTasks.removeAt(position)
            //2. Notify the adapter that data has changed
            adapter.notifyDataSetChanged()

            saveItems()
        }

    }
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        // Initialize contacts
        adapter = TaskItemAdapter(listofTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputField = findViewById<EditText>(R.id.editing)

        //setup the button and inut field so that the user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab the text a user has input to the field
            val userInputtedTask = inputField.text.toString()
            //2. Add the string to our list of tasks
            listofTasks.add(userInputtedTask)
            //notify the adapter
            adapter.notifyItemInserted(listofTasks.size - 1)
            //3. reset the text field
            inputField.setText("")
        }

        saveItems()
    }

    //save the data that the user has input
    //save data by writing and reading from a file
    //create a method to get the file we need
    fun getDataFile() : File{
        //every line represents a task in the list
        return File(filesDir, "data.txt")
    }

    //load the items by reading every line of the data file
    fun loadItems(){
        try {
            listofTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }
    //save items by reading them into a data file
    fun saveItems(){
       try {
           org.apache.commons.io.FileUtils.writeLines(getDataFile(), listofTasks)
       }catch (ioException: IOException){
           ioException.printStackTrace()
       }
    }

}