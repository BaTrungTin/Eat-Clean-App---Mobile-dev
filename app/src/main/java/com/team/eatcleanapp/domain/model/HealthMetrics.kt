package com.team.eatcleanapp.domain.model

/**
 * Data class để lưu trữ các chỉ số sức khỏe đã được tính toán của người dùng.
 * Các giá trị này được tạo ra bằng cách sử dụng `NutritionCalculator`.
 */
data class HealthMetrics(
    val bmi: Double = 0.0,
    val bmr: Double = 0.0,
    val tdee: Double = 0.0
)
