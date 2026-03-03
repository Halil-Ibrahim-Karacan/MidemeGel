package com.example.midemegel.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.midemegel.data.database.BagProductsDAO
import com.example.midemegel.data.database.CategoriesDAO
import com.example.midemegel.data.database.DatabaseContactRoom
import com.example.midemegel.data.database.ProductsDAO
import com.example.midemegel.data.datasource.BagProductsDataSourceImpl
import com.example.midemegel.data.datasource.CategoriesDataSourceImpl
import com.example.midemegel.data.datasource.ProductsDataSourceImpl
import com.example.midemegel.data.repository.BagProductsRepositoryImpl
import com.example.midemegel.data.repository.CategoriesRepositoryImpl
import com.example.midemegel.data.repository.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt modülü. Uygulama genelinde kullanılacak bağımlılıkları (Database, DAO, Repository vb.) sağlar.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Veritabanı sürüm 4'ten 5'e geçiş için migrasyon tanımı.
     */
    val MIGRATION = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Veritabanı şeması değişiklikleri burada yapılır.
        }
    }

    /**
     * Room veritabanı örneğini sağlar.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseContactRoom {
        return Room.databaseBuilder(
            context,
            DatabaseContactRoom::class.java,
            "midemegel.sqlite"
        )
            .addMigrations(MIGRATION)
            .createFromAsset("midemegel.sqlite")
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsDAO(db: DatabaseContactRoom): ProductsDAO = db.getProductsDAO()

    @Provides
    @Singleton
    fun provideBagProductsDAO(db: DatabaseContactRoom): BagProductsDAO = db.getBagProductsDAO()

    @Provides
    @Singleton
    fun provideCategoriesDAO(db: DatabaseContactRoom): CategoriesDAO = db.getCategoriesDAO()

    @Provides
    @Singleton
    fun provideProductsDataSourceImpl(
        productsDAO: ProductsDAO
    ): ProductsDataSourceImpl = ProductsDataSourceImpl(productsDAO = productsDAO)

    @Provides
    @Singleton
    fun provideProductsRepositoryImpl(
        dataSource: ProductsDataSourceImpl
    ): ProductsRepositoryImpl = ProductsRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideBagProductsDataSourceImpl(
        bagProductsDAO: BagProductsDAO
    ): BagProductsDataSourceImpl = BagProductsDataSourceImpl(bagProductsDAO)

    @Provides
    @Singleton
    fun provideBagProductsRepositoryImpl(
        dataSource: BagProductsDataSourceImpl
    ): BagProductsRepositoryImpl = BagProductsRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideCategoriesDataSourceImpl(
        categoriesDAO: CategoriesDAO
    ): CategoriesDataSourceImpl = CategoriesDataSourceImpl(categoriesDAO)

    @Provides
    @Singleton
    fun provideCategoriesRepositoryImpl(
        dataSource: CategoriesDataSourceImpl
    ): CategoriesRepositoryImpl = CategoriesRepositoryImpl(dataSource)

}