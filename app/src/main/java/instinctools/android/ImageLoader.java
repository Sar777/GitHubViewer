package instinctools.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by orion on 22.12.16.
 */

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private Context mContext;
    private WeakReference<ImageView> mImageViewReference;
    private int mPlaceholderErrorDrawId;
    private int mPlaceholderLoadingDrawId;

    public ImageLoader(Builder builder) {
        this.mContext = builder.mContext;
        this.mPlaceholderErrorDrawId = builder.mPlaceholderErrorDrawId;
        this.mPlaceholderLoadingDrawId = builder.mPlaceholderLoadingDrawId;
        this.mImageViewReference = builder.mImageViewReference;
    }

    public void load(@NonNull String url) {
        ImageLoadTask task = new ImageLoadTask();
        task.execute(url);
    }

    public static class Builder {
        private Context mContext;

        private int mPlaceholderErrorDrawId;
        private int mPlaceholderLoadingDrawId;

        private WeakReference<ImageView> mImageViewReference;

        public Builder(@NonNull Context context, @NonNull ImageView imageView) {
            this.mContext = context;
            this.mImageViewReference = new WeakReference<>(imageView);
        }

        public Builder error(int errorDrawableId) {
            this.mPlaceholderErrorDrawId = errorDrawableId;
            return this;
        }

        public Builder placeholder(int errorDrawableId) {
            this.mPlaceholderLoadingDrawId = errorDrawableId;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Placeholder set
            if (mPlaceholderLoadingDrawId != 0) {
                ImageView imageView = mImageViewReference.get();
                if (imageView != null)
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, mPlaceholderLoadingDrawId));
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = App.getBitmapCache().getFromCache(params[0]);
            if (bitmap == null) {
                URL url;
                try {
                    url = new URL(params[0]);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Fail parse url for loading image", e);
                    return null;
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e(TAG, "Fail open connection for loading image", e);
                    return null;
                }

                App.getBitmapCache().addToCache(params[0], bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            ImageView imageView = mImageViewReference.get();
            if (imageView == null)
                return;

            if (result == null) {
                if (mPlaceholderErrorDrawId != 0) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, mPlaceholderErrorDrawId));
                    return;
                }
            }

            imageView.setImageBitmap(result);
        }
    }
}
