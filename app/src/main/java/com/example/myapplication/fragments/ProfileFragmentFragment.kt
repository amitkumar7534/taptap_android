package com.example.myapplication.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.example.myapplication.activities.CommonActivity
import com.example.myapplication.adapter.OwnPostAdapter
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.login.LoginActivity
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.FragmentProfileFragmentBinding


class ProfileFragmentFragment : BaseFragment<FragmentProfileFragmentBinding>() {

    override fun getLayout(): Int {
        return R.layout.fragment_profile_fragment
    }

    lateinit var imagesAdapter: OwnPostAdapter

    var private_mode: String ="no"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tBinding.ivLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout !")
                .setMessage("Are you sure you want to logout on this app?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        logout()
                        appSession().save("login_value", false)
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finishActivity()
                    })
                .setNegativeButton("No", null)
                .show()
        }

        tBinding.setting.setOnClickListener {
            SettingSheet(private_mode)
//            replaceFragment(R.id.container,SettingActivity())
        }

        getProfile(getBaseActivity().appSession().getString(Constant.USER_ID).toString())

    }


    fun SettingSheet(switch_type: String){

        val dialog = BottomSheetDialog(requireActivity())
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.setting_bottomsheet, null)
        val btnClose = view.findViewById<TextView>(R.id.delete_sheet)
        val rl_setting = view.findViewById<RelativeLayout>(R.id.rl_setting)
        val switch_private = view.findViewById<Switch>(R.id.switch_private)
        rl_setting.setOnClickListener {
            val i = Intent(requireActivity(), CommonActivity::class.java)
            i.putExtra(getString(R.string.fragmentName), getString(R.string.setting_fragment))
            startActivity(i)
            dialog.dismiss()
        }

        if (switch_type.equals("yes")){
            switch_private.isChecked = true
        }else{
            switch_private.isChecked = false
        }

        switch_private.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            if (isChecked){
                private_mode("yes")
            }else{
                private_mode("no")
            }
        })

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()

    }

    fun getProfile(id:String){
        getBaseActivity().baseViewModel.repository.getProfile(getBaseActivity().apiListener(),id,id).observe(viewLifecycleOwner) {
            if (it.posts.isEmpty()){
//              private_mode = it.data.private_mode.toString()
                tBinding.tvName.text=it.data.full_name
                tBinding.tvPostCount.text=it.post_count.toString()
                tBinding.tvProductCount.text=it.product_count.toString()
                tBinding.tvName.text=it.data.full_name
                if (!it.data.profile_pic.isNullOrEmpty()) {
                    loadGlideInCircle(
                        it.data.profile_pic,
                        tBinding.userImage,
                        R.drawable.iv_default_user
                    )
                }
                tBinding.tvNoData.visibility=View.VISIBLE
                tBinding.rvGallery.visibility=View.GONE
            }else{
//                private_mode = it.data.private_mode.toString()

                tBinding.tvNoData.visibility=View.GONE
                tBinding.rvGallery.visibility=View.VISIBLE
                tBinding.tvName.text=it.data.full_name
                tBinding.tvPostCount.text=it.post_count.toString()
                tBinding.tvProductCount.text=it.product_count.toString()
                if (!it.data.profile_pic.isNullOrEmpty()) {
                    loadGlideInCircle(
                        it.data.profile_pic,
                        tBinding.userImage,
                        R.drawable.iv_default_user
                    )
                }
                imagesAdapter = OwnPostAdapter(requireContext(),it.posts as ArrayList<Data>,appSession().getString(Constant.USER_ID).toString())
                tBinding!!.rvGallery.adapter = imagesAdapter
            }
        }
    }

    fun private_mode(id:String){
        getBaseActivity().baseViewModel.repository.PrivateAccount(getBaseActivity().apiListener(),getBaseActivity().appSession().getString(Constant.USER_ID).toString(),id).observe(requireActivity()) {
            if (it.success!!){
                it.message?.let { it1 -> showToast(it1) }
            }
        }
    }


    fun logout(){
        getBaseActivity().baseViewModel.repository.logout(getBaseActivity().apiListener(),getBaseActivity().appSession().getString(Constant.USER_ID).toString()).observe(requireActivity()) {
            if (it.success){
                showToast(it.message)
            }
        }
    }


}