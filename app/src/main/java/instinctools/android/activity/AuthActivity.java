package instinctools.android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import instinctools.android.R;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.GithubServices;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initView();
    }

    private void initView() {
        mEditTextUsername = (EditText) findViewById(R.id.edit_text_username);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);

        mButton = (Button) findViewById(R.id.button_auth);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_auth)
            return;

        final Editable username = mEditTextUsername.getText();
        final Editable password = mEditTextPassword.getText();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(R.id.activity_auth), R.string.msg_empty_fields, Snackbar.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getString(R.string.msg_auth_title_dialog), getString(R.string.msg_auth_message_dialog), true);

        GithubServices.authorization(username.toString(), password.toString(), new GithubServiceListener<AuthToken>() {
            @Override
            public void onError(int code) {
                mProgressDialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_auth), R.string.msg_auth_unknown_error, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(AuthToken token) {
                mProgressDialog.dismiss();

                if (token == null) {
                    Snackbar.make(findViewById(R.id.activity_auth), R.string.msg_auth_unknown_error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
