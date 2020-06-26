package ctrl.vanya.githubcermati.data.source.remote

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import ctrl.vanya.githubcermati.app.MyApplication
import ctrl.vanya.githubcermati.data.utils.TLSSocketFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

object ApiClient {
    private val mContext: Context? = null
    private const val TAG = "RetrofitManager"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_PRAGMA = "Pragma"
    private var mRetrofit: Retrofit? = null
    private var mCachedRetrofit: Retrofit? = null
    private var mCache: Cache? = null
    private var mOkHttpClient: OkHttpClient? = null
    private var mCachedOkHttpClient: OkHttpClient? = null

    @SuppressLint("ObsoleteSdkInt")
    fun retrofitClient(
        url: String): Retrofit {

        val okHttpBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val hostnameVerifier = HostnameVerifier { hostname, session ->
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            hv.verify(hostname, session)
        }

        okHttpBuilder.hostnameVerifier(hostnameVerifier)

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(MyApplication.appContext!!.cacheDir, cacheSize)

        okHttpBuilder.addInterceptor(createLoggingInterceptor())
            .cache(myCache)
            .pingInterval(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .addNetworkInterceptor(provideCacheInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor())
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                okHttpBuilder.sslSocketFactory(TLSSocketFactory())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val okHttpClient = okHttpBuilder.build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    private fun isConnected(): Boolean {
        try {
            val e = mContext!!.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork: NetworkInfo? = e.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (e: java.lang.Exception) {
            Log.w("Api Client", e.toString())
        }
        return false
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val cacheControl: CacheControl = if (isConnected()) {
                CacheControl.Builder()
                    .maxAge(2, TimeUnit.HOURS)
                    .build()
            } else {
                CacheControl.Builder()
                    .maxStale(2, TimeUnit.HOURS)
                    .build()
            }
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request: Request = chain.request()
            if (!isConnected()) {
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(2, TimeUnit.HOURS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    fun clean() {
        if (mOkHttpClient != null) {
            // Cancel Pending Request

            mOkHttpClient!!.dispatcher().cancelAll()
        }
        if (mCachedOkHttpClient != null) {
            // Cancel Pending Cached Request

            mCachedOkHttpClient!!.dispatcher().cancelAll()
        }
        mRetrofit = null
        mCachedRetrofit = null
        if (mCache != null) {
            try {
                mCache!!.evictAll()
            } catch (e: IOException) {
                Log.e(TAG, "Error cleaning http cache")
            }
        }
        mCache = null
    }

    fun oauthRetrofitClient(
        url: String): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()

        okHttpBuilder.addInterceptor(createLoggingInterceptor())
            .pingInterval(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)

        val okHttpClient = okHttpBuilder.build()

        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}