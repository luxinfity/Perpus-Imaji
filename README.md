# Perpus-Imaji

Aplikasi Perpustakaan Sederhana. Aplikasi ini memiliki fitur utama:

- Setup akun admin
- Daftar Kategori
- Daftar Buku
- Daftar Anak / Member
- Daftar Peminjaman
- Trackable last edit
- Social media link
- Download recap data (tidak termasuk gambar) dalam bentuk xlsx

Demo App dapat dilihat pada [Play Store](https://play.google.com/store/apps/details?id=com.selasarimaji.perpus)

## How To Use

Aplikasi ini menggunakan layanan [firebase](firebase.com) oleh Google. Untuk menggunakannya:

1. Silahkan masukkan `google-services.json` ke dalam folder `/android/app/`
2. Deploy cloud functions code yang terdapat dalam `/web-functions`
4. Copy and paste alamat url funtions `downloadAllData` ke file [InfoFragment.kt](android/app/src/main/java/com/selasarimaji/perpus/view/fragment/InfoFragment.kt#L33)
3. Buat akun pada authentication agar dapat login ke dalam aplikasi

Layanan firebase yang digunakan:

- Authentication
- Firestore sebagai database
- Storage sebagai image folder
- Cloud Function sebagai _well, duh functions_

## In collaboration with

- Relawan program [Selasar Imaji](https://www.instagram.com/selasarimaji/) oleh Turun Tangan Bandung.
- Team developer dari [Telkom University, Mobile Innovation](https://www.instagram.com/motionlab_/), Bandung, Indonesia.

## Apps Screenshot

### Recycler

![alt text][category-recycler]
![alt text][book-recycler] 
![alt text][kid-recycler]
![alt text][borrow-recycler]
![alt text][inspect-recycler]

### Editing

![alt text][category-editing]
![alt text][book-editing] 
![alt text][kid-editing]
![alt text][borrow-editing]
![alt text][picker-editing]

### Misc

![alt text][login-misc] 
![alt text][reset-misc]
![alt text][search-misc]
![alt text][delete-misc]

## License

MIT

<!-- recycler -->
[category-recycler]: images/recycler/category.png "Category Recycler"
[book-recycler]: images/recycler/book.png "Book Recycler"
[kid-recycler]: images/recycler/kid.png "Kid Recycler"
[borrow-recycler]: images/recycler/borrow.png "Borrow Recycler"
[inspect-recycler]: images/recycler/inspect.png "Inspect Recycler"

<!-- editing -->
[category-editing]: images/editing/category.png "Category Editing"
[book-editing]: images/editing/book.png "Book Editing"
[kid-editing]: images/editing/kid.png "Kid Editing"
[borrow-editing]: images/editing/borrow.png "Borrow Editing"
[picker-editing]: images/editing/picker.png "Picker Editing"

<!-- misc -->
[delete-misc]: images/misc/delete.png "Delete Misc"
[login-misc]: images/misc/login.png "Login Misc"
[reset-misc]: images/misc/reset.png "Reset Misc"
[search-misc]: images/misc/search.png "Search Misc"
