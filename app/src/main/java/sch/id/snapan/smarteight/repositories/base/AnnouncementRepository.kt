package sch.id.snapan.smarteight.repositories.base

import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.other.Resource

interface AnnouncementRepository {

    suspend fun getListAnnouncement(): Resource<List<Announcement>>

    suspend fun getDetailAnnouncement(announcementId: String): Resource<Announcement?>

}