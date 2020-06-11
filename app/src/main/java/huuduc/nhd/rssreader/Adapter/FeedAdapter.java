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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<FeedEntity> items;
    private Context mContext;

    public FeedAdapter(Context context, List<FeedEntity> items){
        this.mContext = context;
        this.items    = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View rssFeedView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rssFeedView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item_layout,parent, false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeedEntity item = items.get(position);
        final TextView mTitle, mDescription, mLink;

        // maping component
        mDescription = holder.rssFeedView.findViewById(R.id.descriptionText);
        mTitle       = holder.rssFeedView.findViewById(R.id.titleText);
        mLink        = holder.rssFeedView.findViewById(R.id.linkText);

        // set values for component
        mDescription.setText(item.getDescription());
        mTitle.setText(item.getTitle());
        mLink.setText(item.getLink());

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webView = new Intent(Intent.ACTION_VIEW,Uri.parse(mLink.getText().toString()));
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
        try{
            return items.size();
        }catch (NullPointerException e){
            return -1;
        }
    }

    public void saveChanges(){ notifyDataSetChanged();}

    public void deleteAll(){
        items.clear();
        saveChanges();
    }

}
