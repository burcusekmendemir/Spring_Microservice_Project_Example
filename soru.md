## 1- 7071'de ayaga kalkacak bir "user-service" mikroservisi olusturalim. 
## Ve iki servisimiz icin de postgres baglantilarini gerceklestirelim. (java13AuthDB, java13UserDB)
## ODEV SORUSU : AUTH'da gerekli katmanlari ekleyelim. Sonrasinda bir "register()" metodu yazalim. register metodu
## DTO alsin ve geriye DTO donsun. Bunun icin gerekli mapper islemlerini de gerceklestirelim.

## 2- Disaridan login olmak icin gerekli parametreleri alalim. eger bilgiler dogru ise bize true, yanlis ise false donsun.

## 3- Validasyon islemlerini yapalim. Aklimiza gelen basit validasyonlari kullanalim.

## 4- Kullanıcının Status'unu aktif hale getirmek için aktivasyon kod doğrulaması yapan bir metot yazınız.

## 5- Auth'da status'u ACTIVE hale getirdigimizde user-service'deki status da aktif hale gelsin.

## 6- Login metodunu revize edelim. Bize bir token üretip o tokeni geri dönsün. Sadece aktif kullanıcılar login olabilsin.

## 7- User'da emaili değiştirdiğimde autda da değişsin istiyorum. Bunun için user-service'den -> auth-service'e bir feign client bağlantısı gerçekleştirelim.

## 8- Auth'da softDelete işlemi yaptığımda user tarafı da bu işlemden etkilensin

## 9- Delete methodunu her iki servis için de token ile çalışır hale getirelim.

## Uygulamamıza Redis'i entegre edelim. auth-service için configuration ayarlarını yapalım.
    Sonrasında redisExample isminde String alıp String dönen bir metotta Thread'ler yardımıyla redis'in çalışırlığını test edelim.