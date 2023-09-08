package com.example.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activites.MainActivity
import com.example.easyfood.activites.MealActivity
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val meal_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(meal_ID)
        }

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheetMeal()
        onBottomSheetDiaLogClick()
    }

    private fun onBottomSheetDiaLogClick() {
        binding.bottomSheet.setOnClickListener {
            if(mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    intent.putExtra(HomeFragment.MEAL_ID, mealId)
                    intent.putExtra(HomeFragment.MEAL_NAME, mealName)
                    intent.putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.setText(meal.strArea)
            binding.tvBottomSheetCategories.setText(meal.strCategory)
            binding.tvBottomMealName.setText(meal.strMeal)

            mealName = mealId
            mealThumb = meal.strMealThumb
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(meal_ID, param1)
                }
            }
    }
}