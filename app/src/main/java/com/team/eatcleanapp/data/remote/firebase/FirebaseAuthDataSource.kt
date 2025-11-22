package com.team.eatcleanapp.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource {
    private val auth = FirebaseAuth.getInstance()

    suspend fun registerWithEmail(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Đăng ký thất bại - không có người dùng được trả về")
    }

    suspend fun loginWithEmail(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Đăng nhập thất bại - không có người dùng được trả về")
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun deleteAccount() {
        val currentUser = auth.currentUser
            ?: throw Exception("Không có người dùng đăng nhập")
        currentUser.delete().await()
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return auth.currentUser
    }
}