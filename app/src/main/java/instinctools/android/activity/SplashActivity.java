package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import instinctools.android.R;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.services.github.GithubServices;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthToken token = GithubServices.getAuthToken();
                Intent intent;
                if (token == null)
                    intent = new Intent(SplashActivity.this, AuthToken.class);
                else
                    intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        }).start();
    }
}
