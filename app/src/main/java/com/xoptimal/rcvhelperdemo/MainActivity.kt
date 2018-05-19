package com.xoptimal.rcvhelperdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.xoptimal.rcvhelper.RcvHelper
import com.xoptimal.rcvhelper.viewholder.BaseViewHolder
import com.xoptimal.rcvhelper.viewholder.ExViewBinder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mRcvHelper: RcvHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mRcvHelper = RcvHelper.Builder(rcv).setSmartLoadMore(true).create()
        mRcvHelper.register(String::class.java, object : ExViewBinder<String>() {
            override fun getLayoutId(): Int {
                return R.layout.item_view
            }

            override fun onBindViewHolder(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_value, item)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_add -> mRcvHelper.add(Date().time.toString())
            R.id.item_remove -> mRcvHelper.remove(mRcvHelper.size - 1)
            R.id.item_replaceAll -> {
                mRcvHelper.replaceAll(
                        listOf(Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString()))
            }
            R.id.item_addAll -> {
                mRcvHelper.addAll(
                        listOf(Date().time.toString(),
                                Date().time.toString(),
                                Date().time.toString()))

            }
            R.id.item_clear -> mRcvHelper.clear()
        }

        return super.onOptionsItemSelected(item)
    }

}
