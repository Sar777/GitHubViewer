package instinctools.android.misc;

import android.net.Uri;
import android.os.Parcel;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.style.URLSpan;
import android.view.View;

import instinctools.android.R;

/**
 * Created by orion on 28.12.16.
 */

public class CustomTabsURLSpan extends URLSpan {
    public CustomTabsURLSpan(String url) {
        super(url);
    }

    public CustomTabsURLSpan(Parcel src) {
        super(src);
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(widget.getContext(), R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(widget.getContext(), R.color.colorPrimaryDark));
        CustomTabsIntent tab = intentBuilder.build();
        tab.launchUrl(widget.getContext(), Uri.parse(url));
    }
}