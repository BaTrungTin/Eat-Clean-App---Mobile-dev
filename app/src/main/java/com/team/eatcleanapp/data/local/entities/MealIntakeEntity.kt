package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// bang meal_intake
@Entity(tableName = "meal_intake")
data class MealIntakeEntity(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val userId : String,
    val mealId : String,
    val date : String,
    val mealTime : String,
    val quantity : Double,
    val unit : String,
    val isEaten : Boolean,
    val totalCalories : Double

)

