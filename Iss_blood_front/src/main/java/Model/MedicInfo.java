package Model;

public class MedicInfo extends UserInfo {
    private String nume;
    private String prenume;
    private Integer idLocatie;

    public MedicInfo(String username, String nume, String prenume, Integer idLocatie) {
        super(username);
        this.nume = nume;
        this.prenume = prenume;
        this.idLocatie = idLocatie;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public Integer getIdLocatie() {
        return idLocatie;
    }
}
