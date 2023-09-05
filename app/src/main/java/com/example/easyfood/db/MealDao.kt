package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.pojo.Meal

@Dao
interface MealDao {

    @Insert
    fun insertFavorite(meal: Meal)

    @Update
    fun updateFavorite(meal: Meal)

    @Delete
    fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllSavedMeals(): LiveData<List<Meal>>

}