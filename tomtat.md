# Daily Menu - Tài liệu chức năng

## Tổng quan
Module Daily Menu quản lý thực đơn hàng ngày của người dùng, bao gồm các món ăn cho từng buổi (Sáng, Trưa, Tối) và các chức năng CRUD đầy đủ.

---

## 1. Domain Models

### 1.1. DailyMenuItem.kt
**Chức năng:** Model dữ liệu cho một món ăn trong thực đơn hàng ngày

**Thuộc tính:**
- `id`: ID duy nhất của món (tự động tạo UUID)
- `userId`: ID người dùng
- `date`: Ngày của thực đơn
- `mealId`: ID của món ăn
- `category`: Buổi ăn (BREAKFAST, LUNCH, DINNER)
- `mealName`: Tên món ăn
- `calories`: Số calo trên 1 phần
- `quantity`: Số phần ăn
- `unit`: Đơn vị (ví dụ: "phần")

**Tính năng:**
- `totalCalories`: Tính tổng calo = calories * quantity

**Các class/enum liên quan:**
- `DateCategory`: Cặp ngày + category để xóa
- `MealCategory`: Enum các buổi ăn (BREAKFAST, LUNCH, DINNER)

---

### 1.2. DailyMenuDay.kt
**Chức năng:** Model dữ liệu cho thực đơn của 1 ngày

**Thuộc tính:**
- `date`: Ngày
- `breakfast`: Danh sách món ăn buổi sáng
- `lunch`: Danh sách món ăn buổi trưa
- `dinner`: Danh sách món ăn buổi tối

**Tính năng:**
- `totalCalories`: Tính tổng calo của cả ngày (tổng của 3 buổi)

---

### 1.3. DailyMenuWeek.kt
**Chức năng:** Model dữ liệu cho thực đơn của cả tuần

**Thuộc tính:**
- `days`: Danh sách các ngày trong tuần (List<DailyMenuDay>)

---

## 2. Repository Layer

### 2.1. DailyMenuRepository.kt (Interface)
**Chức năng:** Định nghĩa các phương thức để tương tác với dữ liệu daily menu

**Các phương thức:**

#### Lấy dữ liệu:
- `getDailyMenuItemById(id)`: Lấy 1 món theo ID
- `getDailyMenuByDate(userId, date)`: Lấy thực đơn theo ngày
- `getDailyMenuByWeek(userId, startDate, endDate)`: Lấy thực đơn theo tuần
- `getMealsByCategory(userId, date, category)`: Lấy món theo buổi trong ngày
- `getTotalCaloriesByDate(userId, date)`: Tính tổng calo 1 ngày
- `getTotalCaloriesByCategory(userId, date, category)`: Tính tổng calo 1 buổi
- `mealExists(userId, date, mealId, category)`: Kiểm tra món có tồn tại không
- `getMealCountByCategory(userId, date, category)`: Đếm số món trong 1 buổi
- `getAllUsedMealIds()`: Lấy tất cả mealId đang được dùng

#### Thêm/Sửa:
- `insertDailyMenu(dailyMenuItem)`: Thêm 1 món
- `insertMultipleDailyMenus(dailyMenuItems)`: Thêm nhiều món
- `updateDailyMenu(dailyMenuItem)`: Cập nhật thông tin món
- `updateQuantity(id, newQuantity)`: Cập nhật số phần ăn
- `updateDailyMenuMealInfo(mealId, newMealName, newCalories)`: Cập nhật khi meal thay đổi

#### Xóa:
- `deleteDailyMenuItem(id)`: Xóa 1 món
- `deleteCategory(userId, date, category)`: Xóa 1 buổi của 1 ngày
- `deleteMultipleCategoriesOfDate(userId, date, categories)`: Xóa nhiều buổi của 1 ngày
- `deleteSelectedCategories(userId, selectedDateCategories)`: Xóa nhiều buổi của nhiều ngày
- `deleteAllByDate(userId, date)`: Xóa 1 ngày
- `deleteSelectedDates(userId, selectedDates)`: Xóa nhiều ngày
- `deleteWeek(userId, weekStartDate, weekEndDate)`: Xóa cả tuần
- `deleteDailyMenuByMealId(mealId)`: Xóa khi meal bị xóa
- `deleteAllByUserId(userId)`: Xóa toàn bộ của user

---

### 2.2. DailyMenuRepositoryImpl.kt
**Chức năng:** Triển khai các phương thức của DailyMenuRepository

**Tính năng:**
- Chuyển đổi giữa Domain Model và Entity
- Xử lý lỗi và trả về Result
- Tối ưu hóa xóa nhiều buổi (nhóm theo ngày, xóa cả tuần)

**Các phương thức helper:**
- `executeDatabaseOperation()`: Bọc database operation trong try-catch
- `deleteFullWeekCategories()`: Xóa hiệu quả khi xóa cả tuần
- `deleteDateCategoriesOneByOne()`: Xóa từng ngày khi không phải cả tuần
- `toDomain()`: Chuyển Entity sang Domain Model
- `toEntity()`: Chuyển Domain Model sang Entity

---

## 3. Data Layer

### 3.1. DailyMenuDao.kt
**Chức năng:** Room DAO để truy vấn database

**Các query:**
- Lấy dữ liệu: `getDailyMenuByDate`, `getDailyMenusByDateRange`, `getMealsByCategory`, `getDailyMenuItemById`
- Tính toán: `getTotalCaloriesByDate`, `getTotalCaloriesByCategoryAndDate`, `getMealCountByCategoryAndDate`, `mealExists`
- Thêm/Sửa: `insertMeal`, `insertMeals`, `updateMeal`, `updateDailyMenuMealInfo`, `updateQuantity`
- Xóa: `deleteMealById`, `deleteAllByDate`, `deleteCategoryOfDate`, `deleteMultipleCategoriesOfDate`, `deleteCategoriesAcrossDates`, `deleteSelectedDates`, `deleteDailyMenuByMealId`, `deleteAllByUserId`

---

### 3.2. DailyMenuEntity.kt
**Chức năng:** Entity cho Room database

**Thuộc tính:**
- `id`: Primary key
- `userId`: ID người dùng
- `date`: Ngày (String format "yyyy-MM-dd")
- `mealId`: ID món ăn
- `mealName`: Tên món
- `calories`: Số calo
- `quantity`: Số phần ăn
- `unit`: Đơn vị
- `category`: Buổi ăn (String: "BREAKFAST", "LUNCH", "DINNER")

**Index:**
- `[userId, date]`: Để tìm nhanh theo user và ngày
- `[userId, date, mealId]`: Để tìm nhanh theo user, ngày và món

---

## 4. Use Cases

### 4.1. GetDailyMenuUseCase.kt
**Chức năng:** Lấy thực đơn của 1 ngày

**Input:**
- `userId`: ID người dùng
- `date`: Ngày cần lấy

**Output:** `Result<List<DailyMenuItem>>`

**Validation:** Kiểm tra userId không được để trống

---

### 4.2. GetWeeklyMenuUseCase.kt
**Chức năng:** Lấy thực đơn của cả tuần (7 ngày)

**Input:**
- `userId`: ID người dùng
- `date`: Ngày bất kỳ trong tuần

**Output:** `Result<Map<Date, List<DailyMenuItem>>>`

**Tính năng:**
- Tự động tính tuần hợp lệ từ ngày cho
- Nhóm thực đơn theo ngày
- Validation tuần hợp lệ

---

### 4.3. GetTotalCaloriesByDateUseCase.kt
**Chức năng:** Lấy tổng calo của 1 ngày

**Input:**
- `userId`: ID người dùng
- `date`: Ngày cần tính

**Output:** `Result<Double?>` (null nếu không có món nào)

**Sử dụng:** Hiển thị tổng calo cần nạp / đã nạp / còn lại trong ngày

---

### 4.4. GetTotalCaloriesByCategoryUseCase.kt
**Chức năng:** Lấy tổng calo của 1 buổi trong 1 ngày

**Input:**
- `userId`: ID người dùng
- `date`: Ngày
- `category`: Buổi ăn (BREAKFAST, LUNCH, DINNER)

**Output:** `Result<Double?>` (null nếu không có món nào)

**Sử dụng:** Hiển thị tổng calo của từng buổi (Sáng, Trưa, Tối)

---

### 4.5. AddMealsToDayUseCase.kt
**Chức năng:** Thêm nhiều món ăn vào 1 buổi của 1 ngày (quantity mặc định = 1.0)

**Input:**
- `userId`: ID người dùng
- `date`: Ngày
- `mealIds`: Danh sách ID món ăn
- `mealCategory`: Buổi ăn

**Output:** `Result<Unit>`

**Tính năng:**
- Lấy thông tin món từ MealRepository
- Tạo DailyMenuItem với quantity = 1.0
- Thêm nhiều món cùng lúc

---

### 4.6. AddMealToDailyMenuUseCase.kt
**Chức năng:** Thêm 1 món ăn với quantity tùy chỉnh

**Input:**
- `userId`: ID người dùng
- `date`: Ngày
- `mealId`: ID món ăn
- `category`: Buổi ăn
- `quantity`: Số phần ăn (tùy chỉnh)

**Output:** `Result<Unit>`

**Sử dụng:** Khi thêm món từ màn hình mealdetail (popup chọn ngày, buổi, số phần ăn)

**Validation:**
- User ID không được để trống
- Meal ID không được để trống
- Quantity phải > 0

---

### 4.7. UpdateMealQuantityUseCase.kt
**Chức năng:** Cập nhật số phần ăn (quantity) của 1 món

**Input:**
- `id`: ID của DailyMenuItem
- `newQuantity`: Số phần ăn mới

**Output:** `Result<Unit>`

**Sử dụng:** Khi chỉnh sửa phần ăn từ màn hình dailymenu

**Validation:**
- ID không được để trống
- Quantity phải > 0

---

### 4.8. UpdateMealIntakeUseCase.kt
**Chức năng:** Cập nhật trạng thái đã ăn món (checked/unchecked)

**Input:**
- `mealIntakeId`: ID của MealIntake
- `isChecked`: Đã ăn hay chưa

**Output:** `Result<Unit>`

**Sử dụng:** Khi tick vào ô check trong màn hình home

---

### 4.9. DeleteDayMenuUseCase.kt
**Chức năng:** Xóa toàn bộ thực đơn của 1 ngày

**Input:**
- `userId`: ID người dùng
- `date`: Ngày cần xóa
- `isConfirmed`: Xác nhận xóa

**Output:** `Result<Unit>`

**Validation:**
- User ID không được để trống
- Phải có xác nhận mới xóa

---

### 4.10. DeleteMultipleMealsUseCase.kt
**Chức năng:** Xóa nhiều món ăn theo danh sách ID

**Input:**
- `mealIds`: Danh sách ID món cần xóa

**Output:** `Result<Unit>`

**Sử dụng:** Khi xóa nhiều món đã chọn từ màn hình dailymenu

**Tính năng:** Xóa từng món, trả về lỗi cuối cùng nếu có

---

### 4.11. DeleteSelectedMealsUseCase.kt
**Chức năng:** Xóa các món/ngày đã chọn (linh hoạt)

**Input:**
- `userId`: ID người dùng
- `mealIds`: Danh sách ID món (optional)
- `dateCategories`: Danh sách DateCategory - xóa nhiều buổi của nhiều ngày (optional)
- `dates`: Danh sách ngày - xóa nhiều ngày (optional)

**Output:** `Result<Unit>`

**Sử dụng:** Xóa linh hoạt theo nhiều cách:
- Xóa nhiều món theo ID
- Xóa nhiều buổi của nhiều ngày
- Xóa nhiều ngày

**Validation:**
- User ID không được để trống
- Phải chọn ít nhất 1 mục để xóa

---

## 5. Tổng kết chức năng

### ✅ Đã hoàn thành:

#### Lấy dữ liệu:
- ✅ Lấy thực đơn theo ngày
- ✅ Lấy thực đơn theo tuần
- ✅ Lấy món theo buổi
- ✅ Tính tổng calo 1 ngày
- ✅ Tính tổng calo 1 buổi
- ✅ Đếm số món trong buổi

#### Thêm:
- ✅ Thêm 1 món với quantity tùy chỉnh
- ✅ Thêm nhiều món với quantity mặc định

#### Sửa:
- ✅ Cập nhật số phần ăn
- ✅ Cập nhật trạng thái đã ăn

#### Xóa:
- ✅ Xóa 1 món
- ✅ Xóa 1 buổi của 1 ngày
- ✅ Xóa nhiều buổi của 1 ngày
- ✅ Xóa nhiều buổi của nhiều ngày
- ✅ Xóa 1 ngày
- ✅ Xóa nhiều ngày
- ✅ Xóa cả tuần
- ✅ Xóa toàn bộ của user

---



