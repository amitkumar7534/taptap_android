package com.example.myapplication.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.api.response.CarData
import com.example.myapplication.ui.base.BindingActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityCarDetailBinding

class CarDetailActivity : BindingActivity<ActivityCarDetailBinding>() {
    lateinit var data: CarData

    override fun getLayout(): Int {
        return R.layout.activity_car_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = intent.getSerializableExtra("data") as CarData


        Glide.with(this)
            .load(data.vehicle_photo).placeholder(R.drawable.discoveraddislogo_new)
            .into(viewBinding.ivBusinessImage)


        viewBinding.businessNameOnDetail.setText(data.vehicle_name.toString())
        viewBinding.carOwnerName.setText(data.owner_name.toString())
        viewBinding.ownerPhoneNumber.setText(data.owner_phone.toString())
        viewBinding.vehicleCode.setText(data.vehicle_code.toString())
        viewBinding.chassieNumber.setText(data.chasis_number.toString())
        viewBinding.plateNumber.setText(data.plate_number.toString())
        viewBinding.insure.setText(data.insurer.toString())
        viewBinding.insureName.setText(data.insurance_number.toString())
        viewBinding.insureType.setText(data.insurance_type.toString())
        viewBinding.insureDate.setText(data.insured_date.toString())


        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.btnBackTomain.setOnClickListener {
            finish()
        }

    }


}