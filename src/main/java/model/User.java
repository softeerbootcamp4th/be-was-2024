package model;


/**
 * User객체 class
 */
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;


    /**
     * User객체 생성자
     * @param userId 유저 Id
     * @param password 유저 비밀번호
     * @param name 유저 이름
     * @param email 유저 이메일
     */
    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }



    /**
     * userId getter
     */
    public String getUserId() {
        return userId;
    }


    /**
     * password getter
     */
    public String getPassword() {
        return password;
    }


    /**
     * name getter
     */
    public String getName() {
        return name;
    }



    /**
     * email getter
     */
    public String getEmail() {
        return email;
    }




    /**
     * User객체 디버깅을 위한 toString() 메소드
     */
    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
