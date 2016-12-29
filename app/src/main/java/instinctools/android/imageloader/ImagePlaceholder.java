package instinctools.android.imageloader;

/**
 * Created by orion on 24.12.16.
 */

public class ImagePlaceholder {

    private int mLoadingId;
    private int mErrorId;

    public int getLoadingId() {
        return mLoadingId;
    }

    public void setLoadingId(int loadingId) {
        this.mLoadingId = loadingId;
    }

    public int getErrorId() {
        return mErrorId;
    }

    public void setErrorId(int errorId) {
        this.mErrorId = errorId;
    }
}
