package service;

import util.FileStorage;

public class AuthenticationService {

    String file="src/data/users.txt";

    public boolean login(String u,String p){

        for(String line:FileStorage.read(file)){

            String parts[]=line.split(",");

            if(parts[0].equals(u)
                    && parts[1].equals(p))

                return true;
        }

        return false;
    }

    public void signup(String u,String p){

        FileStorage.write(file,u+","+p);
    }
}