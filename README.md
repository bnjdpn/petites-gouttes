# Petites Gouttes

Application Android gratuite et open source pour g&eacute;rer un stock de pochettes de lait maternel congel&eacute;.

## Fonctionnalit&eacute;s

- **Gestion du stock** &mdash; ajout, modification et suppression de pochettes de lait
- **Suivi FIFO** &mdash; la pochette la plus ancienne est toujours sugg&eacute;r&eacute;e en premier
- **Alertes stock bas** &mdash; notification quand le stock passe sous le seuil configurable
- **Alertes DLC** &mdash; pochettes expirant dans les 14 prochains jours signal&eacute;es en rouge
- **Historique des sorties** &mdash; suivi des pochettes sorties du cong&eacute;lateur avec possibilit&eacute; d'annuler
- **Statistiques** &mdash; graphiques de volume par jour/semaine, moyennes, d&eacute;tection de baisse de lactation
- **Param&egrave;tres** &mdash; seuil d'alerte, consommation quotidienne et jours de garde configurables
- **Mode sombre** &mdash; th&egrave;me Material 3 avec support du mode sombre

## Screenshots

*&Agrave; venir*

## Installation

T&eacute;l&eacute;charger l'APK depuis la [derni&egrave;re release](https://github.com/bnjdpn/petites-gouttes/releases/latest) et l'installer sur un appareil Android 8.0+ (SDK 26).

## Stack technique

- Kotlin + Jetpack Compose
- Room (base de donn&eacute;es locale)
- Material 3
- DataStore Preferences
- Architecture MVVM

## Donn&eacute;es

Pas de compte, pas de serveur &mdash; toutes les donn&eacute;es restent sur l'appareil. Les donn&eacute;es sont persistantes entre les mises &agrave; jour de l'application.

## Build

```bash
# Java 17 requis
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew assembleRelease
```

L'APK est g&eacute;n&eacute;r&eacute; dans `app/build/outputs/apk/release/app-release.apk`.

## Licence

[MIT](LICENSE)
