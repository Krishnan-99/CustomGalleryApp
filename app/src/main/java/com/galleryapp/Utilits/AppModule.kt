package com.galleryapp.Utilits.di

import com.galleryapp.ViewModels.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { GalleryViewModel() }
}