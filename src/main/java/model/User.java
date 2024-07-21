package model;

import exception.InvalidHttpRequestException;

import java.util.Map;

/**
 * 유저의 정보를 저장하는 클래스
 */
public class User implements MyTagDomain{
    private int id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(int id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> map) {
        this(
                getValueFromMap(map, "userId"),
                getValueFromMap(map, "password"),
                getValueFromMap(map, "name"),
                getValueFromMap(map, "email")
        );
    }

    /**
     * 유저의 특정 필드 값을 map에서 꺼내서 반환한다.
     * 필드 값이 null이거나 비어있다면 예외가 발생한다.
     *
     * @param map : 유저의 정보가 저장된 map
     * @param key : 유저 클래스의 필드 이름
     * @return : 유저 클래스의 필드 값
     */
    public static String getValueFromMap(Map<String, String> map, String key) {
        String value = map.get(key);
        if (value == null || value.isEmpty()) {
            throw new InvalidHttpRequestException("User information cannot be null");
        }
        return value;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
