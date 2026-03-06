<div align="center">

# 🍼 Petites Gouttes

**Gérez votre stock de lait maternel congelé en toute simplicité.**

[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![iOS](https://img.shields.io/badge/iOS-17%2B-000000?logo=apple&logoColor=white)](https://apps.apple.com/app/petites-gouttes/id6760202126)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Swift](https://img.shields.io/badge/Swift-6-F05138?logo=swift&logoColor=white)](https://swift.org)
[![License: MIT](https://img.shields.io/badge/Licence-MIT-yellow.svg)](LICENSE)

App gratuite et open source — aucune inscription, aucun serveur, toutes les données restent sur votre appareil.

[![App Store](https://img.shields.io/badge/App%20Store-Télécharger-0D96F6?logo=apple&logoColor=white)](https://apps.apple.com/app/petites-gouttes/id6760202126)
[Télécharger l'APK](https://github.com/bnjdpn/petites-gouttes/releases/latest)

</div>

---

## ✨ Fonctionnalités

| | Fonctionnalité | Description |
|---|---|---|
| 📦 | **Gestion du stock** | Ajout, modification et suppression de pochettes de lait |
| 🔄 | **Suivi FIFO** | La pochette la plus ancienne est toujours suggérée en premier |
| ⚠️ | **Alertes stock bas** | Notification quand le stock passe sous le seuil configurable |
| 🔴 | **Alertes DLC** | Pochettes expirant dans les 14 prochains jours signalées en rouge |
| 📋 | **Historique** | Suivi des pochettes sorties du congélateur avec possibilité d'annuler |
| 📊 | **Statistiques** | Graphiques de volume par jour/semaine, moyennes, détection de baisse de lactation |
| ⚙️ | **Paramètres** | Seuil d'alerte, consommation quotidienne et jours de garde configurables |

## 🏗️ Stack technique

```
Kotlin · Jetpack Compose · Room · Material 3 · DataStore · MVVM
```

- **UI** — Jetpack Compose avec Material 3
- **Base de données** — Room (SQLite, stockage local)
- **Préférences** — DataStore Preferences
- **Architecture** — MVVM, pas de framework d'injection de dépendances

## 📥 Installation

Téléchargez l'APK depuis la [dernière release](https://github.com/bnjdpn/petites-gouttes/releases/latest) et installez-le sur un appareil **Android 8.0+** (SDK 26).

## 🔨 Build

> Requiert **Java 17**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew assembleRelease
```

L'APK signé est généré dans `app/build/outputs/apk/release/app-release.apk`.

## 📁 Structure du projet

```
app/src/main/java/com/bnjdpn/petitesgouttes/
├── data/
│   ├── database/       # Room : AppDatabase, MilkBagEntity, MilkBagDao
│   ├── preferences/    # DataStore : SettingsDataStore
│   └── repository/     # MilkBagRepository
├── viewmodel/          # ViewModels (Dashboard, Freezer, History, Stats, Settings)
├── ui/
│   ├── screens/        # Écrans principaux
│   ├── components/     # Composants réutilisables
│   ├── navigation/     # Navigation bottom bar + routes
│   └── theme/          # Thème Material 3
├── MainActivity.kt
└── PetitesGouttesApp.kt
```

## 🤝 Contribuer

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une *issue* ou une *pull request*.

## 📄 Licence

Ce projet est distribué sous licence [MIT](LICENSE).
