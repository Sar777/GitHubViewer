package instinctools.android.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

import instinctools.android.App;
import instinctools.android.cache.BitmapCacheMgr;
import instinctools.android.constans.Constants;
import instinctools.android.executors.ImageTaskExecutor;
import instinctools.android.imageloader.transformers.ImageTransformer;

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static final BitmapCacheMgr mBitmapCacheMgr;

    // Current loading images
    private static final Executor ImageBitmapExecutor = ImageTaskExecutor.create();

    static {
        mBitmapCacheMgr = new BitmapCacheMgr.Builder().enableSDCardCache(Constants.DISK_MAX_CACHE_SIZE, App.getAppContext()).build();
    }

    private ImageLoader() {
    }

    public static ImageHolder what(@NonNull String url) {
        return new ImageHolder(url);
    }

    public static class ImageHolder {
        private String mUrl;
        private WeakReference<ImageView> mImageViewReference;
        private ImagePlaceholder mImagePlaceholder;
        private ImageTransformer mTransformer;

        ImageHolder(String url) {
            this.mUrl = url;
        }

        public ImageHolder what(String url) {
            this.mUrl = url;
            return this;
        }

        public ImageHolder error(int drawId) {
            if (mImagePlaceholder == null)
                mImagePlaceholder = new ImagePlaceholder();

            mImagePlaceholder.setErrorId(drawId);
            return this;
        }

        public ImageHolder loading(int drawId) {
            if (mImagePlaceholder == null)
                mImagePlaceholder = new ImagePlaceholder();

            mImagePlaceholder.setLoadingId(drawId);
            return this;
        }

        public ImageHolder in(@NonNull ImageView image) {
            this.mImageViewReference = new WeakReference<>(image);
            return this;
        }

        public ImageHolder transformer(ImageTransformer transformer) {
            this.mTransformer = transformer;
            return this;
        }

        public void load(@NonNull ImageLoadingStateListener listener) {
            if (TextUtils.isEmpty(mUrl) || mImageViewReference.get() == null)
                return;

            new ImageBitmapWorker(this, listener).executeOnExecutor(ImageBitmapExecutor);
        }

        public void load() {
            new ImageBitmapWorker(this).executeOnExecutor(ImageBitmapExecutor);
        }
    }

    private static class ImageBitmapWorker extends AsyncTask<Void, Void, Bitmap> {
        private final ImageHolder mImageHolder;
        private final ImageLoadingStateListener mListener;

        ImageBitmapWorker(ImageHolder image) {
            this.mImageHolder = image;
            this.mListener = null;
        }

        ImageBitmapWorker(ImageHolder image, ImageLoadingStateListener listener) {
            this.mImageHolder = image;
            this.mListener = listener;
        }

        private Bitmap loadingFromNetwork() {
            URL url;
            Bitmap bitmap;
            try {
                url = new URL(mImageHolder.mUrl);
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

            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            ImageView imageView = mImageHolder.mImageViewReference.get();
            if (imageView != null) {
                imageView.setTag(mImageHolder.mUrl);

                ImagePlaceholder imagePlaceholder = mImageHolder.mImagePlaceholder;
                if (imagePlaceholder != null && imagePlaceholder.getLoadingId() != 0)
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), imagePlaceholder.getLoadingId()));
            }

            if (mListener != null)
                mListener.onPrepare();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            ImageTransformer transformer = mImageHolder.mTransformer;

            Bitmap bitmap = mBitmapCacheMgr.getFromCache(transformer != null ? mImageHolder.mUrl + transformer.getClass().getName() : mImageHolder.mUrl);
            boolean cache = true;
            if (bitmap == null) {
                cache = false;
                bitmap = loadingFromNetwork();
            }

            if (bitmap != null && !cache) {
                // Apply transformer
                if (transformer != null)
                    bitmap = transformer.transform(bitmap);

                mBitmapCacheMgr.addToCache(mImageHolder.mTransformer != null ? mImageHolder.mUrl + mImageHolder.mTransformer.getClass().getName() : mImageHolder.mUrl, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                ImagePlaceholder imagePlaceholder = mImageHolder.mImagePlaceholder;
                if (imagePlaceholder != null && imagePlaceholder.getErrorId() != 0) {
                    ImageView imageView = mImageHolder.mImageViewReference.get();
                    if (imageView != null)
                        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), imagePlaceholder.getErrorId()));
                }

                if (mListener != null)
                    mListener.onError();

                return;
            }

            ImageView imageView = mImageHolder.mImageViewReference.get();
            if (imageView != null) {
                if (imageView.getTag().equals(mImageHolder.mUrl))
                    imageView.setImageBitmap(bitmap);
            }

            if (mListener != null)
                mListener.onLoaded(bitmap);
        }
    }
}
