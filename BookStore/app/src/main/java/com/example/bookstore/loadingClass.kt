package com.example.bookstore

import android.app.Activity
import android.app.AlertDialog

class loadingClass(val mActivity: Activity) {
    private lateinit var isdialog : AlertDialog

    fun startLoading()
    {

        val inflater = mActivity.layoutInflater
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(inflater.inflate(R.layout.loading_bar, null))

        builder.setCancelable(true)

        isdialog = builder.create()
        isdialog.show()
    }
    fun isDismiss()
    {
        isdialog.dismiss()
    }

}
