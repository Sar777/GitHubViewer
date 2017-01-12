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

import java.net.HttpURLConnection;

import instinctools.android.R;
import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.utility.Base64Hash;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener, OnHttpClientListener {

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

        Editable username = mEditTextUsername.getText();
        Editable password = mEditTextPassword.getText();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(R.id.activity_auth), R.string.msg_empty_fields, Snackbar.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getString(R.string.msg_auth_title_dialog), getString(R.string.msg_auth_message_dialog), true);

        String userpass = "";

        HttpClientFactory.HttpClient httpClient = HttpClientFactory.create("https://api.github.com/authorizations");
        httpClient.
                setMethod("POST").
                //addHeader("Content-Type", "application/x-www-form-urlencoded").
                addHeader("Authorization", "Basic " + Base64Hash.create(userpass)).
                addHeader("Content-Type", "application/x-www-form-urlencoded").
                setData("{\"scopes\":[\"repo\"],\"note\":\"Demo 2\"}").
                send(this);

//        HttpClientFactory.HttpClient httpClient = HttpClientFactory.create("https://api.github.com/authorizations/76450638");
//        httpClient.
//                setMethod("DELETE").
//                //addHeader("Content-Type", "application/x-www-form-urlencoded").
//                //        addHeader("User-Agent", "Awesome-Octocat-App").
//                addHeader("Authorization", "Basic " + Base64Hash.create(userpass)).
//                //addHeader("Content-Type", "application/x-www-form-urlencoded").
//                setData("{\"scopes\":[\"repo\"],\"note\":\"Demo 2\"}").
//                send(new OnHttpClientListener() {

      /*  HttpClientFactory.HttpClient httpClient = HttpClientFactory.create("https://api.github.com/authorizations/764530638");
        httpClient.
                setMethod("GET").
                //addHeader("Content-Type", "application/x-www-form-urlencoded").
                //        addHeader("User-Agent", "Awesome-Octocat-App").
                        addHeader("Authorization", "Basic " + Base64Hash.create(userpass)).
                //addHeader("Content-Type", "application/x-www-form-urlencoded").
                        setData("{\"scopes\":[\"repo\"],\"note\":\"Demo 2\"}").
                send(this);*/
    }

    @Override
    public void onError(int errCode) {
        mProgressDialog.dismiss();
    }

    @Override
    public void onSuccess(int code, String content) {
        mProgressDialog.dismiss();
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Snackbar.make(findViewById(R.id.activity_auth), R.string.msg_auth_username_or_password_fail, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (code == HttpURLConnection.HTTP_CREATED) {
            setResult(RESULT_OK);
            AuthToken token = JsonTransformer.transform(content, AuthToken.class);
            /// TODO REMOVE ME
            //finish();
        }
    }
}
