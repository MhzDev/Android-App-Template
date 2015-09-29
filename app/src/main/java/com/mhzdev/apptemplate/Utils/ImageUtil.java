package com.mhzdev.apptemplate.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.mhzdev.apptemplate.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {
    public static int getDisplayWidth(Context c) {
        Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
    public static int getDisplayHeight(Context c) {
        Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static Bitmap decodeBitmapFromUriScaled(Context c, Uri uri, int reqWidth, int reqHeight, boolean shouldRotate) {
        InputStream inputStream = null;
        try {
            inputStream = c.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(c, R.string.error_loading_image, Toast.LENGTH_LONG).show();
            return null;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        byte[] byteArr = new byte[0];
        byte[] buffer = new byte[1024];
        int len;
        int count = 0;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                if (len != 0) {
                    if (count + len > byteArr.length) {
                        byte[] newbuf = new byte[(count + len) * 2];
                        System.arraycopy(byteArr, 0, newbuf, 0, count);
                        byteArr = newbuf;
                    }

                    System.arraycopy(buffer, 0, byteArr, count, len);
                    count += len;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeByteArray(byteArr, 0, count, options);

            //BitmapFactory.decodeStream(inputStream, null, options);

        } catch (NullPointerException e) {
            Toast.makeText(c, "Retry", Toast.LENGTH_LONG).show();
            return null;
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap midBitmap = BitmapFactory.decodeByteArray(byteArr, 0, count, options);

        //Bitmap midBitmap = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, options);

        String path = getRealPathFromURI(c, uri);

        if(shouldRotate && path != null)
            return rotateBitmap(midBitmap, path);

        return midBitmap;
    }

    private static String getRealPathFromURI(Context content, Uri contentURI) {
        String result;
        Cursor cursor = content.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    private static Bitmap rotateBitmap(Bitmap finalBitmap, String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation;
        try {
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return finalBitmap;
        }

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return finalBitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return finalBitmap;
        }
        try {
            Bitmap bmRotated =
                    Bitmap.createBitmap(finalBitmap, 0, 0, finalBitmap.getWidth(), finalBitmap.getHeight(), matrix, true);
            finalBitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }





    public static Uri getBitmapUri(Context context, Bitmap image) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

    /**
     * SAVE ON DISK (Internal memory public folder)
     */
    public static Uri saveImageOnDisk(Context context, Bitmap image, String title) {
        File pictureFile = getOutputMediaFile(context, title);
        saveImage(pictureFile, image);

        return Uri.fromFile(pictureFile);
    }
    //Private
    private static void saveImage(File file, Bitmap bitmap){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e("UT_LOG", "Error when saving image to cache. ", e);
        }
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(Context context, String title){
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String fileName = "Title-"+ timeStamp +".jpg";
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/UltimTeam");
        dir.mkdirs();

        return new File(dir, fileName);
    }
    /**
    * SAVE ON DISK END
    */





    /**
     * SAVE ON Internal App Data--------------------------------
     */
    public static Uri saveImageOnData(Context context, Bitmap image) {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String fileName = "Title-"+ timeStamp +".jpg";

        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File pictureFile = new File(directory, fileName);

//        File pictureFile = context.getFilesDir();
//        pictureFile.getParentFile().mkdirs();
        saveImage(pictureFile, image);

        return Uri.fromFile(pictureFile);
    }

    /**
     * SAVE ON Internal App CACHE
     */
    public static Uri saveImageOnCache(Context context, Bitmap image) {
        File pictureFile = context.getCacheDir();
        saveImage(pictureFile, image);

        return Uri.fromFile(pictureFile);
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}