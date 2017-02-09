package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import instinctools.android.R;
import instinctools.android.constans.Constants;
import instinctools.android.services.github.GithubService;
import instinctools.android.storages.ApplicationPersistantStorage;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!ApplicationPersistantStorage.getBoolProperty(Constants.PROPERTY_FIRST_RUN)) {
            Intent intent = new Intent(this, IntroductionsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        String accessToken = GithubService.getAccessToken();
        Intent intent;
        if (TextUtils.isEmpty(accessToken))
            intent = new Intent(SplashActivity.this, AuthActivity.class);
        else
            intent = new Intent(SplashActivity.this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
