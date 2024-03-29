package com.example.businesshub.di

import android.app.Application
import androidx.room.Room
import com.example.businesshub.core.Constants
import com.example.businesshub.data.data_source.CompanyApi
import com.example.businesshub.data.data_source.PersonApi
import com.example.businesshub.data.data_source.UserApi
import com.example.businesshub.data.data_source.UserDatabase
import com.example.businesshub.data.repository.CompanyApiRepositoryImpl
import com.example.businesshub.data.repository.FirebaseStorageRepositoryImpl
import com.example.businesshub.data.repository.FirebaseUserRepositoryImpl
import com.example.businesshub.data.repository.FirestoreCompanyRepositoryImpl
import com.example.businesshub.data.repository.FirestoreUserRepositoryImpl
import com.example.businesshub.data.repository.UserRepositoryImpl
import com.example.businesshub.domain.repository.CompanyApiRepository
import com.example.businesshub.domain.repository.FirebaseStorageRepository
import com.example.businesshub.domain.repository.FirebaseUserRepository
import com.example.businesshub.domain.repository.FirestoreCompanyRepository
import com.example.businesshub.domain.repository.FirestoreUserRepository
import com.example.businesshub.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase): UserRepository {
        return UserRepositoryImpl(db.userDAO)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return Retrofit.Builder().baseUrl(UserApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(UserApi::class.java)
    }



    @Provides
    @Singleton
    fun provideCompanyApi(): CompanyApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_FUNC_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CompanyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompanyApiRepository(companyApi: CompanyApi): CompanyApiRepository {
        return CompanyApiRepositoryImpl(companyApi)
    }

    @Provides
    @Singleton
    fun providePersonApi(): PersonApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_FUNC_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PersonApi::class.java)
    }



    @Provides
    @Singleton
    fun provideFirebaseUserRepository(): FirebaseUserRepository {
        return FirebaseUserRepositoryImpl(Firebase.auth)
    }

    @Provides
    @Singleton
    fun provideFirestoreUserRepository(): FirestoreUserRepository {
        return FirestoreUserRepositoryImpl(Firebase.firestore.collection("users"))
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(): FirebaseStorageRepository{
        return FirebaseStorageRepositoryImpl(Firebase.storage.reference)
    }

    @Provides
    @Singleton
    fun provideFirestoreCompanyRepository():FirestoreCompanyRepository{
        return FirestoreCompanyRepositoryImpl(Firebase.firestore.collection("company"))
    }

}