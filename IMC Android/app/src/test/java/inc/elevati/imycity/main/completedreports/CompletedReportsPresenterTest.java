package inc.elevati.imycity.main.completedreports;

import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Report;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CompletedReportsPresenter.class, FirebaseAuthHelper.class})
public class CompletedReportsPresenterTest {

    @Mock
    private MainContracts.ReportsView view;

    @Mock
    private FirestoreHelper firestoreHelper;

    private CompletedReportsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        view = mock(CompletedReportsFragment.class);
        presenter = new CompletedReportsPresenter();
        presenter.attachView(view);
        firestoreHelper = mock(FirestoreHelper.class);
        whenNew(FirestoreHelper.class).withArguments(presenter).thenReturn(firestoreHelper);
    }

    @Test
    public void loadReportsTest() {
        presenter.loadReports();
        verify(firestoreHelper).readCompletedReports();
    }

    @Test
    public void onLoadReportsTaskCompleteTest() {
        presenter.onLoadReportsTaskComplete(any(QuerySnapshot.class));
        verify(view).updateReports(anyListOf(Report.class));
    }

    @Test
    public void onUpdateTaskCompleteTest(){
        presenter.onUpdateTaskComplete();
        verify(view).resetRefreshing();
    }

    @Test
    public void showReportTest(){
        Report report = mock(Report.class);
        presenter.showReport(report);
        verify(view).showReportDialog(report);
    }

    @Test
    public void starsButtonClickedTest() {

        // Mock this static method used inside the tested code
        String uid = "dummy";
        mockStatic(FirebaseAuthHelper.class);
        when(FirebaseAuthHelper.getUserId()).thenReturn(uid);

        // Not mocked because we want to test the true and false cases of "starred" field
        Report report = new Report(null, null, null, 0, null, null, null);
        report.setStarred(true);
        presenter.starsButtonClicked(report);

        // Check if unstarReport method is called, as report was starred
        verify(firestoreHelper).unstarReport(report, uid);
        report.setStarred(false);
        presenter.starsButtonClicked(report);

        // Check if starReport method is called, as report was not starred
        verify(firestoreHelper).starReport(report, uid);
    }

    @Test
    public void onStarOperationCompleteTest() {
        presenter.onStarOperationComplete();
        verify(firestoreHelper).readCompletedReports();
    }
}