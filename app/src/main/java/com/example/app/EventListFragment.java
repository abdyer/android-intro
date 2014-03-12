package com.example.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.app.api.Event;
import com.example.app.api.EventsResponse;
import com.example.app.api.GitHubRequest;
import com.example.app.api.GitHubRequestManager;

/**
 * Created by andy on 3/12/14.
 */
public class EventListFragment extends ListFragment implements Response.Listener<EventsResponse>, Response.ErrorListener {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestEvents();
    }

    private void requestEvents() {
        getActivity().setProgressBarIndeterminateVisibility(true);
        GitHubRequest<EventsResponse> request = GitHubRequestManager.getEventRequest(this, this, "chaione");
        request.setTag(this);
        GithubEventsApp.getRequestQueue().add(request);
    }

    @Override
    public void onResponse(EventsResponse response) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        setListShown(true);
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, response);
        getListView().setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), R.string.unable_to_fech_events, Toast.LENGTH_SHORT).show();
    }
}
