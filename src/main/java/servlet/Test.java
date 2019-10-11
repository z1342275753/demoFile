package servlet;

import servlet.entity.Myspring;
import servlet.entity.Question;

public class Test {
    public static void main(String[] args) {
        Myspring myspring=new Myspring();
        Question question= (Question) myspring.getMethod("servlet.entity.Question");
        System.out.println(question);
    }
}
