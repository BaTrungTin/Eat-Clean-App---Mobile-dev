package com.team.eatcleanapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team.eatcleanapp.domain.model.Ingredient
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MealRepository {

    private val collectionName = "monan"

    override suspend fun getAllMeals(): List<Meal> {
        return try {
            val snapshot = firestore.collection(collectionName).get().await()
            snapshot.documents.mapNotNull { doc ->
                mapDocumentToMeal(doc)
            }
        } catch (e: Exception) {
            Log.e("MealRepository", "Lỗi lấy danh sách món ăn: ${e.message}")
            emptyList()
        }
    }

    override suspend fun searchMeals(query: String, maxCalories: Double?): List<Meal> {
        return try {
            val allMeals = getAllMeals()
            
            allMeals.filter { meal ->
                val nameMatches = meal.name.contains(query, ignoreCase = true)
                val caloriesMatches = maxCalories?.let { meal.totalCalories <= it } ?: true
                nameMatches && caloriesMatches
            }
        } catch (e: Exception) {
            Log.e("MealRepository", "Lỗi tìm kiếm món ăn: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getMealDetail(id: String): Meal? {
        return try {
            val doc = firestore.collection(collectionName).document(id).get().await()
            mapDocumentToMeal(doc)
        } catch (e: Exception) {
            Log.e("MealRepository", "Lỗi lấy chi tiết món ăn: ${e.message}")
            null
        }
    }

    private fun mapDocumentToMeal(doc: com.google.firebase.firestore.DocumentSnapshot): Meal? {
        if (!doc.exists()) return null
        
        try {
            val data = doc.data ?: return null
            
            val id = doc.id
            
            // Fix: Kiểm tra cả key tiếng Việt có dấu (như trên DB của bạn) và không dấu
            val name = (data["tên"] ?: data["ten"]) as? String ?: "Món ăn chưa có tên"
            val image = (data["hình ảnh"] ?: data["hinhAnh"]) as? String
            val description = (data["mô tả"] ?: data["moTa"]) as? String ?: ""
            
            val instructionsRaw = (data["hướng dẫn"] ?: data["huongDan"]) as? List<*>
            val instructions = instructionsRaw?.mapNotNull { it.toString() } ?: emptyList()
            
            val ingredientsRaw = (data["thành phần"] ?: data["thanhPhan"]) as? List<Map<String, Any>>
            val ingredientsList = ingredientsRaw?.mapNotNull { item ->
                try {
                    Ingredient(
                        name = (item["tên"] ?: item["ten"]) as? String ?: "",
                        quantity = ((item["số lượng"] ?: item["soLuong"]) as? Number)?.toDouble() ?: 0.0,
                        unit = (item["đơn vị"] ?: item["donVi"]) as? String ?: "",
                        caloriesPer100 = ((item["calo trên 100"] ?: item["caloTren100"]) as? Number)?.toDouble() ?: 0.0,
                        carbsPer100 = ((item["carb trên 100"] ?: item["carbTren100"]) as? Number)?.toDouble() ?: 0.0,
                        proteinPer100 = ((item["protein trên 100"] ?: item["proteinTren100"]) as? Number)?.toDouble() ?: 0.0,
                        fatPer100 = ((item["fat trên 100"] ?: item["fatTren100"]) as? Number)?.toDouble() ?: 0.0
                    )
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            return Meal(
                id = id,
                name = name,
                image = image,
                description = description,
                ingredients = ingredientsList,
                instructions = instructions
            )
        } catch (e: Exception) {
            Log.e("MealRepository", "Lỗi parse document ${doc.id}: ${e.message}")
            return null
        }
    }
}
