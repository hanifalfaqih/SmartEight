package sch.id.snapan.smarteight.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import sch.id.snapan.smarteight.repositories.base.AuthRepository
import sch.id.snapan.smarteight.repositories.DefaultAuthRepository

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @ViewModelScoped
    @Provides
    fun provideAuthRepository() = DefaultAuthRepository() as AuthRepository
}