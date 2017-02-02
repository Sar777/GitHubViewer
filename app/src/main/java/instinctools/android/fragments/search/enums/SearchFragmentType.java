package instinctools.android.fragments.search.enums;

import java.util.HashMap;
import java.util.Map;

public enum SearchFragmentType {
    REPOSITORIES(0),
    COMMITS(1),
    ISSUES(2),
    USERS(3);

    private int mValue;

    private static Map<Integer, SearchFragmentType> mMap = new HashMap<>();

    static {
        for (SearchFragmentType value : SearchFragmentType.values()) {
            mMap.put(value.mValue, value);
        }
    }

    SearchFragmentType(final int value) { this.mValue = value; }

    public static SearchFragmentType get(int value) {
        return mMap.get(value);
    }
}
