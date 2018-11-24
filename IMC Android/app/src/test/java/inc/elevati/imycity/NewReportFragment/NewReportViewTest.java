package inc.elevati.imycity.NewReportFragment;

import android.graphics.Bitmap;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.new_report_fragment.NewReportFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)

public class NewReportViewTest {

    private NewReportFragment fragment = new NewReportFragment();

    @Before
    public void setup(){
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void ToastTest(){

        String toastTrue = "Failed to connect to server. Please check your internet connection and try again";
        String toastFalse = "Report sent correctly! Update the \"All reports\" section to view it";

        //  Test toast con errore
        fragment.dismissProgressDialog(true);
        assertEquals(ShadowToast.getTextOfLatestToast(), toastTrue);

        //  Test toast senza errore
        fragment.dismissProgressDialog(false);
        assertEquals(ShadowToast.getTextOfLatestToast(), toastFalse);

    }

    @Test
    public void SendReportTest(){

        //  Riferimento al bottone per l'invio
        Button sendBn = fragment.getActivity().findViewById(R.id.bn_new_report_send);

        //  Settiamo come presenter non quello vero ma un mock
        fragment.presenter = mock(MainContracts.NewReportPresenter.class);

        //  Test senza immagine
        String toastText = "Please insert a picture to continue";
        sendBn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), toastText);

        // Test completo
        Bitmap bitmap = mock(Bitmap.class);
        fragment.imageData.image = bitmap;
        fragment.textInputTitle.setText("ProvaTitolo");
        fragment.textInputDesc.setText("ProvaDescr");
        sendBn.performClick();
        verify(fragment.presenter).handleSendReport(any(Bitmap.class), anyString(), anyString());

    }

    @Test
    public void ImageDialogTest(){

        //  Testo se dopo aver cliccato limmagine si apre il chooseImagDialog per la scelta
        fragment.createImageDialog();
        assertTrue(fragment.chooseImagDialog.isShowing());
    }



}
