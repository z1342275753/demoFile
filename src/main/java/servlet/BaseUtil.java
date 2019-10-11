package servlet;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;

public class BaseUtil {
//    private static DataSource dataSource;
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            Class.forName("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC")
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn() throws SQLException {
        String a="123".toString();
//        dataSource= new BasicDataSource();
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC","root","root");
    }
    public static ResultSet exQ(String sql) throws SQLException {
        conn=getConn();
        ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }
    public static int ex(String sql) throws SQLException {
        ps = conn.prepareStatement(sql);
        return ps.executeUpdate();
    }
    public static void close(){

            try {
                if(rs!=null){
                rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

        }
    }
    @Deprecated
    public static void main(String[] args) {
//        String sql="SELECT * FROM `menu`.`admin` LIMIT 0, 1000";
//        try {
//            ResultSet rs=exQ(sql);
//            while (rs.next()){
//                System.out.println(rs.getString("username"));
//                System.out.println(rs.getString("password"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        Class<?> classType=null;
        try {
            classType=Class.forName("servlet.ASD");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
                System.out.println(classType.getPackage());
                Class<?> classs=classType.getSuperclass();
                while (classs!=null){
                    System.out.println(classs);
                    classs=classs.getSuperclass();
                }

//            Class<?> []classi=classType.getInterfaces();

            try {
                System.out.println(classType.getField("name"));
//                System.out.println(classType.getDeclaredField("a"));
                System.out.println(classType.getField("name").getModifiers());
//                classType.getField("name").set("cc","asdfghjkl");
//                System.out.println(classType.getField("name").get("cc"));
                ASD asd=new ASD();
                ASD asd1=new ASD();
                classType.getField("name").set(asd,"asd1");
                classType.getField("name").set(asd1,"asd2");
                System.out.println(classType.getField("name").get(asd));
                System.out.println(classType.getField("name").get(asd1));
//                System.out.println(classType.getField("a"));
//                Field [] fs=classType.getFields();
//                System.out.println(fs.length);
                Field f=classType.getDeclaredField("a");
                f.setAccessible(true);
                char[] bb=((String)(f.get(asd))).toCharArray();
                System.out.println(bb);
//                ((String) f.get(asd)).toCharArray()[0]='1';
//                char [] chars= "sllj".toCharArray();
//                char[] chars=s.toCharArray();
//                for(int i=0;i<chars.length;i++){
//                    System.out.print(chars[i]+" ");
//                }
                f.set(asd,"新中国");
                System.out.println(f.get(asd));
                Method [] methods=classType.getMethods();
//                for (Method method:methods
//                     ) {
//                    System.out.println(method);
//                }
                Method [] methods1=classType.getDeclaredMethods();
                for (Method method:methods1
                ) {
                    System.out.println(method);
                }
                Method method=classType.getDeclaredMethod("getA");
                System.out.println(method.getExceptionTypes());
                String ab= (String) method.invoke(asd);
                System.out.println();
                System.out.println(ab);

                Date date=new Date();
                date.getYear();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
