package instinctools.android.fragments.introductions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import instinctools.android.R;
import instinctools.android.activity.AuthActivity;
import instinctools.android.activity.MainActivity;
import instinctools.android.constans.Constants;
import instinctools.android.storages.PersistantStorage;

public class AboutGithubFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_AUTHORIZATION = 1;

    private static final String ARGUMENT_PAGE_NUMBER = "ARG_PAGE_NUMBER";

    private Button mButtonSignUp;

    public AboutGithubFragment() {
    }

    public static AboutGithubFragment newInstance(int position) {
        AboutGithubFragment fragment = new AboutGithubFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, position);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        int resId;
        switch (getArguments().getInt(ARGUMENT_PAGE_NUMBER)) {
            case 0:
                resId = R.layout.fragment_intro_github_1;
                break;
            case 1:
                resId = R.layout.fragment_intro_github_2;
                break;
            case 2:
                resId = R.layout.fragment_intro_github_3;
                break;
            case 3:
                resId = R.layout.fragment_intro_github_4;
                break;
            default:
                return null;
        }

        View view = inflater.inflate(resId, container, false);

        if (resId == R.layout.fragment_intro_github_4) {
            mButtonSignUp = (Button) view.findViewById(R.id.button_signUp);
            mButtonSignUp.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.button_signUp)
            return;

        PersistantStorage.addProperty(Constants.PROPERTY_FIRST_RUN, true);

        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AUTHORIZATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_AUTHORIZATION)
            return;

        if (resultCode != Activity.RESULT_OK)
            return;

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
