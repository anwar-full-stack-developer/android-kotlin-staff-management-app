package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EmployeeRecyclerViewAdapter.OnClickListener
import com.example.myapplication.data.ApiInterface
import com.example.myapplication.data.EmployeeData
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.placeholder.PlaceholderContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A fragment representing a list of Items.
 */
class EmployeeFragment : Fragment() {

    private var columnCount = 1
    private var editBack = 0
    private var editPosition = 0

    lateinit var rvAdapter: EmployeeRecyclerViewAdapter
    lateinit var rv: RecyclerView
    lateinit var rvData: PlaceholderContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            editBack = it.getInt(ARG_EDIT_BACK)
            editPosition = it.getInt(ARG_EDIT_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_employee_list, container, false)

        rv = view.findViewById(R.id.listEmp)
        // Set the adapter
        if (rv is RecyclerView) {
            with(rv) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = EmployeeRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
                rvAdapter = rv.adapter as EmployeeRecyclerViewAdapter

                // Applying OnClickListener to our Adapter
                rvAdapter.setOnClickListener(object : OnClickListener {
                    override fun onClick(position: Int, model: EmployeeData) {
                        val empItemData: EmployeeData? = rvData.getItemData(position)
                        Log.d("Employee", "Item on clicked")
                    }

                    override fun onLongClick(
                        position: Int,
                        model: PlaceholderContent.PlaceholderItem
                    ) {
//                        TODO("Not yet implemented")
                    }

                    override fun onClickToEdit(
                        position: Int,
                        model: EmployeeData
                    ) {
                        val empItemData: EmployeeData? = rvData.getItemData(position)
                        val actionType = "EDIT" // EDIT | NEW

                        Log.d("Employee", "Emp list EDIT btn clicked on clicked")
                        Log.d("Employee", empItemData.toString())
                        val fr = parentFragmentManager.beginTransaction()
                        fr.replace(R.id.nav_host_fragment_content_main, EmployeeEditFragment.newInstance(
                            actionType,
                            position
                        ))
                        fr.commit()
                    }

                    override fun onClickToDelete(
                        position: Int,
                        model: EmployeeData
                    ) {
                        val empItemData: EmployeeData? = rvData.getItemData(position)
                        rvData.removeItem(position)
                        rvAdapter.submitList(PlaceholderContent.ITEMS)
//                        rvAdapter.notifyDataSetChanged()
//                        TODO: server-side api implementation
                        if (empItemData != null) {
                            deleteEmployee(empItemData)
                        }
                    }
                })
                rvAdapter.notifyDataSetChanged()
            }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEmployeeList()
//        rvAdapter.notifyDataSetChanged()
        Log.i("Employee", "Adapter Created")
        Log.i("Employee", "Adapter Created, view Created")
//        Log.i("EmpAdapter", "Adapter Data Size: " + rvAdapter.getItemCount().toString())
    }


    private fun notifyDataChanged(){

        rvAdapter.submitList(PlaceholderContent.ITEMS)
        Log.d("Employee: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
        rvAdapter.notifyDataSetChanged()
        Log.d("Employee: ", "Adapter List Count: = " + rvAdapter.getItemCount().toString())
    }
    private fun getEmployeeList() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val response = apiInterface.getAllEmployee()
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Employee: ", response.body().toString())

                    val empList: List<EmployeeData>? = response.body()?.data
                    if (empList != null) {
                        PlaceholderContent.clearAll()
                        for (i in 0.. empList.size - 1) {
                            PlaceholderContent.addItem(empList[i])
                        }
//                        if (! rv.isComputingLayout())
//                        rvAdapter.submitList(PlaceholderContent.ITEMS)
                        notifyDataChanged()
//                        Log.d("Employee: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
//                        rvAdapter.notifyDataSetChanged()
//                        rvAdapter.notifyAll()
                    }


                } else {
                    Log.e("Error", response.errorBody().toString())
                    Toast.makeText(
                        this@EmployeeFragment.context,
                        response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    fun deleteEmployee(empItemData: EmployeeData) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id: String = if (empItemData._id.isNotEmpty()) empItemData._id else (if ( empItemData.id.isNotEmpty()) empItemData.id else "" )
                val response = apiInterface.deleteEmployee(id)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Employee: ", response.body().toString())

//                    val emp: EmployeeData? = response.body()?.data
//                    if (emp != null) {
////                        PlaceholderContent.removeItem(position)
//                    }

                } else {
                    Log.e("Error", response.errorBody().toString())
//                    Toast.makeText(
//                        this@EmployeeEditFragment.context,
//                        response.errorBody().toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_EDIT_BACK = "edit-back"
        const val ARG_EDIT_POSITION = "edit-position"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, editBack: Int, editPosition: Int) =
            EmployeeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_EDIT_BACK, editBack)
                    putInt(ARG_EDIT_POSITION, editPosition)
                }
            }
    }
}

