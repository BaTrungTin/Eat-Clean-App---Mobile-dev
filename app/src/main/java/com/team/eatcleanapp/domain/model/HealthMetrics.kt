package com.team.eatcleanapp.domain.model

/**
 * Data class để lưu trữ các chỉ số sức khỏe đã được tính toán của người dùng.
 * Các giá trị này được tạo ra bằng cách sử dụng `NutritionCalculator`.
 */
data class HealthMetrics(
    val bmi: Float,  // Chỉ số khối cơ thể
    val bmr: Float,  // Tỷ lệ trao đổi chất cơ bản
    val tdee: Float, // Tổng năng lượng tiêu thụ hàng ngày


)
