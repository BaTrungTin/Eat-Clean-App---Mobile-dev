package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.Meal


interface MealRepository {

    //Lấy tất cả món ăn.

    suspend fun getAllMeals(): List<Meal>


    //Tìm kiếm món theo tên

    suspend fun searchMeals(
        query: String,
        maxCalories: Double? = null
    ): List<Meal>

   //Lấy chi tiết 1 món ăn theo id
    suspend fun getMealDetail(id: String): Meal?
}