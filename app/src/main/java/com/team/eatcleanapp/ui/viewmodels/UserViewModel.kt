package com.team.eatcleanapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.usecase.user.*
import com.team.eatcleanapp.domain.usecase.auth.CheckRegistrationProgressUseCase
import com.team.eatcleanapp.domain.usecase.health.UpdateHealthMetricsUseCase
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val completeUserProfileUseCase: CompleteUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val savePartialProfileUseCase: SavePartialProfileUseCase,
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase,
    private val checkRegistrationProgressUseCase: CheckRegistrationProgressUseCase,
    private val updateHealthMetricsUseCase: UpdateHealthMetricsUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<Result<User?>>(Result.Idle)
    val currentUser: StateFlow<Result<User?>> = _currentUser

    private val _userProfile = MutableStateFlow<Result<User>>(Result.Idle)
    val userProfile: StateFlow<Result<User>> = _userProfile

    private val _registrationProgress = MutableStateFlow<Result<com.team.eatcleanapp.domain.model.enums.RegistrationProgress>>(Result.Idle)
    val registrationProgress: StateFlow<Result<com.team.eatcleanapp.domain.model.enums.RegistrationProgress>> = _registrationProgress

    private val _updateProfileState = MutableStateFlow<Result<User>>(Result.Idle)
    val updateProfileState: StateFlow<Result<User>> = _updateProfileState

    private val _healthMetricsState = MutableStateFlow<Result<User>>(Result.Idle)
    val healthMetricsState: StateFlow<Result<User>> = _healthMetricsState

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            _currentUser.value = getCurrentUserUseCase()
        }
    }

    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            _userProfile.value = Result.Loading
            _userProfile.value = getUserProfileUseCase(userId)
        }
    }

    fun checkRegistrationProgress(userId: String) {
        viewModelScope.launch {
            _registrationProgress.value = Result.Loading
            _registrationProgress.value = checkRegistrationProgressUseCase(userId)
        }
    }

    fun completeUserProfile(
        userId: String,
        name: String,
        weight: Double,
        height: Double,
        age: Int,
        gender: com.team.eatcleanapp.domain.model.enums.Gender,
        activityMinutesPerDay: Int,
        activityDaysPerWeek: Int,
        goal: com.team.eatcleanapp.domain.model.enums.Goal
    ) {
        viewModelScope.launch {
            _updateProfileState.value = Result.Loading
            _updateProfileState.value = completeUserProfileUseCase(
                userId, name, weight, height, age, gender,
                activityMinutesPerDay, activityDaysPerWeek, goal
            )
        }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            _updateProfileState.value = Result.Loading
            _updateProfileState.value = updateUserProfileUseCase(user)
        }
    }

    fun savePartialProfile(
        userId: String,
        name: String? = null,
        weight: Double? = null,
        height: Double? = null,
        age: Int? = null
    ) {
        viewModelScope.launch {
            _updateProfileState.value = Result.Loading
            _updateProfileState.value = savePartialProfileUseCase(userId, name, weight, height, age)
        }
    }

    fun updateUserAvatar(userId: String, avatarUrl: String) {
        viewModelScope.launch {
            _updateProfileState.value = Result.Loading
            _updateProfileState.value = updateUserAvatarUseCase(userId, avatarUrl)
        }
    }

    fun updateHealthMetrics(user: User) {
        viewModelScope.launch {
            _healthMetricsState.value = Result.Loading
            _healthMetricsState.value = updateHealthMetricsUseCase(user)
        }
    }

    fun resetUpdateState() {
        _updateProfileState.value = Result.Idle
    }

    fun resetHealthMetricsState() {
        _healthMetricsState.value = Result.Idle
    }
}