package com.example.midemegel.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.midemegel.data.model.BagProductModel
import com.example.midemegel.data.model.CategoryModel
import com.example.midemegel.data.model.ProductModel

/**
 * Uygulamanın ana Room veritabanı yapılandırması.
 * Ürünler, Kategoriler ve Sepet ürünleri tablolarını içerir.
 */
@Database(entities = [ProductModel::class, BagProductModel::class, CategoryModel::class], version = 5)
abstract class DatabaseContactRoom : RoomDatabase() {
    /**
     * Ürünler tablosu için DAO erişimi sağlar.
     */
    abstract fun getProductsDAO() : ProductsDAO
    
    /**
     * Sepet ürünleri tablosu için DAO erişimi sağlar.
     */
    abstract fun getBagProductsDAO() : BagProductsDAO
    
    /**
     * Kategoriler tablosu için DAO erişimi sağlar.
     */
    abstract fun getCategoriesDAO() : CategoriesDAO

    companion object {
        private var INSTANCE : DatabaseContactRoom? = null

        /**
         * Veritabanı sürüm 3'ten 4'e geçiş için migrasyon tanımı.
         */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Gerekirse sütun ekleme veya tablo yapısı değişiklikleri burada yapılır.
            }
        }

        /**
         * Veritabanı örneğine (Instance) erişim sağlar (Singleton pattern).
         * @param context Uygulama bağlamı.
         * @return DatabaseContactRoom örneği.
         */
        fun access_Database(context: Context) : DatabaseContactRoom? {
            if (INSTANCE == null){
                synchronized(DatabaseContactRoom::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DatabaseContactRoom::class.java,"midemegel.sqlite")
                        .addMigrations(MIGRATION_3_4)
                        .createFromAsset("midemegel.sqlite")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
