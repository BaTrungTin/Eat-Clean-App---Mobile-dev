package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject


class DeleteDayMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {

    /**
     * Data class để chứa các tham số đầu vào cho UseCase.
     */
    data class Params(
        val userId: String,
        val date: Date,
        val isConfirmed: Boolean // Cờ xác nhận từ người dùng
    )

    /**
     * @param params Các tham số cần thiết, bao gồm cả cờ xác nhận.
     * @return Trả về Result.Success(Unit) nếu xóa thành công, hoặc Result.Error nếu có lỗi hoặc hành động bị hủy.
     */
    suspend operator fun invoke(params: Params): Result<Unit> {
        //  Kiểm tra logic nghiệp vụ: Người dùng đã thực sự xác nhận chưa?
        if (!params.isConfirmed) {
            // Nếu chưa, trả về một lỗi đặc biệt để ViewModel có thể xử lý.
            return Result.Error(ActionCancelledException("Hành động xóa đã bị người dùng hủy bỏ."))
        }

        // Nếu đã xác nhận, gọi xuống repository để thực hiện xóa.
        // Vì hàm repository đã trả về Result<Unit>, chúng ta có thể trả về trực tiếp kết quả của nó.
        return dailyMenuRepository.deleteAllMealsByDate(
            userId = params.userId,
            date = params.date
        )
    }
}

class ActionCancelledException(message: String) : Exception(message)
