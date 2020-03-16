package com.moria.finalexamflickerapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickerJsonData extends AsyncTask<String,Void,List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickerJsonData";

    private List<Photo>photoList = null;
    private String baseURL;
    private String language;
    private boolean matchAll;

    private final OnDataAvailable callBack;
    private boolean runningOnSameThread = false;

    interface  OnDataAvailable {
        void onDataAvailable(List<Photo> data,DownloadStatus status);
    }

    public GetFlickerJsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll ) {
        Log.d(TAG, "GetFlickerJsonData: called");
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
        this.callBack = callBack;
    }

    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destUri = createUri(searchCriteria,language,matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");

        if (callBack != null){
            callBack.onDataAvailable(photoList,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destUri = createUri(params[0],language,matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destUri);
        Log.d(TAG, "doInBackground: ends");
        return photoList;

    }

    private String createUri(String searchCriteria, String language, boolean matchAll) {
        Log.d(TAG, "createUri: createUri starts");

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang",language)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts status = " + status);

        if (status == DownloadStatus.OK){
            photoList = new ArrayList<>();

            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.","_b.");

                    Photo photoObject = new Photo(title,author,authorId,link,tags,photoUrl);
                    photoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            }catch (JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error procesing Json data " + jsone.getMessage() );
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (runningOnSameThread == true && callBack != null){
            callBack.onDataAvailable(photoList,status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
