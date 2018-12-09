package inc.elevati.imycity.login.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.login.register.RegisterFragment;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.utils.ProgressDialog;

public class SignInFragment extends Fragment implements LoginContracts.SignInView {

    private LoginContracts.SignInPresenter presenter;

    private TextInputEditText textInputEmail, textInputPassword;
    private TextInputLayout textLayoutEmail, textLayoutPassword;

    /** Dialog displayed during firebase communications */
    private ProgressDialog progressDialog;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignInPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        textInputEmail = v.findViewById(R.id.text_input_edit_email);
        textInputPassword = v.findViewById(R.id.text_input_edit_password);
        textLayoutEmail = v.findViewById(R.id.text_input_layout_email);
        textLayoutPassword = v.findViewById(R.id.text_input_layout_password);

        resetErrorOnTextInput();

        v.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presenter.registerButtonClicked();
            }
        });

        v.findViewById(R.id.bn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputEmail.clearFocus();
                textInputPassword.clearFocus();
                textLayoutEmail.setError(null);
                textLayoutPassword.setError(null);
                String email = textInputEmail.getText().toString();
                String password = textInputPassword.getText().toString();
                presenter.signInButtonClicked(email, password);
            }
        });
        return v;
    }

    @Override
    public void showProgressDialog() {
        progressDialog = ProgressDialog.newInstance(R.string.login_loading);
        progressDialog.show(getFragmentManager(), null);
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void switchToRegisterView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_login, RegisterFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void notifyInvalidEmail() {
        textLayoutEmail.setError(getString(R.string.login_no_email));
    }

    @Override
    public void notifyInvalidPassword() {
        textLayoutPassword.setError(getString(R.string.login_no_password));
    }

    @Override
    public void startMainActivity() {
        Activity activity = getActivity();
        if (activity == null) return;
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void notifyAccountNotExists() {
        if (isAdded()) textLayoutEmail.setError(getString(R.string.login_account_not_exists));
    }

    @Override
    public void notifyWrongPassword() {
        if (isAdded()) textLayoutPassword.setError(getString(R.string.login_wrong_password));
    }

    @Override
    public void notifyUnknownError() {
        if (isAdded())
            Toast.makeText(getContext(), R.string.login_unknown_error, Toast.LENGTH_SHORT).show();
    }

    private void resetErrorOnTextInput() {
        textInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutEmail.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutPassword.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
}