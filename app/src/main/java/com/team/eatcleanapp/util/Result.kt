package com.team.eatcleanapp.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val exception: Throwable? = null,   // exception
        val message: String? = null,        // message loi
        val code: Int? = null               // code loi
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()      // dang tai

    object Idle : Result<Nothing>()         // trang thai cho (chua lam gi)

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error
    
    val isLoading: Boolean
        get() = this is Loading

    val isIdle: Boolean
        get() = this is Idle

    // lay data neu Success, null neu khong
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    // lay data hoac throw exception neu Error
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception ?: RuntimeException(message ?: "Lỗi không xác định")
        is Loading -> throw IllegalStateException("Đang tải kết quả")
        is Idle -> throw IllegalStateException("Chưa bắt đầu thao tác nào")
    }

    fun errorMessage(): String? = when (this) {
        is Error -> message ?: exception?.message
        else -> null
    }
}

