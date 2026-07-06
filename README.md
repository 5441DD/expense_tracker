Expense Tracker

Kategori bazlı, grafikli ve tarih filtreli bir gelir/gider takip uygulaması. Android üzerinde Java ile geliştirildi, harcamaları kategorilere ayırarak görsel bir bütçe analizi sunar.

Özellikler


Kategori bazlı gelir/gider takibi — her işlem bir kategoriye bağlanır
Pasta grafik ile görsel analiz — kategori dağılımını anlık olarak grafikte gör
Günlük / Aylık / Yıllık tarih filtreleme — seçilen döneme göre veriler otomatik güncellenir
Özel kategori oluşturma — kullanıcı kendi kategorisini istediği renkle oluşturabilir
Room veritabanı ile kalıcı veri saklama — uygulama kapansa da veriler cihazda saklı kalır


Ekran Görüntüleri

<img width="1080" height="2400" alt="Screenshot1" src="https://github.com/user-attachments/assets/56d7fe29-0c78-46d5-9fea-10196ef463f9" />

<img width="1080" height="2400" alt="Screenshot2" src="https://github.com/user-attachments/assets/6236a67a-4286-4439-b282-bd3e38ef6f7f" />


Kullanılan Teknolojiler


Java — uygulama dili
Room — SQLite tabanlı yerel veritabanı
MPAndroidChart — pasta grafik görselleştirmesi
Fragment mimarisi — Grafik ve Liste ekranları arasında tek Activity üzerinden geçiş
Material Components — Chip, MaterialButtonToggleGroup, BottomNavigationView gibi modern UI bileşenleri


Mimari

Uygulama, tek bir MainActivity üzerinde çalışan iki Fragment (ChartFragment ve ListFragment) ile tasarlandı. Veritabanı bağlantısı MainActivity seviyesinde tutulur, Fragment'lar bu bağlantıyı paylaşarak kendi ekranlarını günceller. Tarih filtresi (Günlük/Aylık/Yıllık) MainActivity'den yönetilir ve aktif olan Fragment'a haber verilerek ekran anlık olarak yenilenir.

MainActivity (veritabanı bağlantısı + tarih filtresi)
 ├── ChartFragment (pasta grafik + gelir/gider ekleme)
 └── ListFragment (işlem listesi)

