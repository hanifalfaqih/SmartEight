package sch.id.snapan.smarteight.repositories.base

import android.net.Uri
import sch.id.snapan.smarteight.data.entity.User
import sch.id.snapan.smarteight.other.Resource

interface AttendanceRepository {

    suspend fun attendanceUser(imageUri: Uri, name: String, status: String, activity: String, time: String): Resource<Any>

    suspend fun getUser(): Resource<User?>

}