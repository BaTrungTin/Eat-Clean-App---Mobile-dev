package com.team.eatcleanapp.domain.usecase.dailymenu


import com.team.eatcleanapp.domain.model.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject


class AddMealsToDayUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealRepository: MealRepository
) {

    data class Params(
        val userId: String,
        val date: Date,
        val mealIds: List<String>,
        val mealCategory: MealCategory
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // 1. Validate input cơ bản
        if (params.mealIds.isEmpty()) {
            return Result.Error(
                IllegalArgumentException("Danh sách món ăn không được để trống")
            )
        }

        // 2. Lấy chi tiết từng món -> build list MealToAdd
        val mealsToAdd = mutableListOf<DailyMenuRepository.MealToAdd>()

        for (mealId in params.mealIds) {
            // Giả sử getMealDetail trả về Meal?
            val meal = mealRepository.getMealDetail(mealId)

            if (meal == null) {
                // Nếu một món không tìm thấy -> fail luôn, không thêm dở dang
                return Result.Error(
                    IllegalStateException("Không tìm thấy món ăn với ID: $mealId")
                )
            }

            // Nếu meal.id là String thì gán cho id, ngược lại gán cho null
            val id = meal.id ?: return Result.Error(
                IllegalStateException("Món ăn $mealId không có id hợp lệ")
            )

            mealsToAdd.add(
                DailyMenuRepository.MealToAdd(
                    mealId = id,
                    mealName = meal.name,
                    calories = meal.totalCalories, // Sửa thành totalCalories vì class Meal không còn trường calories trực tiếp
                    mealType = params.mealCategory.name,
                    protein = meal.totalProtein,   // Lấy giá trị từ Meal mới
                    carbs = meal.totalCarbs,       // Lấy giá trị từ Meal mới
                    fat = meal.totalFat            // Lấy giá trị từ Meal mới
                )
            )
        }

        // 3. Gọi repository để thêm tất cả món đã chuẩn bị
        return when (val repoResult = dailyMenuRepository.addMealsToDailyMenu(
            userId = params.userId,
            date = params.date,
            meals = mealsToAdd
        )) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error   -> Result.Error(repoResult.exception)
            is Result.Loading -> Result.Loading
        }
    }
}
