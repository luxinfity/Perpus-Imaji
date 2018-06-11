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

## How To Use

Aplikasi ini menggunakan layanan [firebase](firebase.com) oleh Google. Untuk menggunakannya:

1. Silahkan masukkan `google-services.json` ke dalam folder `/android/app/`
2. Deploy cloud functions code yang terdapat dalam `/web-functions`
3. Buat akun pada authentication agar dapat login ke dalam aplikasi

Layanan firebase yang digunakan:

- Authentication
- Firestore sebagai database
- Storage sebagai image folder
- Cloud Function

## In collaboration with

[Selasar Imaji](https://www.instagram.com/selasarimaji/) oleh Turun Tangan Bandung.

## Apps Screenshot

### Recycler

![alt text][category-recycler]
![alt text][book-recycler] 
![alt text][kid-recycler]
![alt text][borrow-recycler]
![alt text][inspect-recycler]

## License

MIT

<!-- recycler -->
[category-recycler]: https://github.com/alifgiant/Perpus-Imaji/blob/v1-release/images/recycler/category.png "Category Recycler"
[book-recycler]: https://github.com/alifgiant/Perpus-Imaji/blob/v1-release/images/recycler/book.png "Book Recycler"
[kid-recycler]: https://github.com/alifgiant/Perpus-Imaji/blob/v1-release/images/recycler/kid.png "Kid Recycler"
[borrow-recycler]: https://github.com/alifgiant/Perpus-Imaji/blob/v1-release/images/recycler/borrow.png "Borrow Recycler"
[inspect-recycler]: https://github.com/alifgiant/Perpus-Imaji/blob/v1-release/images/recycler/inspect.png "Inspect Recycler"