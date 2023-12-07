package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.data.ApiInterface
import com.example.myapplication.data.EmployeeData
import com.example.myapplication.data.EmployeeNewRequestData
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.placeholder.PlaceholderContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log


// the fragment initialization parameters, e.g. ARG_EMP_ACTION_TYPE
private const val ARG_EMP_ACTION_TYPE = "ARG_EMP_ACTION_TYPE"
private const val ARG_EMP_POSITION = "ARG_EMP_POSITION"
private const val ARG_EMP_ITEM_DATA = "ARG_EMP_ITEM_DATA"

/**
 * A simple [Fragment] subclass.
 * Use the [EmployeeEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmployeeEditFragment() : Fragment() {
    private var actionType: String? = null // EDIT | NEW
    private var position: Int = -1

    lateinit var empItemData: EmployeeData
    lateinit var rvData: PlaceholderContent

    lateinit var editEmpFirstName : EditText
    lateinit var editEmpSalary : EditText
    lateinit var editEmpAddress : EditText
    lateinit var editEmpSaveBtn : Button
    lateinit var editEmpCancelBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            actionType = it.getString(ARG_EMP_ACTION_TYPE)
            position = it.getInt(ARG_EMP_POSITION)
//            empItemData = it.getSerializable(ARG_EMP_ITEM_DATA)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Employee", "POSITION "+ position)

            if (position > -1)
                empItemData = rvData.getItemData(position)!!


        editEmpFirstName = view.findViewById(R.id.editEmpFirstName)
        editEmpAddress = view.findViewById(R.id.editEmpAddress)
        editEmpSalary = view.findViewById(R.id.editEmpSalary)
        editEmpSaveBtn = view.findViewById(R.id.editEmpSaveBtn)
        editEmpCancelBtn = view.findViewById(R.id.editEmpCancelBtn)

        if (actionType == "EDIT") {
            editEmpFirstName.setText(empItemData!!.firstName)
            editEmpAddress.setText(empItemData!!.address)
            editEmpSalary.setText(empItemData!!.salary)
            editEmpSaveBtn.setText("Update")
        }
        else {
            editEmpSaveBtn.setText("Save")
        }
        editEmpSaveBtn.setOnClickListener(View.OnClickListener {
            Log.d("Employee", "Submit Button Clicked. position: "+ position)

//            empItemData.copy()
            if (actionType == "EDIT") {
                empItemData?.firstName  = editEmpFirstName.getText().toString()
                empItemData?.address  = editEmpAddress.getText().toString()
                empItemData?.salary  = editEmpSalary.getText().toString()
                PlaceholderContent.updateItem(position, empItemData)
                updateEmployee(empItemData)
            } else {
                val empItemData : EmployeeNewRequestData = EmployeeNewRequestData("","","","","","","","","",)
                empItemData?.firstName  = editEmpFirstName.getText().toString()
                empItemData?.address  = editEmpAddress.getText().toString()
                empItemData?.salary  = editEmpSalary.getText().toString()

                saveEmployee(empItemData)
            }
//
//            parentFragmentManager
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment_content_main, EmployeeFragment.newInstance(1,1, position))
//                .commit()
        })

        editEmpCancelBtn.setOnClickListener(View.OnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, EmployeeFragment.newInstance(1,1, position))
                .commit()
        })

    }
    fun goBackToList(){

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, EmployeeFragment.newInstance(1,1, position))
            .commit()
    }
    fun saveEmployee(empItemData: EmployeeNewRequestData) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {

                val response = apiInterface.saveEmployee(empItemData)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Employee: ", response.body().toString())

                    val emp: EmployeeData? = response.body()?.data
                    if (emp != null) {
                        PlaceholderContent.addItem(emp)
                    }
                    goBackToList()
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

    fun updateEmployee(empItemData: EmployeeData) {

        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id: String = if (empItemData._id.isNotEmpty()) empItemData._id else (if ( empItemData.id.isNotEmpty()) empItemData.id else "" )

                val response = apiInterface.updateEmployee(id, empItemData)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Employee: ", response.body().toString())

                    val emp: EmployeeData? = response.body()?.data
                    if (emp != null) {
//                        PlaceholderContent.addItem(emp)
                    }
                    goBackToList()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param actionType Parameter 1.
         * @param position Parameter 2.
         * @return A new instance of fragment EmployeeEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(actionType: String, position: Int) =
            EmployeeEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMP_ACTION_TYPE, actionType)
                    putInt(ARG_EMP_POSITION, position)
                }
            }
    }
}