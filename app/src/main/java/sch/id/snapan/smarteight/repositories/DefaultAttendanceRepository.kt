package sch.id.snapan.smarteight.repositories

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteight.data.entity.Attendance
import sch.id.snapan.smarteight.data.entity.User
import sch.id.snapan.smarteight.other.Resource
import sch.id.snapan.smarteight.other.safeCallNetwork
import sch.id.snapan.smarteight.repositories.base.AttendanceRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DefaultAttendanceRepository: AttendanceRepository {

    private val authUid = Firebase.auth.uid!!
    private val pictures = Firebase.storage.getReference("Pictures")
    private val attendances = Firebase.firestore.collection("Attendances")
    private val users = Firebase.firestore.collection("Users")

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun attendanceUser(imageUri: Uri, name: String, status: String, activity: String, time: String): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val uid = authUid
                val attendanceId = UUID.randomUUID().toString()
                val imageUploadResult = pictures.child(authUid).putFile(imageUri).await()
                val imageUrl = imageUploadResult.metadata?.reference?.downloadUrl?.await().toString()
                val currentDateTime = LocalDateTime.now()
                val attendance = Attendance(
                    id = attendanceId,
                    name = name,
                    userId = uid,
                    imageUrl = imageUrl,
                    status = status,
                    activity = activity,
                    time = time,
                    date = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                )
                attendances.document(name).set(attendance).await()
                Resource.Success(Any())
            }
        }
    }

    override suspend fun getUser(): Resource<User?> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val user = users.document(authUid).get().await().toObject(
                    User::class.java)
                Resource.Success(user)
            }
        }
    }
}