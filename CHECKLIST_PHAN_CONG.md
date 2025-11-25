# ✅ Checklist phân công công việc

## 👤 Thành viên 1: Nguyễn Hồng Đông

### Firebase Setup
- [ ] Cấu hình Firebase trong project
- [ ] Setup FirebaseModule.kt
- [ ] Cấu hình google-services.json
- [ ] Test Firebase connection

### Screens
- [ ] SplashScreen.kt
- [ ] MenuScreen.kt + MenuViewModel.kt
- [ ] FavoriteScreen.kt + FavoriteViewModel.kt
- [ ] ProfileScreen.kt (profile.kt)
- [ ] DetailScreen.kt + DetailViewModel.kt

### Modules - Favorite
- [ ] FavoriteDao.kt
- [ ] FavoriteEntity.kt
- [ ] FavoriteMapper.kt
- [ ] FavoriteRepository.kt + FavoriteRepositoryImpl.kt
- [ ] AddToFavoriteUseCase.kt
- [ ] GetFavoriteMealsUseCase.kt
- [ ] RemoveFromFavoriteUseCase.kt

### Modules - MealOverride (nếu có)
- [ ] MealOverrideDao.kt
- [ ] MealOverrideEntity.kt
- [ ] MealOverrideRepository.kt
- [ ] MealOverride use cases

---

## 👤 Thành viên 2: Bá Trung Tín

### API Setup
- [ ] Setup NetworkModule.kt
- [ ] Tạo ApiService.kt
- [ ] Tạo MealDto.kt
- [ ] Test API calls

### Screens
- [ ] HomeScreen.kt + HomeViewModel.kt
- [ ] DailyMenuScreen.kt + DailyMenuViewModel.kt
- [ ] AddMealScreen.kt (nếu có)

### Modules - DailyMenu
- [ ] DailyMenuDao.kt
- [ ] DailyMenuEntity.kt
- [ ] DailyMenuMapper.kt
- [ ] DailyMenuRepository.kt + DailyMenuRepositoryImpl.kt
- [ ] AddMealsToDayUseCase.kt
- [ ] DeleteDayMenuUseCase.kt
- [ ] GetDailyMenuUseCase.kt
- [ ] GetWeeklyMenuUseCase.kt
- [ ] UpdateMealIntakeUseCase.kt

### Modules - Meals
- [ ] MealDao.kt
- [ ] MealEntity.kt
- [ ] MealMapper.kt
- [ ] MealRepository.kt + MealRepositoryImpl.kt
- [ ] GetAllMealsUseCase.kt
- [ ] GetMealDetailUseCase.kt
- [ ] SearchMealsUseCase.kt

### Modules - MealIntake
- [ ] MealIntakeDao.kt
- [ ] MealIntakeEntity.kt
- [ ] MealIntakeMapper.kt
- [ ] MealIntakeRepository.kt + MealIntakeRepositoryImpl.kt
- [ ] MealIntake use cases

---

## 👤 Thành viên 3: Hoàng Đình Minh Trinh

### Screens - Auth
- [ ] LoginScreen.kt
- [ ] RegisterScreen.kt
- [ ] AuthViewModel.kt

### Screens - Onboarding
- [ ] HealthCalculatorScreen.kt
- [ ] GoalSelectionScreen.kt (nếu có)

### Navigation
- [ ] BottomNavItem.kt
- [ ] Destination.kt
- [ ] Screen.kt
- [ ] NavigationBar components

### Modules - Auth
- [ ] AuthRepository.kt + AuthRepositoryImpl.kt
- [ ] LoginUseCase.kt
- [ ] RegisterUseCase.kt
- [ ] LogoutUseCase.kt
- [ ] DeleteAccountUseCase.kt

### Modules - User
- [ ] UserDao.kt
- [ ] UserEntity.kt
- [ ] UserMapper.kt
- [ ] UserRepository.kt + UserRepositoryImpl.kt
- [ ] User use cases

### Components
- [ ] AgeSelector.kt
- [ ] BMI.kt
- [ ] GenderSwitch.kt
- [ ] InputWithUnit.kt
- [ ] TDEE.kt

---

## ⚠️ Files CHUNG (Cần thông báo trước)

### Core
- [ ] MainActivity.kt - **CẦN THÔNG BÁO**
- [ ] EatCleanApplication.kt - **CẦN THÔNG BÁO**
- [ ] AppEatClean.kt - **CẦN THÔNG BÁO**

### Database & DI
- [ ] AppDatabase.kt - **CẦN THÔNG BÁO**
- [ ] DatabaseModule.kt - **CẦN THÔNG BÁO**
- [ ] RepositoryModule.kt - **CẦN THÔNG BÁO**
- [ ] UseCaseModule.kt - **CẦN THÔNG BÁO**

### Theme
- [ ] Color.kt - **CẦN THÔNG BÁO**
- [ ] Theme.kt - **CẦN THÔNG BÁO**
- [ ] Type.kt - **CẦN THÔNG BÁO**

### Config
- [ ] build.gradle.kts - **CẦN THÔNG BÁO**
- [ ] AndroidManifest.xml - **CẦN THÔNG BÁO**

### Utils
- [ ] Constants.kt - **CẦN THÔNG BÁO**
- [ ] DateUtils.kt - **CẦN THÔNG BÁO**
- [ ] NutritionCalculator.kt - **CẦN THÔNG BÁO**
- [ ] Result.kt - **CẦN THÔNG BÁO**

---

## 📊 Tiến độ tổng thể

### Tuần 1: Setup
- [ ] Đông: Firebase setup
- [ ] Tín: API setup
- [ ] Trinh: Auth screens + Navigation

### Tuần 2-3: Core Features
- [ ] Đông: Menu, Favorite, Profile, Detail
- [ ] Tín: Home, DailyMenu, API integration
- [ ] Trinh: HealthMetrics, Goal screens

### Tuần 4: Integration & Testing
- [ ] Tất cả: Integration testing
- [ ] Tất cả: Code review
- [ ] Tất cả: Bug fixes
- [ ] Tất cả: Merge to main

---

## 📝 Ghi chú

- ✅ = Hoàn thành
- 🟡 = Đang làm
- ❌ = Chưa bắt đầu
- ⚠️ = Cần thông báo trước

---

**Cập nhật checklist này thường xuyên để team biết tiến độ! 📈**

