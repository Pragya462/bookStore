package com.example.bookstore

import android.app.Activity
import android.app.AlertDialog

class loadingClass2(val mActivity: Activity) {
    private lateinit var isdialog : AlertDialog

    fun startLoading()
    {

        val inflater = mActivity.layoutInflater
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(inflater.inflate(R.layout.loading_bar2, null))
        builder.setCancelable(true)

        isdialog = builder.create()
        isdialog.show()
    }
    fun isDismiss()
    {
        isdialog.dismiss()
    }

}