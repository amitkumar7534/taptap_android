package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.adapter.ChatAdapter
import com.example.myapplication.adapter.FollowRequestAdapter
import com.example.myapplication.adapter.SuggestionAdapter
import com.example.myapplication.ui.base.BaseFragment
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.FragmentFollowRequestBinding
import com.yookoo.discoveraddis.databinding.FragmentProfileFragmentBinding


class FollowRequestFragment : BaseFragment<FragmentFollowRequestBinding>() {

    lateinit var followRequestAdapter: FollowRequestAdapter
    lateinit var suggestionAdapter: SuggestionAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_follow_request
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suggestionAdapter=SuggestionAdapter(requireContext())
        tBinding.followRequestRecyclerView?.adapter=suggestionAdapter

        /* tBinding.btnNext.setOnClickListener {
             CommonMethods.replaceFragmentScreenWithBackStack(
                 fragmentManager,
                 RoleFragment(),
                 R.id.homeContainer
             )
         }
 */

        tBinding.ivBack.setOnClickListener {
            finishActivity()
        }

    }

}