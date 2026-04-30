
package model;

public class User {

    String username;

    String password;

    public User(String u,String p){

        username=u;

        password=p;
    }

    public String getUsername(){

        return username;
    }

    public String getPassword(){

        return password;
    }
}