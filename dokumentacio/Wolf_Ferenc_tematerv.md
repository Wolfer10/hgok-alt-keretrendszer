# Tématerv



## Személyes adatok

Név: Wolf Ferenc

Neptun: ILVG04

E-mail: h986172@stud.u-szeged.hu

Szak: programtervező informatikus BSc nappali

Végzés várható ideje: 2022. nyár



## A szakdolgozat tárgya

Általános hívási gráf összehasonlító és kiértékelő keretrendszer, ami segítségével tetszőleges, előre rögzített JSON kimenetet generáló (hívási) gráf készítő alkalmazást hasonlíthatunk össze.

Az alkalmazás képes lesz elemzéseket futtatni/ütemezni egy megfelelően előkészített számítógépen. Az elemzéshez tetszőleges eszközkombinációt kiválaszthatunk, tetszőleges inputtal.

Az elkészült elemzésekről értesítést küld, és egy dashboard felületen grafikusan láthatjuk az elemzés eredményét. Képes lesz riportot készíteni az elemzésből (egyes eszközök futási ideje, max. felhasznált memória, output mérete, talált csomópontok/élek száma), ezeket vizuálisan is megjeleníti a felületen.

Az alkalmazás másik modulja pedig a kiértékelő modul. Itt elkészült elemzéseket validálhatunk kézzel. A kiértékelő modul automatikusan meghatározza azokat az éleket, amelyeket ki kell értékelnünk, ezeket pedig összehasonlító felületen vizsgálhatjuk meg, és validálhatjuk. A kiértékelés végén a rendszer automatikusan meghatározza a hagyományos IR metrikákat.

A kiértékelő modul a kiértékelt élek alapján tartalmazna egy döntéstámogató rendszert is, ami segítene a kézi validálás során, ehhez statisztikai módszereket, valamint NLP algoritmust használna.

## Használni kívánt technológiák

 Java, Spring Boot, Thymeleaf


## Tervezett ütemezés

- **2021. szeptember:**  Képernyő tervek, Alkalmazás tervek, Implementáció elkezdése
- **2021. október:** eszközök felvétele, adatbázis, ütemezés/indítás
- **2021. november:** eredmények megjelenítése grafikusan,
- **2021. december:** hiba javítások fixálások, kiértékelés elkezdése
- **2022. január:** kiértékelés befejezése, statisztika, riport, eredmény megjelenítése
- **2022. február:** riport mentés/exportálás befejezése, "mesterséges inteligencia rész", 
- **2022. március:** dolgozat írásának megkezdése
- **2022. április:** dolgozat írásának befejezése, hibajavítás