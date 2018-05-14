package com.xoptimal.rcvhelperdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xoptimal.rcvhelper.RcvHelper
import com.xoptimal.rcvhelper.viewholder.BaseViewHolder
import com.xoptimal.rcvhelper.viewholder.ExViewBinder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mRcvHelper : RcvHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRcvHelper = RcvHelper.Builder(rcv).create()
        mRcvHelper.register(String::class.java, object :ExViewBinder<String>(){
            override fun getLayoutId(): Int {
                return R.layout.item_view
            }

            override fun onBindViewHolder(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_value, item)
            }
        })
    }
}
