package Controller;

import Model.CerereSange;
import Model.GrupaSange;
import Model.Importanta;
import Model.RH;
import Service.MainService;
import Utils.CustomMessageBox;
import Utils.FunctiiUtile;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CerereSangeController implements  ControlledScreensInterface{
    private MainService mainService;
    private ControllerScreens controller;

    @FXML
    private JFXTextField trombocitetTextField;
    @FXML
    private JFXTextField globuleRosiTextField;
    @FXML
    private JFXTextField plasmaTextField;


    @FXML
    private JFXTextField numePacientTextField;

    @FXML
    private JFXTextField cnpPacientTextField;

    @FXML
    private JFXComboBox<GrupaSange> grupaSangeComboBox;

    @FXML
    private JFXComboBox<RH> rhComboBox;

    @FXML
    private void initialize(){
        grupaSangeComboBox.getItems().addAll(GrupaSange.O1,GrupaSange.A2,GrupaSange.B3,GrupaSange.AB4);
        rhComboBox.getItems().addAll(RH.POZITIV,RH.NEGATIV);

    }
    private boolean verificareCerere(){
        RH rh = getRH();
        Importanta importanta = getImportanta();
        GrupaSange grupaSange = getGrupaSange();

        String nume = numePacientTextField.getText();
        String cnp = cnpPacientTextField.getText();
        Integer trombocite = Integer.valueOf(trombocitetTextField.getText());
        Integer plasma = Integer.valueOf(plasmaTextField.getText());
        Integer globule = Integer.valueOf(globuleRosiTextField.getText());

        String mesajEroare="";
        if(nume.isEmpty())
            mesajEroare+= "Nume necompletat\n";
        if(cnp.length() != 13)
            mesajEroare+= "Cnp necompletat\n";
        if(trombocite.equals(0) && plasma.equals(0) && globule.equals(0))
            mesajEroare+= "Nu ai cerut nicio punga\n";
        if(grupaSange == null)
            mesajEroare += "Nu ai selectat o grupa de sange\n";
        if(importanta == null)
            mesajEroare += "Nu ai selectat importanta\n";
        if(rh == null)
            mesajEroare += "Nu ai selectat rh\n";

        if(mesajEroare.isEmpty())
            return true;

        CustomMessageBox box = new CustomMessageBox("Error",mesajEroare);
        box.show();
        return false;
    }

    private Logger logger = LogManager.getLogger(CerereSangeController.class.getName());


    @FXML
    private void trimiteCerere() {
        logger.debug("Se trimite cerere");

        if (verificareCerere()) {

            CerereSange a = getCerereSange();

            String x = "Are importanta : " + getImportanta() + " si are nevoie de :"
                    + a.getNumarPungiTrombocite() + "," + a.getNumarPungiGlobuleRosii() + ","
                    + a.getNumarPungiPlasma();

            logger.info("Date introduse corecte in cerere de sange. " + x);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cerere trimisa");
            alert.setHeaderText(a.getNumePacient() + " "
                    + a.getCnpPacient() + " cu grupa " + a.getGrupaSange() + "si rh " + a.getRh());
            CustomMessageBox e = new CustomMessageBox("Cerere trimisa",x,0);
            e.show();
        }
        else
            logger.info("Date introduse gresit in cerere de sange");

    }

    @FXML
    private JFXRadioButton scazutaRadioButton;

    @FXML
    private JFXRadioButton medieRadioButton;

    @FXML
    private JFXRadioButton ridicataRadioButton;


    private void changeResult(JFXTextField punga,int value)
    {

        if(FunctiiUtile.isNumeric(punga.getText()))
        {
            String valoareNoua = String.valueOf((Integer.parseInt(punga.getText())+ value));

            if(Integer.parseInt(valoareNoua) >= 0)
            punga.setText(valoareNoua);
        }
        else
        {
            //Alert
            punga.setText("0");
        }
    }

    private CerereSange getCerereSange(){
        RH rh = getRH();
        Importanta importanta = getImportanta();
        GrupaSange grupaSange = getGrupaSange();

        String nume = numePacientTextField.getText();
        String cnp = cnpPacientTextField.getText();
        Integer trombocite = Integer.valueOf(trombocitetTextField.getText());
        Integer plasma = Integer.valueOf(plasmaTextField.getText());
        Integer globule = Integer.valueOf(globuleRosiTextField.getText());

        return new CerereSange(nume,cnp,grupaSange,rh,trombocite,globule,plasma,importanta);
    }


    @FXML
    private void trombociteDown(){
        logger.debug("Buton - pentru trombocite a fost apasat");
        changeResult(trombocitetTextField,-1);
    }
    @FXML
    private void trombociteUp(){
        logger.debug("Buton + pentru trombocite a fost apasat");
        changeResult(trombocitetTextField,+1);
    }
    @FXML
    private void globuleRosiDown(){

        logger.debug("Buton - pentru globule rosii a fost apasat");
        changeResult(globuleRosiTextField,-1);
    }
    @FXML
    private void globuleRosiUp(){

        logger.debug("Buton + pentru globule rosii a fost apasat");
        changeResult(globuleRosiTextField,+1);
    }
    @FXML
    private void plasmaDown(){
        logger.debug("Buton - pentru plasma a fost apasat");
        changeResult(plasmaTextField,-1);
    }
    @FXML
    private void plasmaUp(){
        logger.debug("Buton + pentru plasma a fost apasat");
        changeResult(plasmaTextField,1);
    }

    private Importanta getImportanta(){
        if(scazutaRadioButton.isSelected())
            return Importanta.SCAZUTA;
        else if(medieRadioButton.isSelected())
            return Importanta.MEDIE;
        else if(ridicataRadioButton.isSelected())
            return Importanta.RIDICATA;
        else
            return null;
    }

    private RH getRH(){
        return rhComboBox.getValue();
    }

    private GrupaSange getGrupaSange(){
        return grupaSangeComboBox.getValue();
    }


    public void setMainService(MainService mainService){
        this.mainService = mainService;
    }

    @Override
    public void setScreenParent(ControllerScreens screenParent) {
        this.controller = screenParent;
    }
}
