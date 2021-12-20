package sch.id.snapan.smarteight.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import sch.id.snapan.smarteight.repositories.DefaultAnnouncementRepository
import sch.id.snapan.smarteight.repositories.base.AttendanceRepository
import sch.id.snapan.smarteight.repositories.DefaultAttendanceRepository
import sch.id.snapan.smarteight.repositories.base.AnnouncementRepository

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @ViewModelScoped
    @Provides
    fun provideAttendanceRepository() = DefaultAttendanceRepository() as AttendanceRepository

    @ViewModelScoped
    @Provides
    fun provideAnnouncementRepository() = DefaultAnnouncementRepository() as AnnouncementRepository
}