package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDataBase
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDataBase: MealDataBase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDataBase.mealDao().getAllSavedMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()


    private var saveSateRandomMeal: Meal? = null

    fun getRandomMeal() {
        saveSateRandomMeal?.let {randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveSateRandomMeal = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItem("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }

    fun insert(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.mealDao().insertFavorite(meal)
        }
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDatails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun delete(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.mealDao().deleteMeal(meal)
        }
    }

    fun observerSearchedMealsLiveData(): LiveData<List<Meal>> = searchMealsLiveData

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.search(searchQuery).enqueue(
        object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        }
    )

    fun observeRandomMealLivedata(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoryLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }

    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetMealLiveData
}