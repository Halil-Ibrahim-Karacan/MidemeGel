package com.example.midemegel.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Sepete eklenen ürünleri temsil eden veritabanı model sınıfı.
 * "bagProducts" tablosunda saklanır ve ürünlerin miktar (count) bilgisini de içerir.
 */
@Entity(
    tableName = "bagProducts",
    foreignKeys = [
        ForeignKey(
            entity = CategoryModel::class,
            parentColumns = ["category_id"],
            childColumns = ["product_category"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["product_category"])]
)
data class BagProductModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Int = 0,

    @ColumnInfo(name = "product_originalId")
    var originalId: Int = 0,

    @ColumnInfo(name = "product_image")
    var image: String = "",

    @ColumnInfo(name = "product_name")
    var name: String = "",

    @ColumnInfo(name = "product_category")
    var categoryId: Int = 0,

    @ColumnInfo(name = "product_chcategory")
    var chcategory: String = "",

    @ColumnInfo(name = "product_color")
    override var color: String = "",

    @ColumnInfo(name = "product_price")
    var price: Double = 0.0,

    @ColumnInfo(name = "product_ingredients")
    var ingredients: String = "",

    @ColumnInfo(name = "product_description")
    var description: String = "",

    @ColumnInfo(name = "product_favorite") var favorite: Int = 0,

    @ColumnInfo(name = "product_ratingSize")
    var ratingSize: Float = 0.0f,

    @ColumnInfo(name = "product_ratingCount")
    var ratingCount: Int = 0,

    @ColumnInfo(name = "product_myRatingSize")
    var myRatingSize: Float = 0.0f,

    @ColumnInfo(name = "product_count")
    var count: Int = 0

) : IColorable {

    /**
     * Ürün içindekiler metnini virgül, noktalı virgül veya yeni satıra göre parçalar.
     * @return Parçalanmış ve temizlenmiş metin listesi.
     */
    fun getIngredientsList(): List<String> {
        if (ingredients.isBlank()) return emptyList()
        
        return ingredients.split(Regex("[\\n,;]"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}
