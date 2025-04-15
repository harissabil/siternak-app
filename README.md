# SiTernak

## Instalasi

1. Clone atau download proyek dan buka di Android Studio.
2. Buat proyek baru di Firebase Console dan tambahkan aplikasi ini.
3. Aktifkan Firebase Authentication dan tambahkan metode login dengan Email/Password dan Google.
4. Aktifkan Firestore Database.
5. Download file `google-services.json` dari Firebase Console dan letakkan di dalam folder `app/` proyek.
6. Buka `local.properties` dan tambahkan `WEB_CLIENT_ID` yang didapatkan dari Web SDK Configutation untuk Google Authentication pada Firebase Console.
   ```android
   ...
   
   WEB_CLIENT_ID=<YOUR_WEB_CLIENT_ID>
   ```
7. Sinkronisasi proyek dengan Gradle dan jalankan aplikasi melalui Emulator atau Device langsung.