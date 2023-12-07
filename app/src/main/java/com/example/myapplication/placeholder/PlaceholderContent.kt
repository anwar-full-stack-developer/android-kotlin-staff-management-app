package com.example.myapplication.placeholder

import android.util.Log
import com.example.myapplication.data.EmployeeData
import java.util.ArrayList
import kotlin.collections.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    // Original data map
    var OD_ITEM_MAP: MutableMap<String, EmployeeData> = HashMap()

    var ITEMS: ArrayList<EmployeeData> = ArrayList()


    private var nextPosition = 0;

    public val COUNT = ITEMS.size

    init {
//         Add some sample items.
//        for (i in 1..COUNT) {
//            addItem(createPlaceholderItem(i))
//        }
    }

    public fun clearAll() {
        nextPosition=0
        OD_ITEM_MAP = HashMap()
        ITEMS = ArrayList()
    }

    public fun addItem(item: EmployeeData) {
        val position = nextPosition
//        val cpi = createPlaceholderItem(position, item)
        Log.d("Employee", "Position: $position")
        Log.d("Employee", item.toString())
        OD_ITEM_MAP.put(position.toString(), item)
        ITEMS.add(item)
        nextPosition++
    }

    public fun updateItem(position: Int, item: EmployeeData) {
        val cpi = createPlaceholderItem(position, item)
        ITEMS.removeAt(position)
        ITEMS.add(position, item)
        OD_ITEM_MAP.put(position.toString(), item)
    }

    fun getItemData(position: Int) : EmployeeData? {
        return OD_ITEM_MAP.get(position.toString());
    }
    fun removeItem(position: Int) : Boolean{
        ITEMS.removeAt(position);
        OD_ITEM_MAP.remove(position.toString())
        return true
    }

    public fun createPlaceholderItem(position: Int, item: EmployeeData): PlaceholderItem {
        return PlaceholderItem((position + 1).toString(), item.firstName, makeDetails(item))
    }

     fun makeDetails(item: EmployeeData): String {
        val builder = StringBuilder()
        builder.append("ID: ").append(item.id)
        builder.append("\nSalary: ").append(item.salary)
        builder.append("\nAddress: ").append(item.address)
        builder.append("\nCountry: ").append(item.country)
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}