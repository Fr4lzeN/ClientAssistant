package com.example.businesshub.di

import android.app.Application
import androidx.room.Room
import com.example.businesshub.data.data_source.CompanyApi
import com.example.businesshub.data.data_source.UserApi
import com.example.businesshub.data.data_source.UserDatabase
import com.example.businesshub.data.repository.CompanyApiRepositoryImpl
import com.example.businesshub.data.repository.UserApiRepositoryImpl
import com.example.businesshub.data.repository.UserRepositoryImpl
import com.example.businesshub.domain.repository.CompanyApiRepository
import com.example.businesshub.domain.repository.UserApiRepository
import com.example.businesshub.domain.repository.UserRepository
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
    fun provideUserDatabase(app: Application): UserDatabase{
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase): UserRepository{
        return UserRepositoryImpl(db.userDAO)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi{
        return Retrofit.Builder().baseUrl(UserApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(UserApi::class.java)
    }


    @Provides
    @Singleton
    fun provideUserApiRepository(userApi: UserApi) : UserApiRepository{
        return UserApiRepositoryImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideCompanyApi(): CompanyApi{
        return Retrofit.Builder().baseUrl(CompanyApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CompanyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompanyApiRepository(companyApi: CompanyApi): CompanyApiRepository{
        return CompanyApiRepositoryImpl(companyApi)
    }


}