package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject


class UpdateMealIntakeUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {

    /**
     * Data class để chứa các tham số đầu vào cho UseCase.
     */
    data class Params(
        val mealIntakeId: Long,
        val isConsumed: Boolean
    )

    /**
     * @param params Các tham số cần thiết để cập nhật trạng thái bữa ăn.
     * @return Trả về Result.Success(Unit) nếu cập nhật thành công, hoặc Result.Error nếu có lỗi.
     */
    suspend operator fun invoke(params: Params): Result<Unit> {
        // Gọi thẳng xuống repository vì nó đã có hàm xử lý logic này.
        return dailyMenuRepository.updateMealIntake(
            mealIntakeId = params.mealIntakeId,
            isConsumed = params.isConsumed
        )
    }
}
