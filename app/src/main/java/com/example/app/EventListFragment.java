package com.example.app;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.example.app.api.Event;
import com.example.app.api.EventsResponse;
import com.example.app.api.GitHubRequest;
import com.example.app.api.GitHubRequestManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
        EventsAdapter adapter = new EventsAdapter(getActivity(), response);
        getListView().setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), R.string.unable_to_fech_events, Toast.LENGTH_SHORT).show();
    }

    private class EventsAdapter extends ArrayAdapter<Event> {

        public EventsAdapter(Context context, List<Event> objects) {
            super(context, -1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.event_list_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = ViewHolder.class.cast(convertView.getTag());
            }

            Event event = getItem(position);
            String line1 = String.format("%s by %s", event.getType(), event.getActor().getLogin());
            holder.textLine1.setText(line1);
            holder.textLine2.setText(getRelativeTime(event.getCreatedAt()));
            holder.avatar.setImageUrl(event.getActor().getAvatarUrl(), GithubEventsApp.getImageLoader());

            return convertView;
        }

        private String getRelativeTime(String isoDateTime) {
            try {
                TimeZone utc = TimeZone.getTimeZone("UTC");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                format.setTimeZone(utc);
                GregorianCalendar calendar = new GregorianCalendar(utc);
                calendar.setTime(format.parse(isoDateTime));
                return DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                return isoDateTime;
            }
        }
    }

    class ViewHolder {

        @InjectView(R.id.event_list_item_avatar) NetworkImageView avatar;
        @InjectView(R.id.event_list_item_text_line1) TextView textLine1;
        @InjectView(R.id.event_list_item_text_line2) TextView textLine2;


        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
