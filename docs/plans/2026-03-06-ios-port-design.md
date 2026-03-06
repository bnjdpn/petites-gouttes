# Petites Gouttes — iOS Port Design

Port 1:1 de l'app Android vers iOS, suivant les conventions des autres apps du portfolio.

## Stack technique

- Swift 6, iOS 17+, strict concurrency
- SwiftUI + @Observable (MVVM)
- SwiftData (local uniquement, pas de CloudKit)
- Swift Charts (graphiques barres)
- XcodeGen (project.yml source of truth)
- Fastlane (screenshots, metadata, deploy)
- Pas de DI framework

## Structure du projet

```
PetitesGouttes/
├── project.yml
├── fastlane/
│   ├── Appfile / Matchfile / Fastfile / Snapfile
│   ├── asc_api_key.json
│   └── metadata/fr-FR/
├── PetitesGouttes/
│   ├── App/PetitesGouttesApp.swift
│   ├── Models/MilkBag.swift
│   ├── ViewModels/ (Dashboard, Freezer, History, Stats, Settings)
│   ├── Views/ (Dashboard, Freezer, History, Stats, Settings, AddEditBag, Components)
│   ├── Navigation/ContentView.swift
│   ├── Theme/Theme.swift
│   └── Resources/Assets.xcassets
├── PetitesGouttesUITests/SnapshotTests.swift
```

## Modele de donnees

### MilkBag (@Model SwiftData)

| Champ | Type | Description |
|-------|------|-------------|
| volumeMl | Int | Volume en ml |
| pumpDate | Date | Date de pompage |
| expiryDate | Date | pumpDate + 4 mois (calcule) |
| removedFromFreezer | Bool | Sachet retire du congelateur |
| removalDate | Date? | Date de retrait |
| createdAt | Date | Date de creation |

### Preferences (@AppStorage)

| Cle | Type | Defaut |
|-----|------|--------|
| lowStockThresholdMl | Int | 1500 |
| dailyConsumptionMl | Int | 300 |
| daycareDaysPerWeek | Int | 5 |

## Ecrans & Navigation

TabView avec 4 onglets :

1. **Tableau de bord** (house) — compteur sachets, volume total, stock restant (jours), alertes, prochain sachet FIFO
2. **Congelateur** (snowflake) — liste triable (4 ordres), actions: editer/retirer/supprimer
3. **Historique** (clock) — sachets retires, restaurer/supprimer
4. **Statistiques** (chart.bar) — moyennes 7j/30j, totaux mensuels, graphiques Swift Charts, alerte baisse lactation

Navigation modale :
- Settings via icone engrenage toolbar
- AddEditBag via sheet modale depuis bouton "+" toolbar

## Logique metier

- FIFO : prochain sachet = plus ancien actif
- DLC : pumpDate + 4 mois
- Couleurs DLC : vert (#4CAF50 >30j), orange (#FF9800 14-29j), rouge (#E53935 <14j)
- Stock bas : volume total < lowStockThresholdMl
- DLC proche : sachets expirant dans 14 jours
- Jours de stock : totalVolume / dailyConsumption
- Baisse lactation : moyenne 7j < 80% moyenne 30j

## Theme

Palette rose/mauve identique a Android :
- Primary : #B4718A (light) / #FFB0D0 (dark)
- Secondary : #8B7091 (light) / #D4BDD9 (dark)

Adaptations iOS :
- Bouton "+" toolbar (pas de FAB)
- NavigationStack + .navigationTitle
- TabView natif
- .listStyle(.insetGrouped)
- Dark mode via @Environment(\.colorScheme)

## App Store

- Categorie : Health & Fitness
- Langue : FR uniquement
- Nom : Petites Gouttes
- Sous-titre : Gerez votre stock de lait maternel
- Mots-cles : lait maternel,congelateur,stock,allaitement,bebe,sachets,DLC,tire-lait,lactation,suivi
- Bundle ID : com.bnjdpn.petitesgouttes
- Team ID : 767SX34A7Z

Screenshots (iPhone 15 Pro Max, FR) :
1. Dashboard avec donnees exemple
2. Liste congelateur remplie
3. Statistiques avec graphiques
4. Ajout d'un sachet
5. Reglages
