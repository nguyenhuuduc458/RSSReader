package huuduc.nhd.rssreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import huuduc.nhd.rssreader.Adapter.FeedAdapter;
import huuduc.nhd.rssreader.Entity.FeedEntity;
import huuduc.nhd.rssreader.Process.XMLPullParserHandler;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FeedAdapter        mAdapter;
    private RecyclerView       mRecyclerView;
    private TextView           mTitleTextView, mLinkTextView, mDescriptionTextView;
    private Button             mFetchButton;
    private EditText           mLinkInput;

    private List<FeedEntity> mlistItems;
    public static String mFeedDescription ="";
    public static String mFeedTitle       ="";
    public static String mFeedLink        = "";

    public static final String KEY_DESCRIPTION_SAVE = "Description";
    public static final String KEY_TITLE_SAVE       = "Title";
    public static final String KEY_LINK_SAVE        = "Link";
    public static final String KEY_LIST_SAVE        = "listItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Maping component
        maping();

        //Set default value for component
        setDefaultValue();

        //Set action event and swipe down
        setActionForButton();

        // Set Animation
        //mRecyclerView.setAnimation(new DefaultItemAnimator());

        // reserve state when rotate
        if(savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
            Log.i("nguyenhuuduc","coqua");
        }
    }

    protected void maping(){
        mDescriptionTextView  = findViewById(R.id.textViewFeedDescription);
        mSwipeRefreshLayout   = findViewById(R.id.swipeRefreshLayout);
        mTitleTextView        = findViewById(R.id.textViewFeedTitle);
        mLinkTextView         = findViewById(R.id.textViewFeedLink);
        mRecyclerView         = findViewById(R.id.recyclerView);
        mFetchButton          = findViewById(R.id.buttonFetchData);
        mLinkInput            = findViewById(R.id.editTextInputLink);
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
        mAdapter.deleteAll();
        mDescriptionTextView.setText("Feed Description: ");
        mTitleTextView.setText("Feed Title: ");
        mLinkTextView.setText("Feed Link: ");
        mLinkInput.setText("");
    }

    private void sortItem() {

    }

    private void dumpLog() {
        
    }

    private void setActionForButton(){
        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchFeedData().execute();
            }
        });

        mLinkInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if(KeyCode == KeyEvent.KEYCODE_ENTER){
                    new FetchFeedData().execute();
                    return true;
                }
                return false;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedData().execute();
            }
        });

    }

    private class FetchFeedData extends AsyncTask<Void, Void, Boolean>{
        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
            urlLink = mLinkInput.getText().toString().trim();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(TextUtils.isEmpty(urlLink))
                return false;
            try {
                if(!urlLink.startsWith("http") && !urlLink.startsWith("https"))
                    urlLink = "https://" + urlLink;

                InputStream inputStream = new URL(urlLink).openConnection().getInputStream();
                mlistItems              = new XMLPullParserHandler().parseFeed(inputStream);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(success){
                mDescriptionTextView.setText("Feed Description: " + MainActivity.mFeedDescription);
                mTitleTextView.setText("Feed Title: " + MainActivity.mFeedTitle);
                mLinkTextView.setText("Feed Link: " + MainActivity.mFeedLink);
                mAdapter = new FeedAdapter(getApplicationContext(),mlistItems);
                mRecyclerView.setAdapter(mAdapter);
                Log.i("nguyenhuuduc", String.valueOf(mAdapter.getItemCount()));
            }else{
                Toast.makeText(MainActivity.this,"Enter a valid Rss feed url",Toast.LENGTH_LONG).show();
                mLinkInput.setText("");
                mLinkInput.requestFocus();
            }
            super.onPostExecute(success);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MainActivity.KEY_LIST_SAVE,(Serializable) mlistItems);
        outState.putString(MainActivity.KEY_DESCRIPTION_SAVE,MainActivity.mFeedDescription);
        outState.putString(MainActivity.KEY_TITLE_SAVE,MainActivity.mFeedTitle);
        outState.putString(MainActivity.KEY_LINK_SAVE,MainActivity.mFeedLink);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mFeedDescription = savedInstanceState.getString(MainActivity.KEY_DESCRIPTION_SAVE);
        mFeedTitle       = savedInstanceState.getString(MainActivity.KEY_TITLE_SAVE);
        mFeedLink        = savedInstanceState.getString(MainActivity.KEY_LINK_SAVE);
        mlistItems       = (List<FeedEntity>) savedInstanceState.getSerializable(MainActivity.KEY_LIST_SAVE);
        mAdapter         = new FeedAdapter(getApplicationContext(),mlistItems);

        mRecyclerView.setAdapter(mAdapter);
        mDescriptionTextView.setText("Feed Description: " + MainActivity.mFeedDescription);
        mTitleTextView.setText("Feed Title: " + MainActivity.mFeedTitle);
        mLinkTextView.setText("Feed Link: " + MainActivity.mFeedLink);
    }
}