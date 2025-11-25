# 📋 Phân chia code chi tiết cho 3 thành viên

## 👤 Thành viên 1: Nguyễn Hồng Đông
**Nhiệm vụ:** Firebase, Screens (Splash, Menu, Favorite, Profile, Detail), Modules (Favorite, MealOverride, HealthMetrics)

### 📁 Files thuộc về Đông:

#### Firebase & Configuration
```
app/src/main/java/com/team/eatcleanapp/
├── di/
│   └── FirebaseModule.kt                    ✅ ĐÔNG
├── data/
│   └── repository/
│       └── (Firebase implementations)      ✅ ĐÔNG
└── google-services.json                     ✅ ĐÔNG (config file)
```

#### Screens
```
app/src/main/java/com/team/eatcleanapp/ui/screens/
├── splash/
│   └── SplashScreen.kt                     ✅ ĐÔNG
├── menu/
│   ├── MenuScreen.kt                       ✅ ĐÔNG
│   └── MenuViewModel.kt                    ✅ ĐÔNG
├── favorite/
│   ├── FavoriteScreen.kt                   ✅ ĐÔNG
│   └── FavoriteViewModel.kt                ✅ ĐÔNG
├── profile/
│   └── profile.kt                          ✅ ĐÔNG
└── meal/
    ├── DetailScreen.kt                     ✅ ĐÔNG
    └── DetailViewModel.kt                  ✅ ĐÔNG
```

#### Modules - Favorite
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── FavoriteDao.kt              ✅ ĐÔNG
│   │   └── entities/
│   │       └── FavoriteEntity.kt           ✅ ĐÔNG
│   ├── mapper/
│   │   └── FavoriteMapper.kt                ✅ ĐÔNG
│   └── repository/
│       └── FavoriteRepositoryImpl.kt       ✅ ĐÔNG
├── domain/
│   ├── model/
│   │   └── (Favorite related models)        ✅ ĐÔNG
│   ├── repository/
│   │   └── FavoriteRepository.kt             ✅ ĐÔNG
│   └── usecase/
│       └── favorite/
│           ├── AddToFavoriteUseCase.kt     ✅ ĐÔNG
│           ├── GetFavoriteMealsUseCase.kt   ✅ ĐÔNG
│           └── RemoveFromFavoriteUseCase.kt ✅ ĐÔNG
```

#### Modules - MealOverride
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── MealOverrideDao.kt          ✅ ĐÔNG (nếu có)
│   │   └── entities/
│   │       └── MealOverrideEntity.kt      ✅ ĐÔNG (nếu có)
│   └── repository/
│       └── MealOverrideRepositoryImpl.kt  ✅ ĐÔNG (nếu có)
├── domain/
│   ├── repository/
│   │   └── MealOverrideRepository.kt        ✅ ĐÔNG (nếu có)
│   └── usecase/
│       └── (MealOverride use cases)       ✅ ĐÔNG (nếu có)
```

#### Modules - HealthMetrics (phần hiển thị)
```
app/src/main/java/com/team/eatcleanapp/
├── domain/
│   ├── model/
│   │   └── HealthMetrics.kt                ⚠️ CHUNG (Đông làm phần hiển thị)
│   └── usecase/
│       └── health/
│           └── UpdateHealthMetricsUseCase.kt ⚠️ CHUNG
```

---

## 👤 Thành viên 2: Bá Trung Tín
**Nhiệm vụ:** API, Screens (Home, DailyMenu, AddMeal), Modules (DailyMenu, Meals, MealIntake)

### 📁 Files thuộc về Tín:

#### API & Network
```
app/src/main/java/com/team/eatcleanapp/
├── di/
│   └── NetworkModule.kt                     ✅ TÍN
├── data/
│   ├── remote/
│   │   ├── ApiService.kt                    ✅ TÍN
│   │   └── MealDto.kt                      ✅ TÍN
│   └── repository/
│       └── (API implementations)           ✅ TÍN
```

#### Screens
```
app/src/main/java/com/team/eatcleanapp/ui/screens/
├── home/
│   ├── HomeScreen.kt                       ✅ TÍN
│   └── HomeViewModel.kt                    ✅ TÍN
├── dailymenu/
│   ├── DailyMenuScreen.kt                  ✅ TÍN
│   └── DailyMenuViewModel.kt               ✅ TÍN
└── addmeal/
    ├── AddMealScreen.kt                    ✅ TÍN (nếu có)
    └── AddMealViewModel.kt                 ✅ TÍN (nếu có)
```

#### Modules - DailyMenu
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── DailyMenuDao.kt             ✅ TÍN
│   │   └── entities/
│   │       └── DailyMenuEntity.kt         ✅ TÍN
│   ├── mapper/
│   │   └── DailyMenuMapper.kt              ✅ TÍN
│   └── repository/
│       └── DailyMenuRepositoryImpl.kt     ✅ TÍN
├── domain/
│   ├── model/
│   │   └── DailyMenu.kt                    ✅ TÍN
│   ├── repository/
│   │   └── DailyMenuRepository.kt        ✅ TÍN
│   └── usecase/
│       └── dailymenu/
│           ├── AddMealsToDayUseCase.kt     ✅ TÍN
│           ├── DeleteDayMenuUseCase.kt    ✅ TÍN
│           ├── GetDailyMenuUseCase.kt      ✅ TÍN
│           ├── GetWeeklyMenuUseCase.kt     ✅ TÍN
│           └── UpdateMealIntakeUseCase.kt  ✅ TÍN
```

#### Modules - Meals
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── MealDao.kt                 ✅ TÍN
│   │   └── entities/
│   │       └── MealEntity.kt              ✅ TÍN
│   ├── mapper/
│   │   └── MealMapper.kt                  ✅ TÍN
│   └── repository/
│       └── MealRepositoryImpl.kt           ✅ TÍN
├── domain/
│   ├── model/
│   │   └── Meal.kt                         ✅ TÍN
│   ├── repository/
│   │   └── MealRepository.kt               ✅ TÍN
│   └── usecase/
│       └── meal/
│           ├── GetAllMealsUseCase.kt       ✅ TÍN
│           ├── GetMealDetailUseCase.kt     ✅ TÍN
│           ├── SearchMealsUseCase.kt       ✅ TÍN
│           └── AddToFavoriteUseCas.kt      ✅ TÍN
```

#### Modules - MealIntake
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── MealIntakeDao.kt           ✅ TÍN
│   │   └── entities/
│   │       └── MealIntakeEntity.kt        ✅ TÍN
│   ├── mapper/
│   │   └── MealIntakeMapper.kt             ✅ TÍN
│   └── repository/
│       └── MealIntakeRepositoryImpl.kt      ✅ TÍN
├── domain/
│   ├── model/
│   │   └── MealIntake.kt                   ✅ TÍN
│   ├── repository/
│   │   └── MealIntakeRepository.kt        ✅ TÍN
│   └── usecase/
│       └── mealintake/
│           └── (MealIntake use cases)      ✅ TÍN
```

---

## 👤 Thành viên 3: Hoàng Đình Minh Trinh
**Nhiệm vụ:** Screens (Login, Register, HealthMetrics, Goal), NavigationBar, Modules (User, Auth)

### 📁 Files thuộc về Trinh:

#### Screens - Auth
```
app/src/main/java/com/team/eatcleanapp/ui/screens/
├── auth/
│   ├── LoginScreen.kt                      ✅ TRINH
│   ├── RegisterScreen.kt                   ✅ TRINH
│   └── AuthViewModel.kt                     ✅ TRINH
```

#### Screens - Onboarding
```
app/src/main/java/com/team/eatcleanapp/ui/screens/
├── onboarding/
│   ├── HealthCalculatorScreen.kt           ✅ TRINH
│   └── GoalSelectionScreen.kt              ✅ TRINH (nếu có)
```

#### Navigation
```
app/src/main/java/com/team/eatcleanapp/ui/navigation/
├── BottomNavItem.kt                         ✅ TRINH
├── Destination.kt                            ✅ TRINH
├── Screen.kt                                 ✅ TRINH
└── (NavigationBar components)               ✅ TRINH
```

#### Modules - Auth
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   └── repository/
│       └── AuthRepositoryImpl.kt           ✅ TRINH
├── domain/
│   ├── repository/
│   │   └── AuthRepository.kt               ✅ TRINH
│   └── usecase/
│       └── auth/
│           ├── DeleteAccountUseCase.kt      ✅ TRINH
│           ├── LoginUseCase.kt              ✅ TRINH
│           ├── LogoutUseCase.kt            ✅ TRINH
│           └── RegisterUseCase.kt          ✅ TRINH
```

#### Modules - User
```
app/src/main/java/com/team/eatcleanapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── UserDao.kt                  ✅ TRINH
│   │   └── entities/
│   │       └── UserEntity.kt               ✅ TRINH
│   ├── mapper/
│   │   └── UserMapper.kt                    ✅ TRINH
│   └── repository/
│       └── UserRepositoryImpl.kt           ✅ TRINH
├── domain/
│   ├── model/
│   │   └── User.kt                          ✅ TRINH
│   ├── repository/
│   │   └── UserRepository.kt               ✅ TRINH
│   └── usecase/
│       └── user/
│           └── (User use cases)             ✅ TRINH
```

#### Components - Health Calculator
```
app/src/main/java/com/team/eatcleanapp/ui/components/
├── AgeSelector.kt                            ✅ TRINH
├── BMI.kt                                    ✅ TRINH
├── GenderSwitch.kt                           ✅ TRINH
├── InputWithUnit.kt                         ✅ TRINH
└── TDEE.kt                                   ✅ TRINH
```

---

## ⚠️ Files CHUNG (Cần thông báo trước khi sửa)

### Core Files
```
app/src/main/java/com/team/eatcleanapp/
├── MainActivity.kt                           ⚠️ CHUNG
├── EatCleanApplication.kt                   ⚠️ CHUNG
└── AppEatClean.kt                            ⚠️ CHUNG
```

### Database & DI
```
app/src/main/java/com/team/eatcleanapp/
├── data/local/
│   └── AppDatabase.kt                       ⚠️ CHUNG
├── di/
│   ├── DatabaseModule.kt                    ⚠️ CHUNG
│   ├── RepositoryModule.kt                  ⚠️ CHUNG
│   └── UseCaseModule.kt                     ⚠️ CHUNG
```

### Theme & Resources
```
app/src/main/java/com/team/eatcleanapp/ui/theme/
├── Color.kt                                  ⚠️ CHUNG
├── Theme.kt                                  ⚠️ CHUNG
└── Type.kt                                   ⚠️ CHUNG

app/src/main/res/
└── (Tất cả resources)                        ⚠️ CHUNG
```

### Utils
```
app/src/main/java/com/team/eatcleanapp/util/
├── Constants.kt                              ⚠️ CHUNG
├── DateUtils.kt                              ⚠️ CHUNG
├── NutritionCalculator.kt                   ⚠️ CHUNG
└── Result.kt                                  ⚠️ CHUNG
```

### Config Files
```
app/
├── build.gradle.kts                          ⚠️ CHUNG
├── AndroidManifest.xml                       ⚠️ CHUNG
└── google-services.json                      ⚠️ CHUNG (Đông quản lý nhưng cần thông báo)
```

---

## 📝 Quy tắc làm việc

### ✅ Được phép
- Sửa file thuộc phần của mình tự do
- Tạo file mới trong phần của mình
- Refactor code trong phần của mình

### ⚠️ Cần thông báo
- Sửa file CHUNG (MainActivity, AppDatabase, DI Modules, Theme, etc.)
- Thay đổi cấu trúc thư mục
- Thêm dependency mới vào build.gradle.kts
- Sửa AndroidManifest.xml

### ❌ Không được
- Sửa file của người khác mà không hỏi
- Xóa file của người khác
- Thay đổi interface/contract mà người khác đang dùng

---

## 🔄 Workflow đề xuất

### Tuần 1: Setup
- **Đông**: Setup Firebase, tạo SplashScreen
- **Tín**: Setup API, NetworkModule
- **Trinh**: Tạo Login/Register, NavigationBar

### Tuần 2-3: Core Features
- **Đông**: Menu, Favorite, Profile, Detail screens
- **Tín**: Home, DailyMenu screens, API integration
- **Trinh**: HealthMetrics, Goal screens, Auth flow

### Tuần 4: Integration
- Tất cả: Test integration, fix bugs
- Code review lẫn nhau
- Merge vào main branch

---

## 📞 Liên hệ khi cần

- **Cần sửa file CHUNG**: Báo trong group chat trước
- **Cần dùng code của người khác**: Hỏi trước khi sửa
- **Gặp conflict**: Liên hệ người tạo conflict để giải quyết

---

**Chúc team làm việc hiệu quả! 🚀**

