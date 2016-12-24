package instinctools.android.imageloader;

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

import instinctools.android.cache.BitmapCacheMgr;
import instinctools.android.constans.Constants;

/**
 * Created by orion on 22.12.16.
 */

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static BitmapCacheMgr mBitmapCacheMgr;

    private Context mContext;
    private ImagePlaceholder mImagePlaceholder;

    private ImageLoader() {
    }

    public ImageLoader(Context context) {
        if (mBitmapCacheMgr == null)
            mBitmapCacheMgr = new BitmapCacheMgr.Builder(context).enableSDCardCache(Constants.DISK_MAX_CACHE_SIZE).build();

        mImagePlaceholder = new ImagePlaceholder();
        mContext = context;
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    public ImageLoader error(int drawId) {
        mImagePlaceholder.setErrorId(drawId);
        return this;
    }

    public ImageLoader loading(int drawId) {
        mImagePlaceholder.setLoadingId(drawId);
        return this;
    }

    public ImageLoad load(String url) {
        return new ImageLoad(url);
    }

    public class ImageLoad {
        private String mUrl;
        private WeakReference<ImageView> mImageReference;

        private ImageLoad() {
        }

        public ImageLoad(String url) {
            this.mUrl = url;
        }

        public ImageLoad in(@NonNull ImageView image) {
            this.mImageReference = new WeakReference<>(image);
            return this;
        }

        public void load() {
            if (mUrl == null || mImageReference == null)
                throw new IllegalArgumentException("Null load arguments. Url or ImageView");

            new ImageLoadTask(mUrl, mImageReference).execute();
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Bitmap, Bitmap> {
        private String mUrl;
        private WeakReference<ImageView> mImageReference;

        public ImageLoadTask(@NonNull String url, WeakReference<ImageView> imageLoadWeakReference) {
            this.mUrl = url;
            this.mImageReference = imageLoadWeakReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mImagePlaceholder.getLoadingId() != 0) {
                ImageView imageView = mImageReference.get();
                if (imageView != null)
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, mImagePlaceholder.getLoadingId()));
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = mBitmapCacheMgr.getFromCache(mUrl);
            if (bitmap == null) {
                URL url;
                try {
                    url = new URL(mUrl);
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

                mBitmapCacheMgr.addToCache(mUrl, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            ImageView imageView = mImageReference.get();
            if (imageView == null)
                return;

            if (result == null) {
                if (mImagePlaceholder.getErrorId() != 0) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, mImagePlaceholder.getErrorId()));
                }

                return;
            }

            imageView.setImageBitmap(result);
        }
    }
}
