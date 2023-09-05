package com.example.easyfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyfood.pojo.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao() : MealDao

    companion object {
        @Volatile
        var INSTACE: MealDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if(INSTACE == null) {
                INSTACE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTACE as MealDataBase
        }
    }
}