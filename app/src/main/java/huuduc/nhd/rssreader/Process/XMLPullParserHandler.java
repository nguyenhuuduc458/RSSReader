package huuduc.nhd.rssreader.Process;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import huuduc.nhd.rssreader.Entity.FeedEntity;
import huuduc.nhd.rssreader.MainActivity;

public class XMLPullParserHandler {
    private List<FeedEntity> items = new ArrayList<>();
    private boolean isItem;
    private boolean isImage;
    private String description = "";
    private String title = "";
    private String link = "";
    private String text;

    public XMLPullParserHandler() {
    }

    public List<FeedEntity> getItems() {
        return this.items;
    }

    public List<FeedEntity> parseFeed(InputStream inputStream) {
        try {
           XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
           factory.setNamespaceAware(false); // avoid conflict of tagName in xml

           XmlPullParser parser = factory.newPullParser();
           parser.setInput(inputStream,null);

           int eventType = parser.getEventType();
           while(eventType != XmlPullParser.END_DOCUMENT){
               String tagName = parser.getName();
               switch (eventType){
                   case XmlPullParser.START_TAG:{
                        if(tagName.equalsIgnoreCase("item")){
                            isItem  = true;
                        }else if(tagName.equalsIgnoreCase("image"){
                            isImage = true;
                        }
                   }
                   case XmlPullParser.TEXT:{
                       text = parser.getText();
                       break;
                   }
                   case XmlPullParser.END_TAG:{
                       if(tagName.equalsIgnoreCase("title")){
                           title = text;
                       }else if(tagName.equalsIgnoreCase("link")){
                           link = text;
                       }else if(tagName.equalsIgnoreCase("description")){
                           description = text;
                       }else if(tagName.equalsIgnoreCase("item")){
                           isItem = false;
                       }

                       if(!title.equals("") && !link.equals("") && !description.equals("")){
                           if(isItem && !isImage){
                               items.add(new FeedEntity(title,link,description));
                           }else if(!isImage){
                               MainActivity.mFeedDescription = description;
                               MainActivity.mFeedTitle       = title;
                               MainActivity.mFeedLink        = link;
                               Log.i("nguyenhuuduc",MainActivity.mFeedTitle + " " + MainActivity.mFeedDescription + " " + MainActivity.mFeedLink);
                           }
                           isItem  = false;
                           isImage = false;
                           description = "";
                           title       = "";
                           link        = "";
                       }
                       break;
                   }
                   default:{
                       break;
                   }
               }
               eventType = parser.next();
           }
            inputStream.close();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }finally {
            return items;
        }
    }
}
