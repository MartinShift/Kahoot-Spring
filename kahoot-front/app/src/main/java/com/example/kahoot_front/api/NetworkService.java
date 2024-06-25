package com.example.kahoot_front.api;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Getter
public class NetworkService {
    private static NetworkService networkService;
    private Retrofit retrofitJson;
    private Retrofit retrofitScalar;
    private Retrofit retrofitXml;
    private Api apiJson;
    private Api apiScalar;
    private Api apiXml;

    private NetworkService() {
        retrofitJson = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiJson = retrofitJson.create(Api.class);

        retrofitScalar = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        apiScalar = retrofitScalar.create(Api.class);

        retrofitXml = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8080/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        apiXml = retrofitXml.create(Api.class);
    }

    public static synchronized NetworkService getInstance() {
        if (networkService == null) {
            networkService = new NetworkService();
        }
        return networkService;
    }

    public Api getApiJson() {
        return apiJson;
    }

    public Api getApiScalar() {
        return apiScalar;
    }

    public Api getApiXml() {
        return apiXml;
    }
}