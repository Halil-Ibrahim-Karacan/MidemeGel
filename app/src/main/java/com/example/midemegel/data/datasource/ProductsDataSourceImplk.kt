/*
package com.example.midemegel.datasource

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.example.midemegel.enums.Category
import com.example.midemegel.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

@SuppressLint("NewApi")
class ProductsDataSourceImpl : ProductsDataSource{

    // Food Colors
    val food1: Color = Color(0xFFFFE2B7)
    val food2: Color = Color(0xFFFFECA0)
    val food3: Color = Color(0xFFD3963B)
    val food4: Color = Color(0xFFC2F070)
    val food5: Color = Color(0xFFFF8F8F)

    // Drink Colors
    val drink1: Color = Color(0xFF69C1F0)
    val drink2: Color = Color(0xFFF0A387)
    val drink3: Color = Color(0xFFFF7C7C)
    val drink4: Color = Color(0xFFFFBB57)
    val drink5: Color = Color(0xFF85E26D)

    //CoffeeHouse Colors
    val coffeehouse1: Color = Color(0xFF423532)
    val coffeehouse2: Color = Color(0xFF2C2321)
    val coffeehouse3: Color = Color(0xFFA5B341)
    val coffeehouse4: Color = Color(0xFF493224)
    val coffeehouse5: Color = Color(0xFF5D6044)
    val coffeehouse6: Color = Color(0xFFCAAC86)
    val coffeehouse7: Color = Color(0xFFEDA12A)
    val coffeehouse8: Color = Color(0xFFBE1E29)
    val coffeehouse9: Color = Color(0xFF95485F)

    val productsIngredients = mutableStateListOf(
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Diyetisyen Korkusu", "İsyan", "Özgürlük"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Bir Tutam Dua", "Manevi Rahatlık", "Göz Hakkı"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Görümcelik Damarı", "Elti Masumluğu", "Zafer"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Son 50 Liranın Gururu", "Geçici Doygunluk"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Lağım Suyu Lezzeti (Şaka!)", "Ninja Becerisi", "Cowabunga Enerjisi"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "La La La Neşesi", "Şirinlerin Sırrı", "Gargamel'in Öfkesi"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Tatil Pozu", "Plaj Keyfi", "Bronz Ten"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Kayınvalide Onayı", "Aile Sınavı Geçişi", "\"Aileye Yakıştı\" Damgası"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "Komşu Teyze Korkusu", "Vicdan Azabı", "\"Bir Daha Yapmam\" Yemini"),
        mutableStateListOf<String>("A,B,C,Ç,D,E,F,G,Ğ... Vitaminleri", "\"Aman Kimse Görmesin\" Duası", "\"Helalleşme\" Aroması", "Taze Pişmanlık"),
        )


    val productsDescriptions = mutableStateListOf<String>(
        "İtiraf ediyorum! O masum salata tabağının ardına saklanıp, diyetisyenimin radarından gizlice sızdım. Bu yemek, kalori sayımım üzerinde bir kaçamak darbe gerçekleştirdi. Tadı, vicdan azabına değdi mi? Kesinlikle evet. Bu, bir isyan tabağı. Bir özgürlük çığlığı. Bugün pişman olacağımı bile bile yediğim o muhteşem şey işte bu.",
        "Normalde bir porsiyonla doyan insan, bu yemek karşısında 'İkramdır, geri çevrilmez' diyerek üçüncüyü alır. O pilavın tereyağlı kokusu, o etin yumuşaklığı tarifle olmaz. Annenin 'Camide yedim' deyip, akşam yemeğinden affolmasına sebep olan efsanevi lezzet. Dikkat! Tabağınızı alırken 'Aman abartma' uyarısına maruz kalabilirsiniz.",
        "Bu somon, sıradan bir balık değil; bir aile savaşının ortasında, 'Sen ne kadar bilgisin!' diyen görümce ile 'Ama ben sevmiyorum!' diyen eltinin arasında sıkışıp kalmış bir diplomat. Zorla yedirilen her lokmada, bir 'aslında lezzetliymiş' itirafı ve bir 'bak gördün mü!' zaferi saklı. Sonuç: Görümce 1 - 0 Elti. (Ve itiraf ediyorum, aslında bayağı güzeldi.)",
        "Aylığın son demleri, cüzdanın hüznü... Bu spagetti, o hüznü bir umut ışığına çeviren sihirbaz. İçindeki her bir parça, 'daha güzel günler göreceğiz' diye fısıldıyor. Fiyat/performansın kralı, öğrenci dostu, ekonomik krizin süper starı. Lezzeti ise 'pahalı lokantalara taş çıkartacak' cinsten!",
        "Splinter Usta bile bu pizzanın karşısında disiplini unuttu! Kabuğunun o kıtır kıtır sesi, dört kollu bir ninjanın saldırısına uğramış gibi 'hapur hupur' yok oluşunu izliyoruz. İçindeki peynirler o kadar esnek ki, Raphael'in kullandığı ip gibi uzanıyor. Leonardo'nun disiplini, Donatello'nun mantığı, Michelangelo'nun iştahı ve Raphael'in öfkesi... Hepsi bu lezzet karşısında bir olup 'Cowabunga!' diye bağırıyor. Bu bir pizza değil, bir çocukluk hayalinin gerçeğe dönüşmüş hali!",
        "Şirin Köyü'nün en iyi saklanan sırrı ve Gargamel'in 'Acaba nerede?' diye ormanı altüst ederken bulamadığı efsanevi içecek! İçenlerde anında mutluluk, enerji ve bir tutam 'La la la' neşesi uyandırır. Yan etkileri: Sürekli şarkı söylemek, mantar evlerde yaşamayı istemek ve Gargamel'den saklanma refleksi. Şirin Baba'nın özel günler için izin verdiği, Azman'ın gözünü diktiği bu iksiri görürseniz... Hemen için ve SAKLANIN!",
        "Dayınızın Marmaris'ten dönüşte ailesine, komşusuna, bakkal Yaşar'a hatta minibüste yanında oturan adama bile 'Bak, Marmaris'te böyle içiyorlar' diye hava atmak için iki yakasını bir araya getirdiği içeceğin ta kendisi. İçindeki karışım, Ege'nin meltemi; buzları, Akdeniz'in serinliği... Dayın 'Bunu her yerde bulamazsın' derken aslında 'Orada olduğum için buldum' demek istiyor. Bir yudumunda bile tatil pozu yapma isteği uyandıran, bronz tenin ve 'bak sen de yap' havasının resmen sıvı hali. Afiyet olsun!",
        "Aile toplantısının en kritik anı: Enişte adayı elinde bu içecekle içeri giriyor! Damadın elindeki çilekli içecek, sanki bir test kağıdı... Kayınvalide 'İçindeki yapay aromayı anlarım' bakışları atarken, kayınpeder 'Ayran içseydi bari' diye mırıldanıyor. Bu içecek, sadece test değil; bir erkeğin aile onayı yolculuğundaki ilk sınavının ta kendisi! Kıvamı ne çok sulu ne çok katı, tam ayarında... Tıpkı aileye verilen mesajlar gibi. Sonuç: 'Geçti, ama final sınavına bekleriz!' \n\nNot: İçeceğin başarı oranı, yanında getirilen çiçek buketinin büyüklüğüyle doğru orantılıdır.",
        "Komşu teyzenin 'Bir koparırsanız annenize söylerim!' diye bağırdığı, ağacın en güneş görmüş, en gizli portakallarının son yolculuğu... O çalıntı lezzetin vicdan azabıyla karışmış tatlı meyvesi, buzla buluştu, bir tutam şekerle günahına ortak oldu. Şimdi bu bardakta, bir çocukluk macerasının 'hazin' ama bir o kadar da lezzetli sonu var. İçerken içinizdeki yaramaz çocuğa seslenin: 'Vallaha billaha bir daha yapmayacağız!' deyin. (Ama portakal mevsimi gelsin, yine yaparız!)",
        "Halamın balkonunda gururla büyüttüğü, 'Bunu koparana yazıklar olsun!' dediği o nadide nane, şimdi bu bardakta buz gibi bir serinliğe büründü. Dalından 'ödünÇalınışının' hüzünlü ama bir o kadar da lezzetli hikayesi... Her yudumda, 'Acaba kokusunu alır mı?' endişesiyle için titrer ama damaklar şenlik yapar.\n\nNot: Nanenin ruhu, halamın 'Benim nanem de nerede boy atmış!' övgüsüne kadar bizimle."
    )

    var _products = mutableStateListOf<ProductModel>(
        ProductModel(id = "1", image = "food1", name = "Diyetisyenimden Gizlice Yediğim Yemek", category =  Category.Yemekler, color = food1, price = 124.99, ingredients = productsIngredients[0], description = productsDescriptions[0], favorite = false, ratingSize = 3.0f, ratingCount = 52, myRatingSize = 1.0f),
        ProductModel(id = "2", image = "food2", name = "Caminin Mevlit Yemeği", category =  Category.Yemekler, color = food2, price = 149.99, ingredients = productsIngredients[1], description = productsDescriptions[1], favorite = false, ratingSize = 2.0f, ratingCount = 47, myRatingSize = 1.0f),
        ProductModel(id = "3", image = "food3", name = "Görümcenin Eltiye Zorla Yedirdiği Somon", category =  Category.Yemekler, color =  food3, price = 249.99, ingredients = productsIngredients[2], description = productsDescriptions[2], favorite = true, ratingSize = 4.0f, ratingCount = 105, myRatingSize = 1.0f),
        ProductModel(id = "4", image = "food4", name = "Aylık Bitmeden Önceki Son Umut", category =  Category.Yemekler, color =  food4, price = 49.99, ingredients = productsIngredients[3], description = productsDescriptions[3], favorite = false, ratingSize = 4.0f, ratingCount = 50, myRatingSize = 1.0f),
        ProductModel(id = "5", image = "food5", name = "Ninja Kaplumbağaların Kıskanarak Hapur Hupur Yediği Pizza", category =  Category.Yemekler, color =  food5, price = 169.99, ingredients = productsIngredients[4], description = productsDescriptions[4], favorite = true, ratingSize = 5.0f, ratingCount = 145, myRatingSize = 1.0f),
        ProductModel(id = "6", image = "drink1", name = "Şirinlerin Gargamelden Sakladığı İçeceği", category =  Category.Icecekler, color =  drink1, price = 149.99, ingredients = productsIngredients[5], description = productsDescriptions[5], favorite = true, ratingSize = 5.0f, ratingCount = 95, myRatingSize = 1.0f),
        ProductModel(id = "7", image = "drink2", name = "Marmaris'e Giden Dayının Hava Atma Bahanesi", category =  Category.Icecekler, color =  drink2, price = 174.99, ingredients = productsIngredients[6], description = productsDescriptions[6], favorite = false, ratingSize = 4.0f, ratingCount = 80, myRatingSize = 1.0f),
        ProductModel(id = "8", image = "drink3", name = "Enişte Adayının İlk Test Sonucu", category =  Category.Icecekler, color =  drink3, price = 149.99, ingredients = productsIngredients[7], description = productsDescriptions[7], favorite = true, ratingSize = 3.0f, ratingCount = 25, myRatingSize = 1.0f),
        ProductModel(id = "9", image = "drink4", name = "Komşunun Ağacından Çaldığımız Portakalların Hazin Sonu", category =  Category.Icecekler, color =  drink4, price = 74.99, ingredients = productsIngredients[8], description = productsDescriptions[8], favorite = false, ratingSize = 3.0f, ratingCount = 30, myRatingSize = 1.0f),
        ProductModel(id = "10", image = "drink5", name = "Akrabadan ÖdünÇaldığım Nanenin Son Durumu", category =  Category.Icecekler, color =  drink5, price = 99.99, ingredients = productsIngredients[9], description = productsDescriptions[9], favorite = true, ratingSize = 3.0f, ratingCount = 43, myRatingSize = 1.0f),
        ProductModel(id = "11", image = "coffeehouse1", name = "Türk Çayı", category = Category.Kahvehane, color =  coffeehouse1, price = 14.99, ingredients = null, description = null, favorite = true, ratingSize = 5.0f, ratingCount = 83, myRatingSize = 1.0f),
        ProductModel(id = "12", image = "coffeehouse2", name = "Kaçak Çay", category = Category.Kahvehane, color =  coffeehouse2, price = 14.99, ingredients = null, description = null, favorite = true, ratingSize = 5.0f, ratingCount = 76, myRatingSize = 1.0f),
        ProductModel(id = "13", image = "coffeehouse3", name = "Kiwi", category = Category.Kahvehane, color =  coffeehouse3, price = 14.99, ingredients = null, description = null, favorite = false, ratingSize = 5.0f, ratingCount = 54, myRatingSize = 1.0f),
        ProductModel(id = "14", image = "coffeehouse4", name = "Türk Kahvesi", category = Category.Kahvehane, color =  coffeehouse4, price = 49.99, ingredients = null, description = null, favorite = true, ratingSize = 5.0f, ratingCount = 132, myRatingSize = 1.0f),
        ProductModel(id = "15", image = "coffeehouse5", name = "Menengiç Kahvesi", category = Category.Kahvehane, color =  coffeehouse5, price = 59.99, ingredients = null, description = null, favorite = false, ratingSize = 5.0f, ratingCount = 63, myRatingSize = 1.0f),
        ProductModel(id = "16", image = "coffeehouse6", name = "Sütlü Kahve", category = Category.Kahvehane, color =  coffeehouse6, price = 39.99, ingredients = null, description = null, favorite = false, ratingSize = 5.0f, ratingCount = 28, myRatingSize = 1.0f),
        ProductModel(id = "17", image = "coffeehouse7", name = "Portakal Suyu", category = Category.Kahvehane, color =  coffeehouse7, price = 59.99, ingredients = null, description = null, favorite = true, ratingSize = 5.0f, ratingCount = 57, myRatingSize = 1.0f),
        ProductModel(id = "18", image = "coffeehouse8", name = "Nar Suyu", category = Category.Kahvehane, color =  coffeehouse8, price = 59.99, ingredients = null, description = null, favorite = false, ratingSize = 5.0f, ratingCount = 46, myRatingSize = 1.0f),
        ProductModel(id = "19", image = "coffeehouse9", name = "Üzüm Suyu", category = Category.Kahvehane, color =  coffeehouse9, price = 59.99, ingredients = null, description = null, favorite = false, ratingSize = 5.0f, ratingCount = 34, myRatingSize = 1.0f),
    )

    var _productsCarting  = mutableStateListOf<ProductModel>()

    // Tüm ürünleri getir
    override suspend fun getAllProducts(): Flow<List<ProductModel>> = withContext(Dispatchers.IO) {
        return@withContext flowOf(_products.toList())
    }

    // ID'ye göre ürün getir
    override suspend fun getProductById(productId: String): ProductModel? = withContext(Dispatchers.IO) {
        return@withContext _products.find { it.id == productId }
    }

    // Ürün ekle
    override suspend fun addProduct(product: ProductModel): String = withContext(Dispatchers.IO) {
        val newId = (_products.size + 1).toString()
        val productWithId = product.copy(id = newId)
        _products.add(productWithId)
        return@withContext newId
    }

    // Ürün güncelle
    override suspend fun updateProduct(product: ProductModel): Boolean = withContext(Dispatchers.IO) {
        val index = _products.indexOfFirst { it.id == product.id }
        return@withContext if (index != -1) {
            _products[index] = product
            true
        } else {
            false
        }
    }

    // Ürün sil
    override suspend fun deleteProduct(productId: String): Boolean = withContext(Dispatchers.IO) {
        val product = _products.find { it.id == productId }
        return@withContext if (product != null) {
            _products.remove(product)
            true
        } else {
            false
        }
    }

    // Ürünleri isme göre ara
    override suspend fun searchProducts(query: String): Flow<List<ProductModel>> = withContext(Dispatchers.IO) {
        return@withContext _products.filter {
            it.name!!.contains(query, ignoreCase = true)
        } as Flow<List<ProductModel>>
    }

    // Kategoriye göre ürünleri getir
    fun getProductsByCategory(category: Category): List<ProductModel> {
        return _products.filter { it.category == category }
    }

    // Favori ürünleri getir
    fun getFavoriteProducts(): List<ProductModel> {
        return _products.filter { it.favorite!! }
    }

    // Ürün favori durumunu değiştir
    fun toggleFavorite(productId: String): Boolean {
        val index = _products.indexOfFirst { it.id == productId }
        return if (index != -1) {
            val product = _products[index]
            _products[index] = product.copy(favorite = !product.favorite!!)
            true
        } else {
            false
        }
    }

}*/
