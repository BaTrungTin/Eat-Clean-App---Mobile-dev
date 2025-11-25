package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class DeleteDayMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    /**
     * Xóa thực đơn theo ngày và buổi ăn
     * @param userId ID người dùng
     * @param dateMealTypes Map với:
     * - Key: Ngày cần xóa
     * - Value: Danh sách buổi ăn cần xóa
     *   + listOf(MealCategory.BREAKFAST) → xóa buổi sáng
     *   + listOf(MealCategory.LUNCH, MealCategory.DINNER) → xóa buổi trưa và tối
     *   + null → xóa toàn bộ ngày
     */
    suspend operator fun invoke(
        userId: String,
        dateMealTypes: Map<Date, List<MealCategory>?>
    ): Result<Unit> {
        if (dateMealTypes.isEmpty()) {
            return Result.Error(message = "Danh sách ngày và buổi ăn không được để trống")
        }

        return dailyMenuRepository.deleteMeals(userId, dateMealTypes)
    }
}