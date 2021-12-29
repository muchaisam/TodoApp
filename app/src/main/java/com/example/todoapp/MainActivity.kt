package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.sony.fancytoastlib.FancyToast
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    var itemlist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        //greetings stuff
        val greetings = findViewById<TextView>(R.id.greetings)
        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        if (timeOfDay in 0..11) {
            greetings.setText("Good Morning")
        } else if (timeOfDay in 12..15) {
            greetings.setText("Good Afternoon")
        } else if (timeOfDay in 16..23) {
            greetings.setText("Good Evening")
        }

        // Initializing the array lists and the adapter
        val itemlist = arrayListOf<String>()
        val adapter =ArrayAdapter(this,
            android.R.layout.simple_list_item_multiple_choice
            , itemlist)

        loadItems()
        val add = findViewById<Button>(R.id.add)
        val editText = findViewById<EditText>(R.id.editText)
        val listView = findViewById<ListView>(R.id.listView)
        val delete = findViewById<Button>(R.id.delete)
        val clear = findViewById<Button>(R.id.clear)
        // Adding the items to the list when the add button is pressed
        add.setOnClickListener {

            itemlist.add(editText.text.toString())
            listView.adapter =  adapter
            adapter.notifyDataSetChanged()
            // This is because every time when you add the item the input space or the eidt text space will be cleared
            editText.text.clear()
            saveItems()
        }
        // Clearing all the items in the list when the clear button is pressed
        clear.setOnClickListener {

            itemlist.clear()
            adapter.notifyDataSetChanged()
            saveItems()
        }
        // Adding the toast message to the list when an item on the list is pressed
        listView.setOnItemClickListener { adapterView, view, i, l ->
            FancyToast.makeText(this, "You Selected the item --> "+itemlist.get(i), FancyToast.SUCCESS, FancyToast.LENGTH_SHORT, false).show()
        }
        // Selecting and Deleting the items from the list when the delete button is pressed
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
            saveItems()
        }
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
            itemlist = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }
    //save items by reading them into a data file
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), itemlist)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}
