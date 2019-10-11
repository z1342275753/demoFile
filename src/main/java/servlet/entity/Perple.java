package servlet.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Perple implements An, Serializable {

    @Override
    public void run(){
        System.out.println("人睡觉");
    }
    @SuppressWarnings({"rawtypes","deprecation"})
    public void run(String name){
        String name1;
        System.out.println(name+"睡觉");
//        List list=new ArrayList();
//        Date date=new Date(2000,1,1);

    }

}
