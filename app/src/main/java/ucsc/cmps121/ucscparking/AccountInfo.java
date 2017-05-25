package ucsc.cmps121.ucscparking;

/**
 * Created by katia on 5/24/2017.
 */

public class AccountInfo{
        private String fireBaseid;
        private String email;
    public AccountInfo()
    {
    }
    public AccountInfo(String fireBaseid, String email)
    {
        this.fireBaseid = fireBaseid;
        this.email = email;
    }
    public void setFireBaseid(String fireBaseid)
    {
        this.fireBaseid = fireBaseid;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getFireBaseid()
    {
        return fireBaseid;
    }
    public String getEmail()
    {
        return email;
    }

}
