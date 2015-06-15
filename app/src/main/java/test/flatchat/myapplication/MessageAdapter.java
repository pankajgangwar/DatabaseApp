package test.flatchat.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import database.MyDataBaseContract;

/**
 * Created by iriemo on 20/5/15.
 */
public class MessageAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TEXT = 0;
    private final int VIEW_TYPE_IMAGE = 1;

    public MessageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        Cursor cursor = (Cursor)getItem(position);

        return getItemViewTypeFromCursor(cursor);
    }

    private int getItemViewTypeFromCursor(Cursor cursor){
        String type = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_TYPE));
        if(type.equals("0")){
            return VIEW_TYPE_TEXT;
        }else{
            return VIEW_TYPE_IMAGE;
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        String viewTypeString = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_TYPE));

        int viewType = Integer.parseInt(viewTypeString);

        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_TEXT: {

                layoutId = R.layout.message_text;
                break;
            }
            case VIEW_TYPE_IMAGE: {
                layoutId = R.layout.message_image;
                break;
            }
            default:
                break;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String viewTypeString = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_TYPE));

        int viewType = Integer.parseInt(viewTypeString);

        switch (viewType) {
            case VIEW_TYPE_TEXT: {

                viewHolder.message_text.setText(cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATA)));
                break;
            }
            case VIEW_TYPE_IMAGE: {

                viewHolder.message_Image.setImageUrl(cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATA)), imageLoader);
                break;
            }
            default:
                break;
        }
    }

    public static class ViewHolder {
        public final NetworkImageView message_Image;
        public final TextView message_text;

        public ViewHolder(View view) {
            message_Image = (NetworkImageView) view.findViewById(R.id.message_image);
            message_text = (TextView) view.findViewById(R.id.message_text);
        }
    }
}
