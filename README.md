# ImgProc

## Opis

ImgProc to aplikacja napisana w języku Java, wykorzystująca JavaFX oraz bibliotekę OpenCV (wersja 4.9.0) do zaawansowanego przetwarzania obrazów. Aplikacja działa na systemie macOS i umożliwia użytkownikowi wczytywanie obrazów z lokalnego systemu plików oraz wykonywanie na nich różnych operacji przetwarzania.

## Funkcje

Aplikacja oferuje następujące funkcje przetwarzania obrazów:

- **Binaryzacja (HSV i YUV)**: Konwersja obrazu do postaci binarnej z użyciem przestrzeni kolorów HSV lub YUV.
- **Odejmowanie dwóch obrazów**: Różnica między dwoma obrazami.
- **Filtr górny**: Wyodrębnia krawędzie o wysokich wartościach gradientu.
- **Filtr dolny**: Wyodrębnia krawędzie o niskich wartościach gradientu.
- **Filtracja maksymalna**: Operacja filtracji, która zachowuje maksymalną wartość w oknie filtra.
- **Filtracja minimalna**: Operacja filtracji, która zachowuje minimalną wartość w oknie filtra.
- **Filtracja medianowa**: Filtracja obrazu na podstawie wartości medianowej w oknie filtra.
- **Wykrywanie krawędzi pionowej, ukośnej i poziomej**: Wykrywanie krawędzi w różnych kierunkach za pomocą filtrów konwolucyjnych.

## Wymagania

- **Java SDK 21 (ARM64)**: Środowisko uruchomieniowe dla aplikacji Java.
- **JavaFX**: Framework do budowy graficznego interfejsu użytkownika w Javie.
- **OpenCV 4.9.0**: Biblioteka do przetwarzania obrazów.

## Instalacja

1. **Klonowanie repozytorium**

   ```bash
   git clone https://github.com/Kofholik/ImgProcApp.git
   ```

2. **Zainstalowanie zależności**

   Upewnij się, że masz zainstalowane Java SDK 21 oraz JavaFX. Możesz pobrać JavaFX z [oficjalnej strony](https://openjfx.io/).

3. **Dodanie biblioteki OpenCV**

   Pobierz wersję OpenCV 4.9.0 dla macOS z [oficjalnej strony OpenCV](https://opencv.org/releases/) i dodaj ją do ścieżki klas w swoim projekcie.

4. **Budowanie projektu**

   Użyj systemu budowania, np. Gradle lub Maven, aby zbudować projekt. Upewnij się, że konfiguracja uwzględnia JavaFX oraz OpenCV.

   **Dla Gradle:**

   ```bash
   ./gradlew build
   ```

   **Dla Maven:**

   ```bash
   mvn package
   ```

5. **Uruchamianie aplikacji**

   Po zbudowaniu aplikacji, możesz ją uruchomić za pomocą następującego polecenia:

   ```bash
   java -cp build/libs/my-image-processing-app.jar com.yourpackage.Main
   ```

## Użycie

1. Uruchom aplikację.
2. Wybierz obraz, który chcesz przetworzyć, za pomocą przycisku "Wczytaj obraz".
3. Wybierz jedną z dostępnych funkcji przetwarzania i dostosuj parametry, jeśli to konieczne.
4. Zobacz przetworzony obraz w oknie podglądu.

## Kontrybucje

Jeśli chcesz przyczynić się do rozwoju aplikacji, zapraszam do przesyłania pull requestów lub zgłaszania problemów. Wszystkie propozycje i uwagi są mile widziane!

## Licencja

Projekt jest udostępniony na licencji [MIT](LICENSE). Proszę zapoznać się z plikiem LICENSE w celu uzyskania szczegółowych informacji.

## Kontakt

Jeśli masz pytania, skontaktuj się ze mną za pośrednictwem [GitHub Issues](https://github.com/Kofholik/ImgProcApp/issues)

---

Dziękuję za zainteresowanie moją aplikacją!
