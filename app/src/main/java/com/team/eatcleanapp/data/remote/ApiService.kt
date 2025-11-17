package com.team.eatcleanapp.data.remote

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.domain.model.User
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service interface cho các endpoints của EatClean App
 * Sử dụng Retrofit để thực hiện các HTTP requests
 */
interface ApiService {
    
    // ==================== AUTHENTICATION ====================
    
    /**
     * Đăng nhập user
     * @param email Email của user
     * @param password Mật khẩu của user
     * @return Response<User> Thông tin user sau khi đăng nhập thành công
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<User>
    
    /**
     * Đăng ký user mới
     * @param request Thông tin đăng ký (email, password, name, etc.)
     * @return Response<User> Thông tin user sau khi đăng ký thành công
     */
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<User>
    
    /**
     * Đăng xuất user
     * @param userId ID của user
     * @return Response<Unit> Kết quả đăng xuất
     */
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
        @Body request: LogoutRequest
    ): Response<Unit>
    
    /**
     * Xóa tài khoản user
     * @param userId ID của user
     * @return Response<Unit> Kết quả xóa tài khoản
     */
    @DELETE("auth/account/{userId}")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<Unit>
    
    // ==================== MEALS ====================
    
    /**
     * Lấy tất cả các món ăn
     * @return Response<List<Meal>> Danh sách tất cả món ăn
     */
    @GET("meals")
    suspend fun getAllMeals(): Response<List<Meal>>
    
    /**
     * Tìm kiếm món ăn
     * @param query Từ khóa tìm kiếm
     * @param maxCalories Calo tối đa (optional)
     * @return Response<List<Meal>> Danh sách món ăn phù hợp
     */
    @GET("meals/search")
    suspend fun searchMeals(
        @Query("q") query: String,
        @Query("maxCalories") maxCalories: Double? = null
    ): Response<List<Meal>>
    
    /**
     * Lấy chi tiết một món ăn
     * @param mealId ID của món ăn
     * @return Response<Meal> Chi tiết món ăn
     */
    @GET("meals/{mealId}")
    suspend fun getMealDetail(
        @Path("mealId") mealId: String
    ): Response<Meal>
    
    // ==================== FAVORITES ====================
    
    /**
     * Lấy danh sách món ăn yêu thích của user
     * @param userId ID của user
     * @return Response<List<Meal>> Danh sách món ăn yêu thích
     */
    @GET("users/{userId}/favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Meal>>
    
    /**
     * Thêm món ăn vào danh sách yêu thích
     * @param userId ID của user
     * @param mealId ID của món ăn
     * @return Response<FavoriteResponse> Kết quả thêm vào yêu thích
     */
    @POST("users/{userId}/favorites")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: AddFavoriteRequest
    ): Response<FavoriteResponse>
    
    /**
     * Xóa món ăn khỏi danh sách yêu thích
     * @param userId ID của user
     * @param mealId ID của món ăn
     * @return Response<Unit> Kết quả xóa khỏi yêu thích
     */
    @DELETE("users/{userId}/favorites/{mealId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("mealId") mealId: String
    ): Response<Unit>
    
    /**
     * Kiểm tra xem món ăn có trong danh sách yêu thích không
     * @param userId ID của user
     * @param mealId ID của món ăn
     * @return Response<Boolean> true nếu là yêu thích, false nếu không
     */
    @GET("users/{userId}/favorites/{mealId}")
    suspend fun isFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("mealId") mealId: String
    ): Response<Boolean>
    
    // ==================== DAILY MENU ====================
    
    /**
     * Lấy menu hàng ngày của user cho một ngày cụ thể
     * @param userId ID của user
     * @param date Ngày cần lấy menu (format: yyyy-MM-dd)
     * @return Response<List<DailyMenu>> Danh sách món ăn trong menu ngày đó
     */
    @GET("users/{userId}/daily-menu")
    suspend fun getDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("date") date: String
    ): Response<List<DailyMenu>>
    
    /**
     * Lấy menu hàng tuần của user
     * @param userId ID của user
     * @param startDate Ngày bắt đầu tuần (format: yyyy-MM-dd)
     * @return Response<List<DailyMenu>> Danh sách món ăn trong menu tuần đó
     */
    @GET("users/{userId}/weekly-menu")
    suspend fun getWeeklyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("startDate") startDate: String
    ): Response<List<DailyMenu>>
    
    /**
     * Thêm món ăn vào menu hàng ngày
     * @param userId ID của user
     * @param request Thông tin món ăn cần thêm
     * @return Response<DailyMenuResponse> Kết quả thêm món ăn
     */
    @POST("users/{userId}/daily-menu")
    suspend fun addMealToDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: AddMealToDailyMenuRequest
    ): Response<DailyMenuResponse>
    
    /**
     * Xóa món ăn khỏi menu hàng ngày
     * @param userId ID của user
     * @param menuId ID của menu item cần xóa
     * @return Response<Unit> Kết quả xóa món ăn
     */
    @DELETE("users/{userId}/daily-menu/{menuId}")
    suspend fun deleteMealFromDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("menuId") menuId: String
    ): Response<Unit>
    
    // ==================== REQUEST/RESPONSE DATA CLASSES ====================
    
    data class LoginRequest(
        val email: String,
        val password: String
    )
    
    data class RegisterRequest(
        val email: String,
        val password: String,
        val name: String,
        val age: Int? = null,
        val gender: String? = null,
        val height: Double? = null,
        val weight: Double? = null
    )
    
    data class LogoutRequest(
        val userId: String
    )
    
    data class AddFavoriteRequest(
        val mealId: String
    )
    
    data class FavoriteResponse(
        val success: Boolean,
        val message: String? = null
    )
    
    data class AddMealToDailyMenuRequest(
        val date: String, // yyyy-MM-dd
        val mealId: String,
        val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACK
        val quantity: Double = 1.0
    )
    
    data class DailyMenuResponse(
        val id: String,
        val userId: String,
        val date: String,
        val mealId: String,
        val mealName: String,
        val calories: Double,
        val mealType: String,
        val protein: Double? = null,
        val carbs: Double? = null,
        val fat: Double? = null
    )
}
