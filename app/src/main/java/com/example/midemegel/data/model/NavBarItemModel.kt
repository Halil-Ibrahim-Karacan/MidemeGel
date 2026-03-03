package com.example.midemegel.data.model

/**
 * Alt navigasyon çubuğundaki (Bottom Navigation Bar) her bir öğeyi temsil eden model sınıfı.
 * @param title Öğenin altında görünen başlık.
 * @param selectedIcon Öğe seçili olduğunda gösterilecek ikonun kaynak ID'si.
 * @param unselectedIcon Öğe seçili olmadığında gösterilecek ikonun kaynak ID'si.
 */
data class NavBarItemModel(val title: String, val selectedIcon: Int, val unselectedIcon: Int)
