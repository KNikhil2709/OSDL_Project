package util;

import java.io.*;
import java.util.*;

public class FileStorage {

    public static List<String> read(String file){

        List<String> list=new ArrayList<>();

        try{

            BufferedReader br=
                    new BufferedReader(new FileReader(file));

            String line;

            while((line=br.readLine())!=null){

                list.add(line);
            }

        }catch(Exception e){}

        return list;
    }

    public static void write(String file,String data){

        try{

            BufferedWriter bw=
                    new BufferedWriter(new FileWriter(file,true));

            bw.write(data);

            bw.newLine();

            bw.close();

        }catch(Exception e){}
    }
}