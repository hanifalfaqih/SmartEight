package sch.id.snapan.smarteight.ui.attendance

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteight.data.entity.User
import sch.id.snapan.smarteight.other.Event
import sch.id.snapan.smarteight.other.Resource
import sch.id.snapan.smarteight.repositories.base.AttendanceRepository
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _attendanceStatusUser = MutableLiveData<Event<Resource<Any>>>()
    val attendanceStatusUser: LiveData<Event<Resource<Any>>> = _attendanceStatusUser

    private val _getUserStatus = MutableLiveData<Event<Resource<User?>>>()
    val getUserStatus: LiveData<Event<Resource<User?>>> = _getUserStatus

    fun attendanceUser(imageUri: Uri, name: String, status: String, activity: String, time: String) {
        _attendanceStatusUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = attendanceRepository.attendanceUser(imageUri, name, status, activity, time)
            _attendanceStatusUser.postValue(Event(result))
        }
    }

    fun getUser() {
        viewModelScope.launch(dispatcher) {
            val result = attendanceRepository.getUser()
            _getUserStatus.postValue(Event(result))
        }
    }

}