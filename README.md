# Dylicious

*Dylicious* adalah aplikasi Android yang memungkinkan pengguna untuk melihat berbagai menu makanan dan menyimpannya ke dalam keranjang sebagai daftar makanan favorit atau rencana pesanan.  
Aplikasi ini dirancang dengan antarmuka sederhana dan intuitif, tanpa fitur pembayaran, sehingga pengguna hanya fokus pada pencarian dan pengelolaan menu makanan yang mereka sukai.

---

## Fitur

- Pencarian menu makanan berdasarkan nama.
- Daftar menu langsung ditampilkan di halaman utama (Home).
- Halaman detail untuk setiap menu, berisi informasi lengkap dan opsi untuk menambah ke keranjang.
- *Keranjang*:
  - Tambah/kurangi jumlah pesanan per menu.
  - Estimasi total harga dan waktu penyajian otomatis diperbarui.
  - Hapus menu satu per satu atau sekaligus hapus semua.
- *Pengaturan*:
  - Ganti tema aplikasi (Light/Dark Mode).
  - Lihat informasi tentang aplikasi.

---

## Penggunaan

1. Luncurkan aplikasi Dylicious di perangkat Android Anda.
2. Pada halaman awal (Home), daftar menu makanan langsung ditampilkan.
3. Gunakan fitur pencarian untuk mencari menu berdasarkan nama.
4. Klik salah satu menu untuk melihat detail informasi makanan.
5. Tekan tombol *"Tambahkan ke Keranjang"* untuk menyimpan menu ke keranjang.
6. Buka tab *Keranjang* untuk melihat dan mengelola daftar makanan yang disimpan.
7. Di halaman keranjang, Anda dapat:
   - Menambah/mengurangi jumlah pesanan.
   - Melihat total harga dan estimasi waktu penyajian yang otomatis diperbarui.
   - Menghapus item tertentu atau semua item sekaligus.
8. Masuk ke menu *Pengaturan* untuk mengganti tema aplikasi dan melihat informasi tentang aplikasi.

---

## Implementasi Teknis

Dylicious dikembangkan sebagai aplikasi Android berbasis *Java* yang mengintegrasikan beberapa komponen berikut:

- *API Integration*:  
  Aplikasi mengambil data menu makanan dari API eksternal melalui *RapidAPI, menggunakan **Retrofit* dan *Gson* untuk parsing data JSON.

- *Penyimpanan Lokal (Keranjang)*:  
  Menggunakan *SQLite* untuk menyimpan data menu yang ditambahkan ke keranjang, seperti:
  - ID menu
  - Nama
  - Deskripsi
  - Gambar
  - Harga
  - Pajak
  - Jumlah pesanan (quantity)

  Semua proses insert, update, dan delete dilakukan melalui query SQL untuk memastikan efisiensi dan ketersediaan data secara offline.

- *Tema Gelap/Terang*:  
  Dukungan tema terang dan gelap yang dapat diatur oleh pengguna. Preferensi tema disimpan menggunakan *SharedPreferences*, sehingga akan otomatis diterapkan kembali saat aplikasi dijalankan ulang.

---


## Catatan

Aplikasi ini tidak memiliki fitur pembayaran. Dylicious hanya digunakan sebagai alat bantu pengguna untuk merencanakan makanan favorit mereka.