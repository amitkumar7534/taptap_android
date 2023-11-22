package com.example.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.myapplication.adapter.CategoryAdapter
import com.example.myapplication.api.response.CategoryResponse
import com.yookoo.discoveraddis.databinding.ActivityBusinessCategoriesBinding
import com.example.myapplication.ui.base.BaseFragment
import com.yookoo.discoveraddis.R

class CategoryFragment : BaseFragment<ActivityBusinessCategoriesBinding>() {
    override fun getLayout(): Int {
        return R.layout.activity_business_categories
    }

    var categoryList=ArrayList<CategoryResponse>()
    lateinit var categoryAdapter: CategoryAdapter

    private val businesscategoryImages = arrayOf(
        R.drawable.hotel,
        R.drawable.ic_restaurant,
        R.drawable.ic_shop,
        R.drawable.ic_hair_cut,
        R.drawable.dealership,
        R.drawable.bank,
        R.drawable.ic_museums,
        R.drawable.park,
        R.drawable.momnutem,
        R.drawable.content_producation,
        R.drawable.ic_mnu_delivery,
        R.drawable.therapy,
        R.drawable.call_service,
        R.drawable.stationary,
        R.drawable.repair_services,
        R.drawable.cupcake,
        R.drawable.bakery_shop,
        R.drawable.house,
        R.drawable.ic_business_b_icon,
        R.drawable.bullhorn,
        R.drawable.minerals,
        R.drawable.logistics,
        R.drawable.dumbbell,
        R.drawable.bnb2,
        R.drawable.agent,
        R.drawable.ic_airport,
        R.drawable.ic_fast_food,
        R.drawable.data_management,
        R.drawable.ic_shop,
        R.drawable.ic_shop,
        R.drawable.tele_advisor,
        R.drawable.ic_shopping,
        R.drawable.gallery,
        R.drawable.hotel,
        R.drawable.bakery_shop,
        R.drawable.coffee,
        R.drawable.butchery,
        R.drawable.ic_mall_new,
        R.drawable.skyline,
        R.drawable.ic_atm,
        R.drawable.currency_exchange,
        R.drawable.ic_event,
        R.drawable.ic_salon_menu,
        R.drawable.hospital,
        R.drawable.clinic,
        R.drawable.ic_pharmacy,
        R.drawable.cash_sync,
        R.drawable.bus_station,
        R.drawable.house,
        R.drawable.ic_market_place,
        R.drawable.factory,
        R.drawable.ic_mnu_delivery,
        R.drawable.lawyer,
        R.drawable.consultation,
        R.drawable.bakery_shop,
        R.drawable.supermarket,
        R.drawable.ic_market_place,
        R.drawable.market,
        R.drawable.spare_parts,
        R.drawable.support,
        R.drawable.market,
        R.drawable.store,
        R.drawable.interior_design,
        R.drawable.fashion_designer,
        R.drawable.content,
        R.drawable.social_post,
        R.drawable.shelf,
        R.drawable.photo_shop,
        R.drawable.ic_video_camera,
        R.drawable.photo_shop,
        R.drawable.ic_car_rent,
        R.drawable.ic_post_office,
        R.drawable.ic_tour_agents,
        R.drawable.ic_laundry,
        R.drawable.ic_market_place,
        R.drawable.book_plus,
        R.drawable.furniture_store,
        R.drawable.ic_church,
        R.drawable.ic_mosque,
        R.drawable.ic_tailor,
        R.drawable.ic_business_b_icon,
        R.drawable.ic_shoemaker,
        R.drawable.cinema,
        R.drawable.clubhouse,
        R.drawable.deer,
        R.drawable.ic_natural_wonder,
        R.drawable.gallery,
        R.drawable.currency_exchange,
        R.drawable.resort,
        R.drawable.playground,
        R.drawable.embassy,
        R.drawable.university,
        R.drawable.ic_college,
        R.drawable.ic_school_new,
        R.drawable.ic_library,
        R.drawable.ic_church,
        R.drawable.ic_mosque,
        R.drawable.tavern,
        R.drawable.ic_taxi,
        R.drawable.bus_station,
        R.drawable.bus_stop,
        R.drawable.train_station,
        R.drawable.factory,
        R.drawable.night_club,
        R.drawable.ic_labs,
        R.drawable.ic_mall_new,
        R.drawable.supermarket,
        R.drawable.historic_site,
        R.drawable.ic_congo,
        R.drawable.government_office,
        R.drawable.massage,
        R.drawable.stadium,
        R.drawable.bakery_shop,
        R.drawable.application,

        )
    private val businesscategory = arrayOf(
        "Hotel",
        "Restaurant",
        "Fashion Shop",
        "Beauty Salon",
        "Dealership",
        "Bank",
        "Museum",
        "Park",
        "Monument",
        "Content Production",
        "Delivery",
        "Therapy",
        "Call Center",
        "Stationary",
        "Hardware Store",
        "Pastry",
        "Orphanage",
        "Real Estate",
        "Betting",
        "Marketing",
        "Mineral",
        "Logistics",
        "Gym",
        "Air BnB",
        "Property Developer",
        "Airport",
        "Fast Food",
        "Technology",
        "Shop",
        "Printing",
        "Telecommunication",
        "Gift Shop",
        "Gallery Shop",
        "Guest House",
        "Cafeteria",
        "Coffee Shop",
        "Butchery",
        "Mall",
        "Construction Company",
        "ATM",
        "Foreign Exchange",
        "Event Host",
        "Makeup House",
        "Hospital",
        "Clinic",
        "Pharmacy",
        "Pension",
        "Station",
        "Townhouse",
        "Import Export",
        "Manufacturer",
        "Courier",
        "Legal Firm",
        "Business Consultant",
        "Cleaning Company",
        "Spaza Shop",
        "Super Market",
        "Hyper Market",
        "Spare Part",
        "Agents|Delala",
        "Liquore Store",
        "Online Store",
        "Interior Designer",
        "Fashion Designer",
        "Content Developer",
        "Social Media Marketing",
        "Decore",
        "Photography",
        "Videography",
        "Photography and Videography",
        "Car Rental",
        "Post office",
        "Tour and Travel",
        "Laundry",
        "Open Market",
        "Book Store",
        "Furniture Store",
        "Church",
        "Mosque",
        "Tailor Shop",
        "Azmari Bet",
        "Shoe Maker",
        "Cinema",
        "Sport Club",
        "Wild Life",
        "Natural Wonder",
        "Gallery",
        "Culture Center",
        "Resort",
        "Play Ground",
        "Embassy",
        "University",
        "College",
        "School",
        "Library",
        "Church",
        "Mosque",
        "Tavern",
        "Taxi Stand",
        "Bus Station",
        "Bus Stop",
        "Train Station",
        "Factory",
        "Night Club",
        "Laboratory",
        "Mall",
        "Supermarket",
        "Historical Site",
        "NGO",
        "Government Office",
        "Massage House",
        "Stadium",
        "Bakery",
        "Others"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getdetail()
    }

    fun getdetail(){
        categoryList.add(CategoryResponse("Hotels",R.drawable.hotel))
        categoryList.add(CategoryResponse("BnB",R.drawable.ic_bnb))
        categoryList.add(CategoryResponse("Mesuems",R.drawable.ic_museums))
        categoryList.add(CategoryResponse("Parks",R.drawable.park))
        categoryList.add(CategoryResponse("Hist Sites",R.drawable.historic_site))
        categoryList.add(CategoryResponse("Galleries",R.drawable.ic_sculpture))
        categoryList.add(CategoryResponse("Culture center",R.drawable.culture_dress))
        categoryList.add(CategoryResponse("Monuments",R.drawable.fue_station))
        categoryList.add(CategoryResponse("Libraries",R.drawable.ic_library))
        categoryList.add(CategoryResponse("African",R.drawable.ic_library))
        categoryList.add(CategoryResponse("Rests",R.drawable.restaurant))
        categoryList.add(CategoryResponse("Shops",R.drawable.ic_shop))
        categoryList.add(CategoryResponse("Banks",R.drawable.bank))
        categoryList.add(CategoryResponse("Apamts",R.drawable.ic_apartment))
        categoryList.add(CategoryResponse("Car Rent",R.drawable.ic_car_rent))
        categoryList.add(CategoryResponse("Post Office",R.drawable.ic_post_office))
        categoryList.add(CategoryResponse("Tour n Tr.",R.drawable.ic_tour_agents))
        categoryList.add(CategoryResponse("Airports",R.drawable.ic_airport))
        categoryList.add(CategoryResponse("Laundry",R.drawable.ic_laundry))
        categoryList.add(CategoryResponse("Open Mrkts",R.drawable.ic_market_place))

        categoryAdapter= CategoryAdapter(requireActivity(),businesscategory,businesscategoryImages)
        val list=ArrayList<String>()
        for (i in businesscategory.indices){
            list.add(businesscategory[i])
        }
        categoryAdapter.list=list
        tBinding.rvCategory.adapter=categoryAdapter
        tBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                filter(query.toString())
            }
        })
//        viewBinding!!.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<String> = ArrayList()

        // running a for loop to compare elements.
        filteredlist.clear()
        for (item in businesscategory) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
            categoryAdapter.filterList(filteredlist)

        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            categoryAdapter.filterList(filteredlist)
        }
    }
}