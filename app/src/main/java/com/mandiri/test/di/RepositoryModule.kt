package com.mandiri.test.di

import com.mandiri.test.repository.MoviesRepository
import com.mandiri.test.remote.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideMoviesRepositoryImpl(repository: MoviesRepositoryImpl): MoviesRepository


}