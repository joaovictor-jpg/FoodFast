package com.example.easyfood.retrofit

import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDatails(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItem(@Query("c") categoriaName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>
}