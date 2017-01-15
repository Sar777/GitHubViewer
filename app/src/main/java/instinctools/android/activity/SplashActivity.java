package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import instinctools.android.R;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.services.github.GithubServices;
import instinctools.android.storages.PersistantStorage;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_AUTHORIZATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!PersistantStorage.getBoolProperty(Constants.PROPERTY_FIRST_RUN)) {
            startActivity(new Intent(this, IntroductionsActivity.class));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthToken token = GithubServices.getAuthToken();
                Intent intent;
                if (token == null)
                    intent = new Intent(SplashActivity.this, AuthActivity.class);
                else
                    intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivityForResult(intent, REQUEST_CODE_AUTHORIZATION);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_AUTHORIZATION)
            return;

        if(resultCode != RESULT_OK)
            return;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
