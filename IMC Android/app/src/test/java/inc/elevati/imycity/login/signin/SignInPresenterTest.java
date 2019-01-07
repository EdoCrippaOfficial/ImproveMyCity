package inc.elevati.imycity.login.signin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.login.LoginContracts;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SignInPresenter.class, FirebaseAuthHelper.class})
public class SignInPresenterTest {

    @Mock
    private LoginContracts.SignInView view;

    private SignInPresenter presenter;

    @Before
    public void setUp() {
        view = mock(SignInFragment.class);
        presenter = new SignInPresenter();
        presenter.attachView(view);
    }

    @Test
    public void registerButtonClickedTest() {
        presenter.registerButtonClicked();
        verify(view).switchToRegisterView();
    }

    @Test
    public void signInButtonClicked() throws Exception {
        FirebaseAuthHelper helper = mock(FirebaseAuthHelper.class);
        whenNew(FirebaseAuthHelper.class).withArguments(presenter).thenReturn(helper);

        String email = "email@provider.domain";
        String password = "passw0rd";

        // Call the function with empty email
        presenter.signInButtonClicked("", null);
        verify(view).notifyInvalidEmail();

        // Call the function with empty password
        presenter.signInButtonClicked(email, "");
        verify(view).notifyInvalidPassword();

        // Call the function correctly
        presenter.signInButtonClicked(email, password);
        verify(view).showProgressDialog();
        verify(helper).signIn(email, password);
    }

    @Test
    public void onLoginTaskComplete() {

        // Call the function with LOGIN_OK as parameter
        presenter.onLoginTaskComplete(LoginContracts.LOGIN_OK);
        verify(view).startMainActivity();

        // Call the function with LOGIN_FAILED_NO_ACCOUNT as parameter
        presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_NO_ACCOUNT);
        verify(view).notifyAccountNotExists();

        // Call the function with LOGIN_FAILED_WRONG_PASSWORD as parameter
        presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_WRONG_PASSWORD);
        verify(view).notifyWrongPassword();

        // Call the function with LOGIN_FAILED_UNKNOWN as parameter
        presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_UNKNOWN);
        verify(view).notifyUnknownError();

        verify(view, times(4)).dismissProgressDialog();
    }
}
