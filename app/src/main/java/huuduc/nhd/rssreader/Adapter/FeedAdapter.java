package huuduc.nhd.rssreader.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huuduc.nhd.rssreader.Entity.FeedEntity;
import huuduc.nhd.rssreader.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedEntity> items;
    private Context mContext;

    public FeedAdapter(Context context, List<FeedEntity> items){
        this.items    = items;
        this.mContext = context;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{
        private View rssFeedView;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            rssFeedView = itemView;
        }
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item_layout,parent, false);
        FeedViewHolder holder = new FeedViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final FeedEntity item = items.get(position);
        final TextView mTitle, mDescription, mLink;

        // maping component
        mTitle       = holder.rssFeedView.findViewById(R.id.titleText);
        mDescription = holder.rssFeedView.findViewById(R.id.descriptionText);
        mLink        = holder.rssFeedView.findViewById(R.id.linkText);

        // set values for component
        mTitle.setText(item.getTitle());
        mDescription.setText(item.getDescription());
        mLink.setText(item.getLink());

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = mLink.getText().toString();
                Uri uri = Uri.parse(link);
                Intent webView = new Intent(Intent.ACTION_VIEW,uri);
                try{
                    if(webView.resolveActivity(mContext.getPackageManager()) != null){
                        webView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(webView);
                    }
                }catch(Exception e){
                    Log.i("Error messages: ", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
