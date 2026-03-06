package com.bnjdpn.petitesgouttes

import android.app.Application
import com.bnjdpn.petitesgouttes.data.database.AppDatabase

class PetitesGouttesApp : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
}
