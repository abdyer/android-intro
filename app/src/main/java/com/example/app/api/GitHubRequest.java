package com.example.app.api;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by andy on 3/12/14.
 */
public class GitHubRequest<T> extends Request<T> {

    private static final String API_URL = "https://api.github.com/";

    private Class<T> clazz;
    private final Response.Listener<T> listener;

    public GitHubRequest(int method, String path, Response.ErrorListener errorListener, Response.Listener<T> listener,
                                 Class<T> clazz) {
        super(method, API_URL + path, errorListener);

        this.listener = listener;
        this.clazz = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Gson gson = new Gson();
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);

            //If requested type is Void, requesting class is not expecting a result. Handle separately
            if(clazz == Void.class)
                return Response.success(null, entry);

            return Response.success(gson.fromJson(jsonString, clazz), entry);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if(listener != null) {
            listener.onResponse(response);
        }
    }
}