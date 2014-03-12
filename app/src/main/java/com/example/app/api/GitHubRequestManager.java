package com.example.app.api;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by andy on 3/12/14.
 */
public class GitHubRequestManager {

    public static GitHubRequest<EventsResponse> getEventRequest(Response.ErrorListener errorListener,
                                                               Response.Listener<EventsResponse> listener,
                                                               String organization) {
        String path = String.format("orgs/%s/events", organization);
        GitHubRequest<EventsResponse> request = new GitHubRequest<EventsResponse>(Request.Method.GET,
                path, errorListener, listener, EventsResponse.class);
        return request;
    }
}
