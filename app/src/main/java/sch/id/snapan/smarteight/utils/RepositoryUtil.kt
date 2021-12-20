package sch.id.snapan.smarteight.other

inline fun <T> safeCallNetwork(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Terjadi masalah tak diketahui")
    }
}