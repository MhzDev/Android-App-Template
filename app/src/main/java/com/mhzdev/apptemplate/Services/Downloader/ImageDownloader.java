package com.mhzdev.apptemplate.Services.Downloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mhzdev.apptemplate.Utils.ImageUtils;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ImageDownloader {

    private static final String LOG_TAG = "ImageDownloader";
    private final ImageDownloaderCallback callback;
    private ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private Context context;

    /**
     * Constructor
     */
    public ImageDownloader(Context context, ImageDownloaderCallback callback){
        this.callback = callback;
        this.context = context;
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
    }

    /**
     * Download an immage from url and save internally
     * The file name will be the url escaped
     * @param url
     */
    public void downloadImage(String url) {
        String fileName = "";
        try {
            fileName = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Uri downloadUri = Uri.parse(url);
        Uri destinationUri = Uri.fromFile(new File(context.getFilesDir() + File.separator + fileName));
        Log.d(LOG_TAG, "DestinationUri: " + destinationUri);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        callback.onImageDownloaded();
                        Log.d(LOG_TAG, "Download completed, id: " + id);
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Log.d(LOG_TAG, "Download failed, id: " + id + " - Error: " + errorMessage);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, int progress) {
                    }
                });

        downloadManager.add(downloadRequest);
    }

    /**
     * Get image saved from the url of the image
     * @param context context
     * @param imageUrl url of the image
     * @return the uri of the image saved
     */
    @Nullable
    public static Uri getSavedImageUriFromUrl(Context context, String imageUrl){
        String fileNameEscaped = null;
        try {
            fileNameEscaped = URLEncoder.encode(imageUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(new File(context.getFilesDir() + File.separator + fileNameEscaped));
    }

    /**
     * Get the bitmap of image saved from the url
     * @param context context
     * @param imageUrl url of the image
     * @return the bitmap of the image saved
     */
    @Nullable
    public static Bitmap getSavedImageBitmapScaledFromUrl(Context context, String imageUrl){
        Uri uri = getSavedImageUriFromUrl(context, imageUrl);
        try {
            return ImageUtils.decodeUri(context, uri, 100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ImageDownloaderCallback {
        void onImageDownloaded();
    }
}
