package instinctools.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by orion on 22.12.16.
 */

public class ImageLoader {
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

        public Builder setErrorDrawableId(int errorDrawableId) {
            this.mPlaceholderErrorDrawId = errorDrawableId;
            return this;
        }

        public Builder setLoadingDrawableId(int errorDrawableId) {
            this.mPlaceholderLoadingDrawId = errorDrawableId;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Void> {
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
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
