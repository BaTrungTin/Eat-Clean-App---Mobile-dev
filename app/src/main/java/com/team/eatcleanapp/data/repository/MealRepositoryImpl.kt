package com.team.eatcleanapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.mapper.MealApiMapper
import com.team.eatcleanapp.data.mapper.MealMapper.toDomain
import com.team.eatcleanapp.data.mapper.MealMapper.toEntity
import com.team.eatcleanapp.data.mapper.MealMapper.toFirestoreMap
import com.team.eatcleanapp.data.mapper.MealMapper.toMeal
import com.team.eatcleanapp.data.mapper.MealMapper.toMealList
import com.team.eatcleanapp.data.remote.MealApiService
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Constants.MEALS
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mealDao: MealDao,
    private val mealApiService: MealApiService
) : MealRepository {

    override fun getAllMeals(): Flow<Result<List<Meal>>> {
        return mealDao.getAllMealsFlow().map { entities ->
            try {
                Result.Success(entities.toDomain())
            } catch (e: Exception) {
                Result.Error(e, "Lỗi khi tải danh sách món ăn")
            }
        }
    }

    override fun searchMeals(query: String): Flow<Result<List<Meal>>> {
        return mealDao.searchMealsByNameFlow(query)
            .map { entities ->
                try {
                    Result.Success(entities.toDomain())
                } catch (e: Exception) {
                    Result.Error(e, "Lỗi khi tìm kiếm món ăn")
                }
            }
    }

    override fun searchMealsByCalories(caloriesQuery: String): Flow<Result<List<Meal>>> {
        return mealDao.searchMealsByCaloriesFlow(caloriesQuery)
            .map { entities ->
                try {
                    Result.Success(entities.toDomain())
                } catch (e: Exception) {
                    Result.Error(e, "Lỗi khi tìm món ăn theo calories")
                }
            }
    }

    override suspend fun getMealDetail(id: String): Result<Meal?> {
        return try {
            android.util.Log.d("MealRepository", "getMealDetail called with ID: $id")
            
            // Ưu tiên lấy từ local database
            val localEntity = mealDao.getMealById(id)
            if (localEntity != null) {
                val localMeal = localEntity.toDomain()
                android.util.Log.d("MealRepository", "Found meal in local database: ${localMeal.name}, ingredients=${localMeal.ingredients.size}, instructions=${localMeal.instructions.size}")
                
                // Kiểm tra xem meal có đầy đủ dữ liệu không
                // Nếu không có ingredients hoặc instructions, thử lấy lại từ API
                if (localMeal.ingredients.isEmpty() || localMeal.instructions.isEmpty() || 
                    localMeal.instructions.all { it.isBlank() }) {
                    android.util.Log.d("MealRepository", "Meal in database is incomplete, fetching from API...")
                    val apiMeal = fetchMealFromApi(id)
                    if (apiMeal != null && (apiMeal.ingredients.isNotEmpty() || apiMeal.instructions.isNotEmpty())) {
                        android.util.Log.d("MealRepository", "Found complete meal in API, updating local database")
                        // Cập nhật vào local database
                        val entity = apiMeal.toEntity()
                        mealDao.insertMeal(entity) // insertMeal sử dụng REPLACE nên sẽ update
                        Result.Success(apiMeal)
                    } else {
                        // API không có dữ liệu tốt hơn, dùng local
                        Result.Success(localMeal)
                    }
                } else {
                    // Meal đã đầy đủ, dùng local
                    Result.Success(localMeal)
                }
            } else {
                android.util.Log.d("MealRepository", "Meal not found in local database, trying API...")
                // Nếu không có trong local, thử lấy từ API
                val apiMeal = fetchMealFromApi(id)
                if (apiMeal != null) {
                    android.util.Log.d("MealRepository", "Found meal in API, saving to local database")
                    // Lưu vào local database
                    val entity = apiMeal.toEntity()
                    mealDao.insertMeal(entity)
                    Result.Success(apiMeal)
                } else {
                    android.util.Log.d("MealRepository", "Meal not found in API, trying Firebase...")
                    // Fallback: thử lấy từ Firebase
                    syncSingleMealFromRemote(id)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MealRepository", "Error in getMealDetail: ${e.message}", e)
            Result.Error(e, "Lỗi khi lấy chi tiết món ăn: ${e.message}")
        }
    }
    
    /**
     * Lấy món ăn từ TheMealDB API
     */
    private suspend fun fetchMealFromApi(mealId: String): Meal? {
        return try {
            android.util.Log.d("MealRepository", "Fetching meal from API with ID: $mealId")
            
            // TheMealDB API sử dụng mealId là số (ví dụ: "52772")
            // Nếu mealId không phải số, có thể là ID từ local database
            val apiMealId = when {
                mealId.toIntOrNull() != null -> mealId // Đã là số, dùng trực tiếp
                mealId.startsWith("meal_") -> {
                    // Format "meal_001" - không thể search TheMealDB bằng custom ID
                    android.util.Log.d("MealRepository", "Custom meal ID format, skipping API")
                    return null
                }
                else -> {
                    // Thử parse hoặc return null
                    android.util.Log.d("MealRepository", "Unknown meal ID format: $mealId")
                    return null
                }
            }
            
            val response = mealApiService.getMealDetail(apiMealId)
            android.util.Log.d("MealRepository", "API response: ${response.meals?.size ?: 0} meals")
            
            response.meals?.firstOrNull()?.let { dto ->
                val meal = com.team.eatcleanapp.data.mapper.MealApiMapper.run { dto.toDomain() }
                android.util.Log.d("MealRepository", "Mapped meal: ${meal.name}")
                meal
            }
        } catch (e: Exception) {
            android.util.Log.e("MealRepository", "Error fetching meal from API: ${e.message}", e)
            null
        }
    }

    override suspend fun getMealsByIds(mealIds: List<String>): Result<List<Meal>> {
        return try {
            val entities = mealDao.getMealsByIds(mealIds)
            Result.Success(entities.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy danh sách món ăn theo IDs")
        }
    }

    override suspend fun deleteMeal(mealId: String): Result<Unit> {
        return try {
            mealDao.deleteMealById(mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa món ăn")
        }
    }

    override suspend fun mealExists(mealId: String): Result<Boolean> {
        return try {
            val exists = mealDao.mealExists(mealId)
            Result.Success(exists)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra món ăn tồn tại")
        }
    }

    override suspend fun syncMealsFromRemote(): Result<Unit> {
        return try {
            android.util.Log.d("MealRepository", "Starting sync from API...")
            
            // Lấy dữ liệu từ API (TheMealDB)
            val apiMeals = fetchMealsFromApi()
            
            if (apiMeals.isNotEmpty()) {
                android.util.Log.d("MealRepository", "Fetched ${apiMeals.size} meals from API")
                
                // 1. Xóa tất cả dữ liệu cũ trong local database
                android.util.Log.d("MealRepository", "Deleting old meals from local database...")
                mealDao.deleteAllMeals()
                
                // 2. Xóa tất cả dữ liệu cũ trong Firebase
                android.util.Log.d("MealRepository", "Deleting old meals from Firebase...")
                deleteAllMealsFromFirestore()
                
                // 3. Lưu dữ liệu mới từ API vào Firebase
                android.util.Log.d("MealRepository", "Saving ${apiMeals.size} meals to Firebase...")
                saveMealsToFirestore(apiMeals)
                
                // 4. Lưu vào local database
                android.util.Log.d("MealRepository", "Saving ${apiMeals.size} meals to local database...")
                val entities = apiMeals.toEntity()
                mealDao.insertMeals(entities)
                
                android.util.Log.d("MealRepository", "Sync completed successfully")
                Result.Success(Unit)
            } else {
                android.util.Log.w("MealRepository", "No meals fetched from API, falling back to Firebase")
                // Fallback: lấy từ Firebase nếu API không có dữ liệu
                val firestoreMeals = fetchMealsFromFirestore()
                val entities = firestoreMeals.toEntity()
                mealDao.insertMeals(entities)
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            android.util.Log.e("MealRepository", "Error syncing meals: ${e.message}", e)
            Result.Error(e, "Lỗi khi đồng bộ dữ liệu món ăn: ${e.message}")
        }
    }
    
    /**
     * Xóa tất cả meals trong Firebase Firestore
     */
    private suspend fun deleteAllMealsFromFirestore() {
        try {
            val snapshot = firestore.collection(MEALS).get().await()
            val batch = firestore.batch()
            
            snapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }
            
            if (snapshot.documents.isNotEmpty()) {
                batch.commit().await()
                android.util.Log.d("MealRepository", "Deleted ${snapshot.documents.size} old meals from Firebase")
            }
        } catch (e: Exception) {
            android.util.Log.e("MealRepository", "Error deleting meals from Firebase: ${e.message}", e)
            throw e
        }
    }
    
    /**
     * Lưu danh sách meals vào Firebase Firestore
     */
    private suspend fun saveMealsToFirestore(meals: List<Meal>) {
        try {
            val batch = firestore.batch()
            
            meals.forEach { meal ->
                val mealMap = meal.toFirestoreMap()
                val docRef = firestore.collection(MEALS).document(meal.id)
                batch.set(docRef, mealMap)
            }
            
            batch.commit().await()
            android.util.Log.d("MealRepository", "Saved ${meals.size} meals to Firebase")
        } catch (e: Exception) {
            android.util.Log.e("MealRepository", "Error saving meals to Firebase: ${e.message}", e)
            throw e
        }
    }
    
    /**
     * Lấy món ăn từ TheMealDB API
     * Lấy các món ăn từ nhiều categories phổ biến
     */
    private suspend fun fetchMealsFromApi(): List<Meal> {
        return try {
            val allMeals = mutableListOf<com.team.eatcleanapp.data.remote.dto.MealDto>()
            
            // Lấy món ăn từ các categories phổ biến
            val categories = listOf("Beef", "Chicken", "Seafood", "Vegetarian", "Breakfast", "Dessert")
            
            categories.forEach { category ->
                try {
                    val response = mealApiService.getMealsByCategory(category)
                    response.meals?.let { allMeals.addAll(it) }
                } catch (e: Exception) {
                    // Bỏ qua category lỗi, tiếp tục với category khác
                }
            }
            
            // Lấy thêm một số món ngẫu nhiên
            repeat(5) {
                try {
                    val randomResponse = mealApiService.getRandomMeal()
                    randomResponse.meals?.firstOrNull()?.let { allMeals.add(it) }
                } catch (e: Exception) {
                    // Bỏ qua nếu lỗi
                }
            }
            
            // Chuyển đổi sang domain model và loại bỏ trùng lặp
            val uniqueMeals = allMeals.distinctBy { it.id }
            android.util.Log.d("MealRepository", "Total unique meals from API: ${uniqueMeals.size}")
            
            uniqueMeals.mapNotNull { dto ->
                try {
                    val meal = MealApiMapper.run { dto.toDomain() }
                    android.util.Log.d("MealRepository", "Mapped meal: ${meal.name} (ID: ${meal.id})")
                    meal
                } catch (e: Exception) {
                    android.util.Log.e("MealRepository", "Error mapping meal ${dto.id}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchMealsFromFirestore(): List<Meal> {
        return try {
            val snapshot = firestore.collection(MEALS).get().await()
            snapshot.documents.toMealList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun syncSingleMealFromRemote(mealId: String): Result<Meal?> {
        return try {
            val document = firestore.collection(MEALS).document(mealId).get().await()
            if (document.exists()) {
                val meal = document.toMeal()
                val entity = meal.toEntity()
                mealDao.insertMeal(entity)
                Result.Success(meal)
            } else {
                Result.Success(null)
            }
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đồng bộ món ăn từ server")
        }
    }
}