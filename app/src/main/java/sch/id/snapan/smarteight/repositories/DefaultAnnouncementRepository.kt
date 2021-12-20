package sch.id.snapan.smarteight.repositories

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.other.Resource
import sch.id.snapan.smarteight.other.safeCallNetwork
import sch.id.snapan.smarteight.repositories.base.AnnouncementRepository

class DefaultAnnouncementRepository: AnnouncementRepository {

    private val announcements = Firebase.firestore.collection("Announcements")

    override suspend fun getListAnnouncement(): Resource<List<Announcement>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val allAnnouncements = announcements.orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(Announcement::class.java)
                Resource.Success(allAnnouncements)
            }
        }
    }

    override suspend fun getDetailAnnouncement(announcementId: String): Resource<Announcement?> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val announcement = announcements.document(announcementId).get().await().toObject(Announcement::class.java)
                Resource.Success(announcement)
            }
        }
    }
}