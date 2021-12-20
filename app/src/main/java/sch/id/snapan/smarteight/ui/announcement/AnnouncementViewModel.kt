package sch.id.snapan.smarteight.ui.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.other.Event
import sch.id.snapan.smarteight.other.Resource
import sch.id.snapan.smarteight.repositories.base.AnnouncementRepository
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _getListAnnouncementStatus = MutableLiveData<Event<Resource<List<Announcement>>>>()
    val getListAnnouncementStatus: LiveData<Event<Resource<List<Announcement>>>> = _getListAnnouncementStatus

    private val _getDetailAnnouncementStatus = MutableLiveData<Event<Resource<Announcement?>>>()
    val getDetailAnnouncementStatus: LiveData<Event<Resource<Announcement?>>> = _getDetailAnnouncementStatus

    fun getListAnnouncement() {
        _getListAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.getListAnnouncement()
            _getListAnnouncementStatus.postValue(Event(result))
        }
    }

    fun getDetailAnnouncement(announcementId: String) {
        _getDetailAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.getDetailAnnouncement(announcementId)
            _getDetailAnnouncementStatus.postValue(Event(result))
        }
    }
}