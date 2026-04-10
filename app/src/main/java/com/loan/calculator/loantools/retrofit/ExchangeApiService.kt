package com.loan.calculator.loantools.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeApiService {
    @GET("v1/currencies/{coin}.json")
    suspend fun getExchangeRates(
        @Path("coin") coin: String
    ): Map<String, Any>
    @GET("v1/currencies.json")
    suspend fun getAllCurrencies(): Map<String, String>
}