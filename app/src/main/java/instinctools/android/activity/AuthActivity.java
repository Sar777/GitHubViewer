package instinctools.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.services.HttpRunAllService;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog mProgressDialog;
    private Button mButtonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initView();

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getDataString()))
            return;

        if (!intent.getDataString().contains(Constants.AUTH_CALLBACK_INITIAL))
            return;

        mButtonAuth.setVisibility(View.INVISIBLE);

        mProgressDialog = ProgressDialog.show(this, getString(R.string.msg_auth_title_dialog), getString(R.string.msg_auth_message_dialog), true);

        String code = intent.getDataString().substring(intent.getDataString().indexOf("code=") + 5);
        GithubServiceAuthorization.continueAuthorization(code, new GithubServiceListener<AccessToken>() {
            @Override
            public void onError(int code) {
                mButtonAuth.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(AccessToken token) {
                startService(new Intent(AuthActivity.this, HttpRunAllService.class));
                Intent intentActivity = new Intent(AuthActivity.this, MainActivity.class);
                intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentActivity);
                mProgressDialog.dismiss();
                finish();
            }
        });
        super.onNewIntent(intent);
    }

    private void initView() {
        mButtonAuth = (Button) findViewById(R.id.button_auth);
        mButtonAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_auth)
            return;

        App.launchUrl(this, GithubServiceAuthorization.getAuthUrl(Constants.AUTH_CALLBACK_INITIAL));
    }
}
