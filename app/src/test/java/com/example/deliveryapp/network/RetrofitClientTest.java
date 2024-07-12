package com.example.deliveryapp.network;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientTest {

    private static final String BASE_URL = "https://api.example.com/";



    @Before
    public void setUp() {
        // Reset the Retrofit instance before each test
        RetrofitClient.setRetrofit(null);
    }

    @After
    public void tearDown() {
        // Reset the Retrofit instance after each test
        RetrofitClient.setRetrofit(null);
    }

    @Test
    public void testGetClient_NotNull() {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        assertNotNull(retrofit);
    }

    @Test
    public void testGetClient_BaseUrl() {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        assertEquals(BASE_URL, retrofit.baseUrl().toString());
    }

    @Test
    public void testGetClient_SameInstance() {
        Retrofit retrofit1 = RetrofitClient.getClient(BASE_URL);
        Retrofit retrofit2 = RetrofitClient.getClient(BASE_URL);
        assertSame(retrofit1, retrofit2);
    }

    @Test
    public void testGetClient_HttpLoggingInterceptor() {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        OkHttpClient client = (OkHttpClient) retrofit.callFactory();
        boolean hasLoggingInterceptor = false;
        for (int i = 0; i < client.interceptors().size(); i++) {
            if (client.interceptors().get(i) instanceof HttpLoggingInterceptor) {
                hasLoggingInterceptor = true;
                break;
            }
        }
        assertTrue(hasLoggingInterceptor);
    }

    @Test
    public void testGetClient_GsonConverterFactory() {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        boolean hasGsonConverterFactory = false;
        for (int i = 0; i < retrofit.converterFactories().size(); i++) {
            if (retrofit.converterFactories().get(i) instanceof GsonConverterFactory) {
                hasGsonConverterFactory = true;
                break;
            }
        }
        assertTrue(hasGsonConverterFactory);
    }
}