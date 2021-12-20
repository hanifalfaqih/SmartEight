package sch.id.snapan.smarteight.repositories.base

import com.google.firebase.auth.AuthResult
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.data.entity.User
import sch.id.snapan.smarteight.other.Resource

interface AuthRepository {

    suspend fun registerUser(name: String, email: String, password: String, status: String): Resource<AuthResult>

    suspend fun loginUser(email: String, password: String): Resource<AuthResult>

}