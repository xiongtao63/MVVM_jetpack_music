package com.xiongtao.puremusic.bridge.data.repository.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-20 11:30
 */
class APIClient {

    private object Holder {
        val INSTANCE = APIClient()
    }

    companion object{
        val instance = Holder.INSTANCE
    }

    fun <T> instanceRetrofit(apiInterface: Class<T>):T {
        // 之前的代码
        /*val mOkHttpClient: OkHttpClient = OkHttpClient().newBuilder()

            // 添加读取超时时间
            .readTimeout(10000, TimeUnit.SECONDS)

            // 添加连接超时时间
            .connectTimeout(10000, TimeUnit.SECONDS)

            // 添加写出超时时间
            .writeTimeout(10000, TimeUnit.SECONDS)

            .build()*/

        val mOkHttpClient:OkHttpClient = OkHttpClient().newBuilder().myApply {
            // 添加读取超时时间
            readTimeout(10000, TimeUnit.SECONDS)

            // 添加连接超时时间
            connectTimeout(10000, TimeUnit.SECONDS)

            // 添加写出超时时间
            writeTimeout(10000, TimeUnit.SECONDS)
        }.build()

       val retrofit:Retrofit = Retrofit.Builder()
           .baseUrl("https://www.wanandroid.com")
           .client(mOkHttpClient)
           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
           .addConverterFactory(GsonConverterFactory.create())
           .build()

        return retrofit.create(apiInterface)
    }

    fun <T> T.myApply(mm:T.() -> Unit):T{
        mm()
        return this
    }
}