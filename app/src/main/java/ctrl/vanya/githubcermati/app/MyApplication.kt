package ctrl.vanya.githubcermati.app

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }

}