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
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.GithubServices;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog mProgressDialog;

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

        mProgressDialog = ProgressDialog.show(this, getString(R.string.msg_auth_title_dialog), getString(R.string.msg_auth_message_dialog), true);

        String code = intent.getDataString().substring(intent.getDataString().indexOf("code=") + 5);
        GithubServices.continueAuthorization(code, new GithubServiceListener<AccessToken>() {
            @Override
            public void onError(int code) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(AccessToken token) {
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                mProgressDialog.dismiss();
                finish();
            }
        });
        super.onNewIntent(intent);
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.button_auth);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_auth)
            return;

        App.launchUrl(this, GithubServices.getAuthUrl(Constants.AUTH_CALLBACK_INITIAL));
    }
}
