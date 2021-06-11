package uni.fmi.bachelors.todoapplication.database;

public class User {

    private int id;
    private String fName;
    private String lName;
    private  String username;
    private String password;
    private String gender;
    private String userLoginDate;
    private String userLogoutDate;



    //Used in RegisterActivity
    public User(String fName, String lName, String username, String password, String gender) {
        setFName(fName);
        setLName(lName);
        setUsername(username);
        setPassword(password);
        setGender(gender);
    }

    //For userLogHistory
    public User(String username, String userLoginDate, String userLogoutDate){
        setUsername(username);
        setUserLoginDate(userLoginDate);
        setUserLogoutDate(userLogoutDate);
    }



    public int getId() {
        return id;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserLoginDate() {
        return userLoginDate;
    }

    public String getUserLogoutDate() {
        return userLogoutDate;
    }

    public void setUserLoginDate(String userLoginDate) {
        this.userLoginDate = userLoginDate;
    }

    public void setUserLogoutDate(String userLogoutDate) {
        this.userLogoutDate = userLogoutDate;
    }
}
