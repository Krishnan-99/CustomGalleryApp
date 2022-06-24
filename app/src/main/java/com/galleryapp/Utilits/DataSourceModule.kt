package com.galleryapp.Utilits.di

import com.galleryapp.Utilits.model.AlbumRepo
import com.galleryapp.Utilits.model.AlbumRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single<AlbumRepo> {
        AlbumRepoImpl(
            androidContext()
        )
    }
}