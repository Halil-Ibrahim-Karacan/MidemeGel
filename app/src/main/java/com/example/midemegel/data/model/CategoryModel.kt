package com.example.midemegel.data.model

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Veritabanındaki "categories" tablosunu temsil eden model sınıfı.
 * Ürünlerin gruplandırıldığı kategorileri tanımlar.
 */
@Entity(tableName = "categories")
@SuppressLint("KotlinNullnessAnnotation")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id") var id: Int = 0,
    @ColumnInfo(name = "category_image") var image: String = "",
    @ColumnInfo(name = "category_name") var name: String = "",
    @ColumnInfo(name = "category_animation") var animation: String = "",
    @ColumnInfo(name = "category_animationSpeed") var animationSpeed: Int = 2,
    @ColumnInfo(name = "category_orderIndex") var orderIndex: Int = 0
)
