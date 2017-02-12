package instinctools.android.account;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import instinctools.android.activity.AuthActivity;
import instinctools.android.activity.MainActivity;
import instinctools.android.services.github.GithubService;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {
    private static final String TAG = "OnTokenAcquired";

    private Context mContext;

    public OnTokenAcquired(Context context) {
        this.mContext = context;
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        Intent intent;
        try {
            Bundle bundle = result.getResult();
            if (bundle.isEmpty())
                intent = new Intent(mContext, AuthActivity.class);
            else {
                GithubService.setAccessToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));
                intent = new Intent(mContext, MainActivity.class);
            }
        } catch (Exception e) {
            Log.e(TAG, "Fail get token from Account Manager...", e);

            intent = new Intent(mContext, AuthActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }
}