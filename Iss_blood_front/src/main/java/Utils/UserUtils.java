package Utils;

import Model.*;
import org.json.JSONObject;

import javax.naming.directory.InvalidAttributesException;

public class UserUtils {
    public static UserInfo GetUserInfoFromResponse (JSONObject loginResponse, String username) throws InvalidAttributesException {
        int type = loginResponse.getInt("user_type");
        UserInfo info = null;
        if(type == 1) //donator
        {
            Object returnedSex = loginResponse.get("sex");
            Sex sex = returnedSex != JSONObject.NULL ? Sex.valueOf((String)returnedSex) : Sex.MASCULIN;
            info = new DonatorInfo(username,
                    loginResponse.getString("nume"),
                    loginResponse.getString("prenume"),
                    loginResponse.getString("cnp"),
                    loginResponse.getString("telefon"),
                    loginResponse.getString("domiciliu_localitate"),
                    loginResponse.getString("domiciliu_judet"),
                    loginResponse.getString("domiciliu_adresa"),
                    loginResponse.getString("resedinta_localitate"),
                    loginResponse.getString("resedinta_judet"),
                    loginResponse.getString("resedinta_adresa"),
                    sex
                    );
        }
        else if(type == 2)
        {
            info = new MedicInfo(username,
                    loginResponse.getString("nume"),
                    loginResponse.getString("prenume"),
                    loginResponse.getInt("id_locatie"),
                    loginResponse.getString("nume_locatie"),
                    loginResponse.getString("cnp"));
        }
        else if(type == 3)
        {
            info = new StaffInfo(username,
                    loginResponse.getString("nume"),
                    loginResponse.getString("prenume"),
                    loginResponse.getInt("id_locatie"),
                    loginResponse.getString("nume_locatie"),
                    loginResponse.getString("nume_judet"),
                    loginResponse.getInt("id_judet"),
                    loginResponse.getString("cnp"));
        }
        else
        {
            throw new InvalidAttributesException("loginResponse contine un tip necunoscut. Tipul trebuie sa fie 1/2/3");
        }
        return info;
    }
}
