# Petites Gouttes 🍼

App Android gratuite et open source pour gérer un stock de pochettes de lait maternel congelé.

## Fonctionnalités

- **Gestion du stock** — ajout, modification et suppression de pochettes de lait
- **Suivi FIFO** — la pochette la plus ancienne est toujours suggérée en premier
- **Alertes stock bas** — notification quand le stock passe sous le seuil configurable
- **Alertes DLC** — pochettes expirant dans les 14 prochains jours signalées en rouge
- **Historique des sorties** — suivi des pochettes sorties du congélateur avec possibilité d'annuler
- **Statistiques** — graphiques de volume par jour/semaine, moyennes, détection de baisse de lactation
- **Paramètres** — seuil d'alerte, consommation quotidienne et jours de garde configurables

## Installation

Télécharger l'APK depuis la [dernière release](https://github.com/bnjdpn/petites-gouttes/releases/latest) et l'installer sur un appareil Android 8.0+ (SDK 26).

## Stack technique

- Kotlin + Jetpack Compose
- Room (base de données locale)
- Material 3
- DataStore Preferences
- Architecture MVVM
- Pas de compte, pas de serveur — toutes les données restent sur l'appareil

## Build

```bash
# Java 17 requis
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew assembleRelease
```

L'APK est généré dans `app/build/outputs/apk/release/app-release.apk`.

## Licence

[MIT](LICENSE)
