package Model;

public class Pacient {

    public String getIdMedic() {
        return idMedic;
    }

    public void setIdMedic(String idMedic) {
        this.idMedic = idMedic;
    }

    private String idMedic;

    private String nume;

    private String cnp;

    private GrupaSange grupaSange;

    private RH rh;

    public Pacient(String nume, String cnp, GrupaSange grupaSange, RH rh) {
        this.nume = nume;
        this.cnp = cnp;
        this.grupaSange = grupaSange;
        this.rh = rh;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public GrupaSange getGrupaSange() {
        return grupaSange;
    }

    public void setGrupaSange(GrupaSange grupaSange) {
        this.grupaSange = grupaSange;
    }

    public RH getRh() {
        return rh;
    }

    public void setRh(RH rh) {
        this.rh = rh;
    }
}
