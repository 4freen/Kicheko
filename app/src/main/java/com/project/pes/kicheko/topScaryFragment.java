package com.project.pes.kicheko;

/**
 * Created by Afreen on 25/10/14.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class topScaryFragment extends Fragment {

    private ArrayAdapter<String> ArticleAdapter;

    public topScaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scary_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchDataTask dataTask = new FetchDataTask();
            dataTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] flist = {"Article_name", "Article_name", "Article_name", "Article_name", "Article_name", "Article_name", "Article_name"};

        List<String> listArticles = new ArrayList<String>(Arrays.asList(flist));
        View rootView = inflater.inflate(R.layout.fragment_scary, container, false);
        ArticleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_article, R.id.list_item_article_tv, listArticles);

        ListView articlelist = (ListView) rootView.findViewById(R.id.listView_article);
        articlelist.setAdapter(ArticleAdapter);
        return rootView;
    }


    public class FetchDataTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchDataTask.class.getSimpleName();


        private String[] getDataFromJson(String articleJsonStr, int numArticles)
                throws JSONException {

// These are the names of the JSON objects that need to be extracted.
            final String NYT_RESP = "response";
            final String NYT_DOCS = "docs";
            final String NYT_HEAD = "headline";
            final String NYT_MAINHEAD = "main";
            final String NYT_URL = "web_url";

            JSONObject articlesJson = new JSONObject(articleJsonStr);
            JSONObject respJson = articlesJson.getJSONObject(NYT_RESP); //get response object
            JSONArray docsArray = respJson.getJSONArray(NYT_DOCS);   //get docs object inside response obj

            String[] resultStrs = new String[numArticles];
            for (int i = 0; i < docsArray.length(); i++) {
                //format is name : url
                String art_url;
                String art_headline;

                //get article headline and url
                JSONObject articleItem = docsArray.getJSONObject(i);
                art_url = articleItem.getString(NYT_URL);
                JSONObject articleHeadline = articleItem.getJSONObject(NYT_HEAD);
                art_headline = articleHeadline.getString(NYT_MAINHEAD);

                //print article headline and url
                resultStrs[i] = art_headline + " : " + art_url;
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            int numArticles = 7;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String articleJsonStr = null;
            try {
                // NYTimes key : c550556ff0968ef0ccab7f3dee8472e1:12:70068306
                URL url = new URL("http://api.nytimes.com/svc/search/v2/articlesearch.json?q=scary&begin_date=20141001&end_date=20141003&sort=oldest&facet_filter=true&hl=true&page=1&api-key=c550556ff0968ef0ccab7f3dee8472e1%3A12%3A70068306");
                // Create the request to NYT, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                articleJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("topScaryFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("topScaryFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(articleJsonStr, numArticles);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                ArticleAdapter.clear();
                for (String dayForecastStr : result) {
                    ArticleAdapter.add(dayForecastStr);
                }

            }
        }
    }
}
