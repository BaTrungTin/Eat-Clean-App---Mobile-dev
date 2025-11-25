package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.local.entities.UserEntity
import com.team.eatcleanapp.domain.model.enums.ActivityLevel
import com.team.eatcleanapp.domain.model.enums.Gender
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.user.HealthMetrics
import com.team.eatcleanapp.domain.model.user.User

object UserMapper {

    // Domain -> Entity
    fun User.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            email = email,
            name = name,
            weight = weight,
            height = height,
            age = age,
            gender = gender.name,
            activityMinutesPerDay = activityMinutesPerDay,
            activityDaysPerWeek = activityDaysPerWeek,
            activityLevel = activityLevel.name,
            goal = goal.name,
            avatarUrl = avatarUrl,
            healthMetrics = healthMetrics?.let { HealthMetricsSerializer.serialize(it) },
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    // Entity -> Domain
    fun UserEntity.toDomain(): User {
        return User(
            id = id,
            email = email,
            name = name,
            weight = weight,
            height = height,
            age = age,
            gender = Gender.valueOf(gender),
            activityMinutesPerDay = activityMinutesPerDay,
            activityDaysPerWeek = activityDaysPerWeek,
            activityLevel = ActivityLevel.valueOf(activityLevel),
            goal = Goal.valueOf(goal),
            avatarUrl = avatarUrl,
            healthMetrics = healthMetrics?.let { HealthMetricsSerializer.deserialize(it) },
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

object HealthMetricsSerializer {
    fun serialize(healthMetrics: HealthMetrics): String {
        return "${healthMetrics.bmi},${healthMetrics.bmr},${healthMetrics.tdee},${healthMetrics.lastUpdated}"
    }

    fun deserialize(data: String): HealthMetrics {
        val parts = data.split(",")
        return HealthMetrics(
            bmi = parts[0].toFloat(),
            bmr = parts[1].toFloat(),
            tdee = parts[2].toFloat(),
            lastUpdated = parts[3].toLong()
        )
    }
}