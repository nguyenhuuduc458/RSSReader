package huuduc.nhd.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import huuduc.nhd.rssreader.Adapter.FeedAdapter;
import huuduc.nhd.rssreader.Entity.FeedEntity;
import huuduc.nhd.rssreader.Process.XMLPullParserHandler;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView       mRecyclerView;
    private TextView           mTitleTextView, mLinkTextView, mDescriptionTextView;
    private Button             mFetchButton;
    private EditText           mLinkInput;

    private List<FeedEntity> mlistItems;
    public static String mFeedDescription ="";
    public static String mFeedTitle ="";
    public static String mFeedLink  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // maping component
        maping();

        //set default value
        setDefaultValue();

        // action
        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchFeedData().execute();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedData().execute();
            }
        });

    }

    protected void maping(){
        mDescriptionTextView    = findViewById(R.id.textViewFeedDescription);
        mSwipeRefreshLayout     = findViewById(R.id.swipeRefreshLayout);
        mTitleTextView          = findViewById(R.id.textViewFeedTitle);
        mLinkTextView           = findViewById(R.id.textViewFeedLink);
        mRecyclerView           = findViewById(R.id.recyclerView);
        mFetchButton            = findViewById(R.id.buttonFetchData);
        mLinkInput              = findViewById(R.id.editTextInputLink);
    }

    protected void setDefaultValue(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLinkInput.setText("https://vnexpress.net/rss/suc-khoe.rss");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAllItem: {
                deleteAll();
                return true;
            }
            case R.id.dumpLog: {
                dumpLog();
                return true;
            }
            case R.id.sortItem:{
                sortItem();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void deleteAll() {

    }

    private void sortItem() {

    }

    private void dumpLog() {
        
    }

    private class FetchFeedData extends AsyncTask<Void, Void, Boolean>{
        private String urlLink;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
            urlLink = mLinkInput.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(TextUtils.isEmpty(urlLink))
                return false;
            try {
                if(!urlLink.startsWith("http") && !urlLink.startsWith("https"))
                    urlLink = "http://" + urlLink;

                InputStream inputStream = new URL(urlLink).openConnection().getInputStream();
                mlistItems              = new XMLPullParserHandler().parseFeed(inputStream);
                Log.i("nguyenhuuduc",String.valueOf(mlistItems.size()));
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
              super.onProgressUpdate(values);
              mRecyclerView.setAdapter(new FeedAdapter(getApplicationContext(),mlistItems));
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(success){
                mDescriptionTextView.setText("Feed Description: " + MainActivity.mFeedDescription);
                mTitleTextView.setText("Feed Title: " + MainActivity.mFeedTitle);
                mLinkTextView.setText("Feed Link: " + MainActivity.mFeedLink);
                mRecyclerView.setAdapter(new FeedAdapter(getApplicationContext(),mlistItems));
                Log.i("nguyenhuuduc", String.valueOf(mlistItems.get(1).toString()));
            }else{
                Toast.makeText(MainActivity.this,"Enter a valid Rss feed url",Toast.LENGTH_LONG).show();
                mLinkInput.requestFocus();
            }
            super.onPostExecute(success);
        }
    }
}