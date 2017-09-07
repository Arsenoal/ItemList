package test.com.itemlist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import test.com.itemlist.adapter.RecyclerAdapter;
import test.com.itemlist.util.MyPreference;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManagerWithSmoothScroller mLayoutManager;
    private AsynchronousGet httpGet;

    public int pageNumber = 1;
    public int visibleItemCount, totalItemCount, pastVisibleItems;
    private boolean loading = true;

    Realm realm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(this);

        httpGet = new AsynchronousGet();

        mRecyclerView.setAdapter(mAdapter);

        String url = "http://13.81.67.155:90/api/v1/scene/Nearest/" + pageNumber + "/8/40.188505/44.517354999999995/1";

        try {
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //realm.insertOrUpdate(dog);
                        RealmResults<Item> result = realm.where(Item.class).findAll();
                    }
                });
            } finally {
                if(realm != null) {
                    realm.close();
                }
            }

            httpGet.run(url , new Callback() {
                @Override
                public void onFailure(@NonNull Call call,@NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        String responseString = responseBody.string();

                        JSONArray responseArray = new JSONArray(responseString);

                        for(int i = 0; i < responseArray.length(); i++){
                            AsynchronousGet httpGet = new AsynchronousGet();

                            String sceneUrl = MyPreference.sceneIdUrl.replace("[id]", responseArray.getJSONObject(i).getString("id"));

                            httpGet.run(sceneUrl, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try (ResponseBody responseBody = response.body()) {
                                        if (!response.isSuccessful())
                                            throw new IOException("Unexpected code " + response);
                                        String responseString = responseBody.string();

                                        final JSONObject responseJson = new JSONObject(responseString);

                                        mAdapter.add(responseJson);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(dy > 0){
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        if(loading){
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                pageNumber++;
                                loading = false;

                                String url = "http://13.81.67.155:90/api/v1/scene/Nearest/" + pageNumber + "/8/40.188505/44.517354999999995/1";

                                try {
                                    httpGet.run(url , new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call,@NonNull IOException e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException {
                                            try (ResponseBody responseBody = response.body()) {
                                                if (!response.isSuccessful())
                                                    throw new IOException("Unexpected code " + response);
                                                String responseString = responseBody.string();

                                                JSONArray responseArray = new JSONArray(responseString);

                                                for(int i = 0; i < responseArray.length(); i++){
                                                    AsynchronousGet httpGet = new AsynchronousGet();

                                                    String sceneUrl = MyPreference.sceneIdUrl.replace("[id]", responseArray.getJSONObject(i).getString("id"));

                                                    httpGet.run(sceneUrl, new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            try (ResponseBody responseBody = response.body()) {
                                                                if (!response.isSuccessful())
                                                                    throw new IOException("Unexpected code " + response);
                                                                String responseString = responseBody.string();

                                                                final JSONObject responseJson = new JSONObject(responseString);

                                                                mAdapter.add(responseJson);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //network
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
