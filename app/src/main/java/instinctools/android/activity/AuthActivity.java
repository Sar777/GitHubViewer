package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import instinctools.android.R;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButtonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initView();
    }

    private void initView() {
        mButtonAuth = (Button) findViewById(R.id.button_auth);
        mButtonAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_auth)
            return;

        mButtonAuth.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.INTENT_AUTH_TYPE, 1);
        startActivity(intent);
    }
}
