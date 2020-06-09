package huuduc.nhd.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import huuduc.nhd.rssreader.Entity.FeedEntity;

public class MainActivity extends Activity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView       mRecyclerView;
    private TextView           mTitleText, mLinkText, mDescriptionText;
    private Button             mFetchButton;
    private EditText           mLinkInput;

    private List<FeedEntity> mlistItems;
    private String           mFeedTitle;
    private String           mFeedLink;
    private String           mFeedDescription;

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

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    protected void maping(){
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mDescriptionText    = findViewById(R.id.textViewFeedDescription);
        mRecyclerView       = findViewById(R.id.recyclerView);
        mFetchButton        = findViewById(R.id.buttonFetchData);
        mLinkInput          = findViewById(R.id.editTextInputLink);
        mTitleText          = findViewById(R.id.textViewFeedTitle);
        mLinkText           = findViewById(R.id.textViewFeedLink);
    }

    protected void setDefaultValue(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLinkInput.setText("https://vnexpress.net/rss/kinh-doanh.rss");
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
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}