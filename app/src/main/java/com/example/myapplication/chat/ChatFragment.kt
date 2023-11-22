package com.example.myapplication.chat

import android.os.Bundle
import android.view.View
import com.example.myapplication.adapter.ChatAdapter
import com.example.myapplication.ui.base.BaseFragment
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.FragmentChatBinding

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatAdapter=ChatAdapter(requireContext())
        tBinding.rvChats.adapter=chatAdapter

       /* tBinding.btnNext.setOnClickListener {
            CommonMethods.replaceFragmentScreenWithBackStack(
                fragmentManager,
                RoleFragment(),
                R.id.homeContainer
            )
        }
*/
    }

    override fun getLayout(): Int {
        return R.layout.fragment_chat
    }

}