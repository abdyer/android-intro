package com.example.app;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.app.api.Event;
import com.example.app.api.EventsResponse;
import com.example.app.api.GitHubRequest;
import com.example.app.api.GitHubRequestManager;

import java.util.List;

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
        EventsAdapter adapter = new EventsAdapter(getActivity(),
                android.R.layout.simple_list_item_2, response);
        getListView().setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), R.string.unable_to_fech_events, Toast.LENGTH_SHORT).show();
    }

    private class EventsAdapter extends ArrayAdapter<Event> {

        public EventsAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            Event event = getItem(position);
            String line1 = String.format("%s by %s", event.getType(), event.getActor().getLogin());
            TextView.class.cast(convertView.findViewById(android.R.id.text1)).setText(line1);
            TextView.class.cast(convertView.findViewById(android.R.id.text2)).setText(event.getCreatedAt());

            return convertView;
        }
    }
}
