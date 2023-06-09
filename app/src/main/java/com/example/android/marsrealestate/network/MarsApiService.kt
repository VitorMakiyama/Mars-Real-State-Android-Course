/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://mars.udacity.com/"

// Enum that gives us the possible parameters to filter the results from the web service
enum class MarsApiFilter(val value: String) {SHOW_ALL("all"), SHOW_BUY("buy"), SHOW_RENT("rent")}

// Creates a Moshi object with the MoshiBuilder giving it an adapter
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// Creates a retrofit object, associating it to a converter (our moshi object, created by a MoshiConvertFactory)
// and the base URL
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    // Using suspend makes this a Coroutine function, automatically integrated in Retrofit and Moshi
    @GET("realestate")
    suspend fun getProperties(@Query("filter") type: String): List<MarsProperty>
}

/** Since the retrofit.create() is an expensive call, we expose our API to the rest of the app using
 * a singleton 'object'
 */
object MarsApi {
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}