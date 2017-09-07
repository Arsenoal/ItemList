package test.com.itemlist.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import test.com.itemlist.AsynchronousGet;
import test.com.itemlist.R;
import test.com.itemlist.util.CircleTransform;
import test.com.itemlist.util.MyPreference;

/**
 * Created by Arsen on 25.07.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private JSONArray content = new JSONArray();
    private Context mContext;

    public RecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView mImageView;
        public TextView tv1;

        public ViewHolder(View v) {
            super(v);

            mImageView = (AppCompatImageView) v.findViewById(R.id.photo);
            tv1 = (TextView) v.findViewById(R.id.t1);
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder,final int position) {
        try {
            System.err.println(content.getJSONObject(position));

            Picasso.with(mContext)
                    .load(MyPreference.imgUrl.replace("[fullPath]", content.getJSONObject(position).getJSONObject("coverImg").getString("fullPath")))
                    .centerCrop()
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.mImageView);

            holder.tv1.setText(content.getJSONObject(position).getJSONObject("movie").getString("name"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return content.length();
    }

    public void add(JSONObject item){
        content.put(item);
        notifyItemInserted(content.length());
    }

    public void remove(int position){
        content.remove(position);
        notifyItemRemoved(position);
    }

    public void empty(){
        try {
            while (content.length() > 0) {
                int position = content.length() - 1;
                remove(position);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
