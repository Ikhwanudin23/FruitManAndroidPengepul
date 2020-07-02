package com.one.fruitmanpengepul.repositories

import com.one.fruitmanpengepul.models.User
import com.one.fruitmanpengepul.webservices.ApiService
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val api : ApiService){

    fun profile(token : String, result : (User?, Error?) -> Unit){
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error(body.message))
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun login(email: String, password: String, result: (String?, Error?) -> Unit){
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data?.token!!, null)
                    }else{
                        result(null, Error(body.message))
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun register(name : String, email: String, password: String, result: (Boolean, Error?) -> Unit){
        api.register(name, email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                result(false, Error("onFailure : ${t.message}"))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(false, Error(body.message))
                    }
                }else{
                    result(false, Error(response.message()))
                }
            }

        })
    }
}