package com.easyjob.ui.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ui.base.BindingActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityMessagesBinding

class MessagesActivity : BindingActivity<ActivityMessagesBinding>(){

    var userlistAdapter: AdapterMessages? = null

    override fun getLayout(): Int {
        return R.layout.activity_messages
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMessageList()

    }

    fun setMessageList(){
        var HorizontalLayout: LinearLayoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)
        viewBinding.userRecyclerView.setLayoutManager(HorizontalLayout)
        userlistAdapter = AdapterMessages(this)
        viewBinding.userRecyclerView.adapter = userlistAdapter
    }


}