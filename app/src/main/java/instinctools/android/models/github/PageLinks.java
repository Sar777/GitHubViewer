package instinctools.android.models.github;

import android.os.Parcel;
import android.os.Parcelable;

public class PageLinks implements Parcelable {
    private static final String DELIM_LINKS = ","; //$NON-NLS-1$
    private static final String DELIM_LINK_PARAM = ";"; //$NON-NLS-1$
    private static final String META_REL = "rel"; //$NON-NLS-1$
    private static final String META_LAST = "last"; //$NON-NLS-1$
    private static final String META_NEXT = "next"; //$NON-NLS-1$
    private static final String META_FIRST = "first"; //$NON-NLS-1$
    private static final String META_PREV = "prev"; //$NON-NLS-1$

    private String mFirst;
    private String mLast;
    private String mNext;
    private String mPrev;

    public PageLinks(String linkHeader) {
        if (linkHeader == null)
            return;

        String[] links = linkHeader.split(DELIM_LINKS);
        for (String link : links) {
            String[] segments = link.split(DELIM_LINK_PARAM);
            if (segments.length < 2)
                continue;

            String linkPart = segments[0].trim();
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            linkPart = linkPart.substring(1, linkPart.length() - 1);

            for (int i = 1; i < segments.length; i++) {
                String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                if (rel.length < 2 || !META_REL.equals(rel[0]))
                    continue;

                String relValue = rel[1];
                if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                    relValue = relValue.substring(1, relValue.length() - 1);

                if (META_FIRST.equals(relValue))
                    mFirst = linkPart;
                else if (META_LAST.equals(relValue))
                    mLast = linkPart;
                else if (META_NEXT.equals(relValue))
                    mNext = linkPart;
                else if (META_PREV.equals(relValue))
                    mPrev = linkPart;
            }
        }
    }

    private PageLinks(Parcel in) {
        mFirst = in.readString();
        mLast = in.readString();
        mNext = in.readString();
        mPrev = in.readString();
    }

    public String getFirst() {
        return mFirst;
    }

    public String getLast() {
        return mLast;
    }

    public String getNext() {
        return mNext;
    }

    public String getPrev() {
        return mPrev;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirst);
        dest.writeString(mLast);
        dest.writeString(mNext);
        dest.writeString(mPrev);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PageLinks> CREATOR = new Creator<PageLinks>() {
        @Override
        public PageLinks createFromParcel(Parcel in) {
            return new PageLinks(in);
        }

        @Override
        public PageLinks[] newArray(int size) {
            return new PageLinks[size];
        }
    };
}