package com.cca.kafka.common.payload;

import java.io.IOException;
import java.util.Map;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class RestHttpClient {
    final OkHttpClient client;
    public static final MediaType JSON
        = MediaType.parse("application/json; charset=utf-8");
    public RestHttpClient() {
        client = new OkHttpClient();
    }
    public String doGetRequest(String url, Map<String, String> queryParams) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+url).newBuilder();
        queryParams.keySet().forEach(x->{
            urlBuilder.addQueryParameter(x, queryParams.get(x));
        });
        String url1 = urlBuilder.build().toString();
        Request request = new Request.Builder()
            .url(url1)
            .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url("http://"+url)
            .post(body)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
