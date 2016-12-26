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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import instinctools.android.cache.BitmapCacheMgr;
import instinctools.android.constans.Constants;
import instinctools.android.utility.MD5Hash;

/**
 * Created by orion on 22.12.16.
 */

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static BitmapCacheMgr mBitmapCacheMgr;
    // Current loading images
    private static final Set<String> IMAGE_LOADING_SET = new HashSet<>();
    private static final Executor THREAD_POOL_EXECUTOR = ImageThreadExecutor.create();

    private ImageLoader(Context context) {
        if (mBitmapCacheMgr == null)
            mBitmapCacheMgr = new BitmapCacheMgr.Builder(context.getApplicationContext()).enableSDCardCache(Constants.DISK_MAX_CACHE_SIZE).build();
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    public ImageLoad load(String url) {
        return new ImageLoad(url);
    }

    public class ImageLoad {
        private String mUrl;
        private WeakReference<ImageView> mImageReference;
        private ImagePlaceholder mImagePlaceholder;

        private ImageLoad() {
        }

        public ImageLoad(String url) {
            this.mUrl = url;
        }

        public ImageLoad error(int drawId) {
            if (mImagePlaceholder == null)
                mImagePlaceholder = new ImagePlaceholder();

            mImagePlaceholder.setErrorId(drawId);
            return this;
        }

        public ImageLoad loading(int drawId) {
            if (mImagePlaceholder == null)
                mImagePlaceholder = new ImagePlaceholder();

            mImagePlaceholder.setLoadingId(drawId);
            return this;
        }

        public ImageLoad in(@NonNull ImageView image) {
            this.mImageReference = new WeakReference<>(image);
            return this;
        }

        public void load() {
            if (mUrl == null || mImageReference == null)
                throw new IllegalArgumentException("Null load arguments. Url or ImageView");

            synchronized (IMAGE_LOADING_SET) {
                String urlHash = MD5Hash.create(mUrl);
                if (IMAGE_LOADING_SET.contains(urlHash))
                    return;

                IMAGE_LOADING_SET.add(urlHash);
            }

            ImageLoadTask task = new ImageLoadTask(this);
            task.executeOnExecutor(THREAD_POOL_EXECUTOR);
        }
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final ImageLoad mImageLoad;

        ImageLoadTask(@NonNull ImageLoad imageLoad) {
            this.mImageLoad = imageLoad;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mImageLoad.mImagePlaceholder != null && mImageLoad.mImagePlaceholder.getLoadingId() != 0) {
                ImageView imageView = mImageLoad.mImageReference.get();
                if (imageView != null)
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), mImageLoad.mImagePlaceholder.getLoadingId()));
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = mBitmapCacheMgr.getFromCache(mImageLoad.mUrl);
            if (bitmap == null) {
                URL url;
                try {
                    url = new URL(mImageLoad.mUrl);
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

                mBitmapCacheMgr.addToCache(mImageLoad.mUrl, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            synchronized (IMAGE_LOADING_SET) {
                IMAGE_LOADING_SET.remove(MD5Hash.create(mImageLoad.mUrl));
            }

            ImageView imageView = mImageLoad.mImageReference.get();
            if (imageView == null)
                return;

            if (result == null) {
                if (mImageLoad.mImagePlaceholder != null && mImageLoad.mImagePlaceholder.getErrorId() != 0)
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), mImageLoad.mImagePlaceholder.getErrorId()));

                return;
            }

            imageView.setImageBitmap(result);
        }
    }
}
