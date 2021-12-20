package sch.id.snapan.smarteight.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.data.entity.User
import sch.id.snapan.smarteight.other.Resource
import sch.id.snapan.smarteight.other.safeCallNetwork
import sch.id.snapan.smarteight.repositories.base.AuthRepository

class DefaultAuthRepository: AuthRepository {

    private val auth = Firebase.auth
    private val users = Firebase.firestore.collection("Users")

    override suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        status: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(uid, email, name, status)
                users.document(uid).set(user).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

}