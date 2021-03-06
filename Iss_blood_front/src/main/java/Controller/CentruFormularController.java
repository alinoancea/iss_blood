package Controller;

import Model.FormularDonare;
import Model.StaffInfo;
import Utils.CustomMessageBox;
import Validators.ValidationException;
import javafx.util.Pair;

public class CentruFormularController extends AbstractFormularDonareController {
    @Override
    public void trimiteFormular() {
        FormularDonare formularDonare = null;
        try {
            formularDonare = GetInfoFormular();
        } catch (ValidationException e) {
            CustomMessageBox msg = new CustomMessageBox("Eroare", "Formularul nu a fost completat corect. " +
                    "\n" + e.getMessage(), 1);
            msg.show();
            return;
        }

        Pair<Boolean, String> rez = getService().staffTrimiteFormularDonare(formularDonare);

        if(rez.getKey())
        {
            CustomMessageBox msg = new CustomMessageBox("Info", "Formularul a fost trimis cu succes", 0);
            msg.show();

        }
        else
        {
            CustomMessageBox msg = new CustomMessageBox("Eroare", rez.getValue(), 1);
            msg.show();
        }

    }


    @Override
    void updateThis() {

    }

    @Override
    public void setScreenController(ScreenController ctrl)
    {
        super.setScreenController(ctrl);

        autofill();
    }

    private void autofill()
    {
        StaffInfo info = (StaffInfo)getScreenController().userInfo;

        DomiciliuJudetTextField.setText(info.getNumeJudet());
        ResedintaJudetTextField.setText(info.getNumeJudet());
    }

}
