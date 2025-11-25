package com.team.eatcleanapp.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {

    override suspend fun registerUser(user: User): Result<User> {
        return try {
            // 1. Tạo user trên Firebase Auth
            val authResult = withTimeout(10000L) {
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            }
            
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val userId = firebaseUser.uid
                val userWithId = user.copy(id = userId)
                
                // 2. Lưu vào Firestore - FIRE AND FORGET
                try {
                    firebaseFirestore.collection("users")
                        .document(userId)
                        .set(userWithId)
                        .addOnSuccessListener { 
                            Log.d("UserRepository", "Lưu Firestore thành công") 
                        }
                        .addOnFailureListener { e -> 
                            Log.e("UserRepository", "Lưu Firestore thất bại: ${e.message}") 
                        }
                } catch (e: Exception) {
                    Log.e("UserRepository", "Lỗi khi gọi set: ${e.message}")
                }
                
                Result.Success(userWithId)
            } else {
                Result.Error(Exception("Đăng ký thất bại: Không thể lấy thông tin user"))
            }
        } catch (e: Exception) {
            val message = if (e is kotlinx.coroutines.TimeoutCancellationException) {
                "Quá thời gian kết nối. Vui lòng kiểm tra mạng."
            } else {
                e.message ?: "Đăng ký thất bại"
            }
            Result.Error(Exception(message))
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = withTimeout(10000L) {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            }
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val userId = firebaseUser.uid
                
                // Khi login, thử lấy data trong tối đa 5 giây
                try {
                    val snapshot = withTimeout(5000L) {
                        firebaseFirestore.collection("users")
                            .document(userId)
                            .get()
                            .await()
                    }
                    
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        Result.Success(user)
                    } else {
                        Result.Success(createBasicUser(firebaseUser))
                    }
                } catch (e: Exception) {
                     Log.e("UserRepository", "Login Firestore error: ${e.message}")
                     Result.Success(createBasicUser(firebaseUser))
                }
            } else {
                Result.Error(Exception("Đăng nhập thất bại"))
            }
        } catch (e: Exception) {
             val message = if (e is kotlinx.coroutines.TimeoutCancellationException) {
                "Quá thời gian kết nối. Vui lòng kiểm tra mạng."
            } else {
                e.message ?: "Đăng nhập thất bại"
            }
            Result.Error(Exception(message))
        }
    }

    private fun createBasicUser(firebaseUser: com.google.firebase.auth.FirebaseUser): User {
        return User(
             id = firebaseUser.uid,
             email = firebaseUser.email ?: "",
             password = "",
             name = firebaseUser.displayName ?: "User",
             weight = 0.0, height = 0.0, age = 0,
             gender = com.team.eatcleanapp.domain.model.Gender.MALE,
             activityLevel = com.team.eatcleanapp.domain.model.ActivityLevel.SEDENTARY,
             goal = com.team.eatcleanapp.domain.model.Goal.MAINTAIN_WEIGHT,
             activityMinutesPerDay = 0, activityDaysPerWeek = 0,
             avatarUrl = null, healthMetrics = null
        )
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val snapshot = firebaseFirestore.collection("users").document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
            if (user != null) Result.Success(user) else Result.Error(Exception("User not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            firebaseFirestore.collection("users").document(user.id).set(user).await()
            Result.Success(user)
        } catch (e: Exception) {
             Result.Error(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> {
        return try {
            try { firebaseFirestore.collection("users").document(userId).delete().await() } catch(_: Exception){}
            firebaseAuth.currentUser?.delete()?.await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> {
         return Result.Success(false) 
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
