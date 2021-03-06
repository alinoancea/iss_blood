package Service;

import Communication.FlaskClient;
import Model.Pacient;
import Model.RegisterInfo;
import Model.*;
import Utils.Observer;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class MainService {

    FlaskClient flaskClient;

    public MainService(FlaskClient flaskClient) {
        this.flaskClient = flaskClient;
    }

    public Pair<UserInfo, String> login(String user, String pass) {
        return flaskClient.login(user, pass);
    }

    public Pair<Boolean, String> register(RegisterInfo info)
    {
        return flaskClient.register(info);
    }

    public Pair<Boolean, String> addPacient(Pacient pacient) { return  flaskClient.addPacient(pacient); }

    public Pair<Boolean, String> userTrimiteFormularDonare(FormularDonare formular, String username){return flaskClient.userTrimiteFormularDonare(formular, username);}

    public Pair<Boolean, String> staffTrimiteFormularDonare (FormularDonare formularDonare) { return flaskClient.staffTrimiteFormularDonare(formularDonare);}

    public List<FormularDonare> getFormulareDonariDupaLocatie(int i){
        return flaskClient.getFormulareDonariDupaLocatie(i);

    }
    public Pair<Boolean, String> staffUpdateFormularDonare(FormularDonare formularDonare,int id_locatie, String staffFullName){
        return flaskClient.staffUpdateFormularDonare(formularDonare,id_locatie, staffFullName);
    }

    public void staffTrimiteAnaliza(Integer idLocatie, FormularDonare cerereDonare, Analiza analiza) {
        flaskClient.staffTrimiteAnaliza(cerereDonare,analiza,idLocatie);
    }

    public Map<String, List<Integer>> getStocCurent(int idLocatie){
        return flaskClient.getStocCurent(idLocatie);
    }

    public List<Analiza> getAnalize(String cnp){
        return flaskClient.getAnalize(cnp);
    }

    public Pair<Boolean, String> trimitePungi(int idCerere, int idLocatie,
                                              GrupaSange grupaSange, RH rh, int plasma, int trombocite, int globule){
        return flaskClient.trimitePungi(idCerere,  idLocatie, grupaSange, rh, plasma,trombocite, globule);

    }

    public void addObserver(Observer controlledScreen) {
        flaskClient.addObserver(controlledScreen);
    }

    public List<CerereSange> getCereriSange(int id_locatie,String status,boolean fromSpital)
    {
     return flaskClient.getCereriSange(id_locatie,status,fromSpital);
    }

    public Pair<Boolean, String> trimiteCerereSange(CerereSange cerere, String cnpMedic) {
        return flaskClient.trimiteCerereSange(cerere, cnpMedic);
    }

    public List<DonareInfo> getIstoricDonare(String username)
    {
        return flaskClient.getIstoricDonare(username);
    }
    public void anulare(Integer id) {
        flaskClient.anulareCerere(id);
    }

    public Pair<Boolean, String> isAValidDonation(String cnpDonator) {
        return flaskClient.isAValidDonation(cnpDonator);
    }
    public List<Pair<String, String>> getActiveUser(String cnp){
        return flaskClient.getActiveUser( cnp);
    }

    public Map<String,Integer> getCentruHomeScreenData(Integer idLocatie) {
        return flaskClient.getCentruHomeScreenData(idLocatie);
    }
    public List<StarePacient> getStareActuala(int idLocatie){
        return flaskClient.getStareActuala(idLocatie);
    }

    public Pair<Integer, String> addUser(String cnp){
        return flaskClient.addActiveUser(cnp);
    }

    public void removeUser(String cnp) {
        flaskClient.removeActiveUser(cnp);
    }

    public List<Pair<String,Boolean>> getMessages(String username, String sender) {
        return flaskClient.getMessages(username,sender);
    }

    public void addMessage(String username, String sender, String effect) {
        flaskClient.addMessage(username,sender,effect);
    }
}
