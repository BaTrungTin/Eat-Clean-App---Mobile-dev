# Cách tắt màu xanh lá/xanh dương trong File Tree

## 🎯 Ý nghĩa màu sắc

- **Xanh lá**: File mới thêm vào Git (chưa commit)
- **Xanh dương**: File đã bị thay đổi (modified)
- **Trắng/Xám**: File không thay đổi
- **Đỏ**: File bị xóa hoặc conflict
- **Vàng**: File bị Git ignore

---

## 🔧 Cách tắt màu sắc

### Cách 1: Tắt File Status Colors (Khuyến nghị)

1. Mở **File** → **Settings** (hoặc `Ctrl + Alt + S`)
2. Tìm: **Appearance & Behavior** → **File Colors**
3. Bỏ chọn tất cả các scope hoặc xóa hết
4. Click **OK**

### Cách 2: Tắt VCS File Colors

1. Mở **File** → **Settings**
2. Tìm: **Version Control** → **File Status Colors**
3. Bỏ chọn tất cả:
   - ❌ Modified
   - ❌ Added
   - ❌ Deleted
   - ❌ Merged
   - ❌ etc.
4. Click **OK**

### Cách 3: Tắt Git Integration (Nếu không dùng Git)

1. Mở **File** → **Settings**
2. Tìm: **Version Control**
3. Trong **Directory**, chọn project
4. Click **-** (xóa) để bỏ Git integration
5. Click **OK**

---

## ⚡ Cách nhanh nhất

**Tắt tất cả màu sắc file status:**

1. `Ctrl + Alt + S` (mở Settings)
2. Gõ "file colors" vào thanh tìm kiếm
3. Bỏ chọn tất cả trong **File Colors**
4. Click **OK**

---

## 📝 Lưu ý

- Màu sắc giúp bạn biết trạng thái file trong Git
- Nếu tắt, bạn sẽ không biết file nào đã thay đổi
- Nên giữ lại để quản lý code tốt hơn

---

**Sau khi tắt, tất cả file sẽ hiển thị cùng một màu (trắng/xám)!**

