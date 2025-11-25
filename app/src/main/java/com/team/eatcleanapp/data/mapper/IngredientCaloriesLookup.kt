package com.team.eatcleanapp.data.mapper

/**
 * Bảng tra cứu calories per 100g cho các nguyên liệu phổ biến
 * Dùng để tính calories khi TheMealDB không cung cấp
 */
object IngredientCaloriesLookup {
    
    private val caloriesPer100g = mapOf(
        // Thịt
        "beef" to 250.0,
        "chicken" to 165.0,
        "pork" to 242.0,
        "lamb" to 294.0,
        "turkey" to 189.0,
        "duck" to 337.0,
        
        // Cá và hải sản
        "salmon" to 208.0,
        "tuna" to 144.0,
        "cod" to 82.0,
        "shrimp" to 99.0,
        "prawn" to 99.0,
        "crab" to 87.0,
        "lobster" to 89.0,
        "fish" to 100.0,
        "seafood" to 100.0,
        
        // Trứng và sữa
        "egg" to 155.0,
        "eggs" to 155.0,
        "milk" to 42.0,
        "cheese" to 300.0,
        "butter" to 717.0,
        "cream" to 345.0,
        "yogurt" to 59.0,
        
        // Rau củ
        "onion" to 40.0,
        "garlic" to 149.0,
        "tomato" to 18.0,
        "potato" to 77.0,
        "carrot" to 41.0,
        "broccoli" to 34.0,
        "spinach" to 23.0,
        "lettuce" to 15.0,
        "cabbage" to 25.0,
        "pepper" to 31.0,
        "bell pepper" to 31.0,
        "mushroom" to 22.0,
        "cucumber" to 16.0,
        "celery" to 16.0,
        "asparagus" to 20.0,
        "zucchini" to 17.0,
        "eggplant" to 25.0,
        
        // Trái cây
        "apple" to 52.0,
        "banana" to 89.0,
        "orange" to 47.0,
        "lemon" to 29.0,
        "lime" to 30.0,
        "strawberry" to 32.0,
        "blueberry" to 57.0,
        "avocado" to 160.0,
        
        // Ngũ cốc và tinh bột
        "rice" to 130.0,
        "pasta" to 131.0,
        "noodle" to 138.0,
        "bread" to 265.0,
        "flour" to 364.0,
        "oats" to 389.0,
        "quinoa" to 368.0,
        
        // Đậu và hạt
        "bean" to 347.0,
        "chickpea" to 364.0,
        "lentil" to 116.0,
        "peanut" to 567.0,
        "almond" to 579.0,
        "walnut" to 654.0,
        
        // Dầu và chất béo
        "oil" to 884.0,
        "olive oil" to 884.0,
        "vegetable oil" to 884.0,
        "coconut oil" to 862.0,
        "sesame oil" to 884.0,
        
        // Gia vị và thảo mộc
        "salt" to 0.0,
        "pepper" to 251.0,
        "sugar" to 387.0,
        "honey" to 304.0,
        "vinegar" to 18.0,
        "soy sauce" to 53.0,
        "ginger" to 80.0,
        "turmeric" to 354.0,
        "cumin" to 375.0,
        "coriander" to 23.0,
        "parsley" to 36.0,
        "basil" to 22.0,
        "oregano" to 265.0,
        "thyme" to 101.0,
        "rosemary" to 131.0,
        
        // Khác
        "water" to 0.0,
        "stock" to 5.0,
        "broth" to 5.0,
        "wine" to 83.0,
        "beer" to 43.0
    )
    
    /**
     * Tìm calories per 100g cho nguyên liệu
     * Tìm kiếm không phân biệt hoa thường và tìm partial match
     */
    fun getCaloriesPer100g(ingredientName: String): Double {
        val normalized = ingredientName.lowercase().trim()
        
        // Tìm exact match trước
        caloriesPer100g[normalized]?.let { return it }
        
        // Tìm partial match
        caloriesPer100g.entries.forEach { (key, value) ->
            if (normalized.contains(key) || key.contains(normalized)) {
                return value
            }
        }
        
        // Default: ước tính dựa trên loại nguyên liệu
        return estimateCalories(normalized)
    }
    
    /**
     * Ước tính calories dựa trên từ khóa trong tên nguyên liệu
     */
    private fun estimateCalories(ingredientName: String): Double {
        return when {
            ingredientName.contains("meat") || ingredientName.contains("beef") || 
            ingredientName.contains("pork") || ingredientName.contains("lamb") -> 250.0
            ingredientName.contains("chicken") || ingredientName.contains("poultry") -> 165.0
            ingredientName.contains("fish") || ingredientName.contains("salmon") || 
            ingredientName.contains("tuna") || ingredientName.contains("seafood") -> 150.0
            ingredientName.contains("egg") -> 155.0
            ingredientName.contains("cheese") || ingredientName.contains("dairy") -> 300.0
            ingredientName.contains("vegetable") || ingredientName.contains("veg") -> 30.0
            ingredientName.contains("fruit") -> 50.0
            ingredientName.contains("rice") || ingredientName.contains("grain") -> 130.0
            ingredientName.contains("pasta") || ingredientName.contains("noodle") -> 130.0
            ingredientName.contains("oil") || ingredientName.contains("fat") -> 884.0
            ingredientName.contains("sugar") || ingredientName.contains("honey") -> 300.0
            ingredientName.contains("nut") || ingredientName.contains("seed") -> 500.0
            else -> 50.0 // Default cho nguyên liệu không xác định
        }
    }
    
    /**
     * Parse unit và convert về gram nếu cần
     */
    fun convertToGrams(quantity: Double, unit: String): Double {
        val normalizedUnit = unit.lowercase().trim()
        
        return when {
            normalizedUnit.contains("kg") || normalizedUnit == "kilogram" -> quantity * 1000.0
            normalizedUnit.contains("g") || normalizedUnit == "gram" -> quantity
            normalizedUnit.contains("lb") || normalizedUnit == "pound" -> quantity * 453.592
            normalizedUnit.contains("oz") || normalizedUnit == "ounce" -> quantity * 28.3495
            normalizedUnit.contains("cup") -> quantity * 240.0 // Ước tính 1 cup = 240g
            normalizedUnit.contains("tbsp") || normalizedUnit.contains("tablespoon") -> quantity * 15.0
            normalizedUnit.contains("tsp") || normalizedUnit.contains("teaspoon") -> quantity * 5.0
            normalizedUnit.contains("ml") || normalizedUnit == "milliliter" -> quantity * 1.0 // Cho chất lỏng, giả sử density = 1
            normalizedUnit.contains("l") || normalizedUnit == "liter" -> quantity * 1000.0
            normalizedUnit.contains("piece") || normalizedUnit.contains("pcs") || 
            normalizedUnit.contains("whole") || normalizedUnit.isEmpty() -> {
                // Ước tính dựa trên loại nguyên liệu
                quantity * 100.0 // Default: 1 piece = 100g
            }
            else -> quantity * 100.0 // Default
        }
    }
}

