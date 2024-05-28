package com.example.jodhy_monitor.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.jodhy_monitor.R
import com.example.jodhy_monitor.databinding.FragmentHomeBinding

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://th.bing.com/th/id/OIP.03Y2k5gKFduegDJzukP7CAHaES?rs=1&pid=ImgDetMain"))
        imageList.add(SlideModel("https://i.ytimg.com/vi/xoNwVy2tLXs/hqdefault.jpg?sqp=-oaymwEoCOADEOgC8quKqQMcGADwAQH4AYAKgALQBYoCDAgBEAEYVSBWKGUwDw==&rs=AOn4CLAA1bogUJypwRRhuDEGQqGBj8k7Ig"))
        imageList.add(SlideModel("https://live.staticflickr.com/1599/25139111231_239425e4e1.jpg"))

        val imageSlider = view.findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)
    }
}
