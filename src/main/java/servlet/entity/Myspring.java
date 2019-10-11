package servlet.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
public class Myspring {
    /**
     *
     * @param className 类名（包.类格式）
     * @return 一个对象
     */
    public Object getMethod(String className) {
        Class<?> classObj =null;
        Object object =null;
        try {
            //获取类
            classObj= Class.forName(className);
            Scanner scanner = new Scanner(System.in);
            System.out.println("请给" + className + "赋值？");
            //创建对象
            object= classObj.newInstance();
            //获取属性
            Field[] fields = classObj.getDeclaredFields();
            for (Field field : fields
            ) {
                String name = field.getName();
                //获取方法名首字母大写
                String firstName = name.substring(0, 1).toUpperCase();
                //获取方法名剩余
                String otherName = name.substring(1);
                //拼接setName方法
                StringBuilder setMethodName = new StringBuilder("set");
                setMethodName.append(firstName);
                setMethodName.append(otherName);
                //获取属性类型（类形式）
                Class fieldClass = field.getType();
                //获取方法
                Method method = classObj.getMethod(setMethodName.toString(), fieldClass);
                System.out.println("请给" + name + "赋值");
                String value = scanner.nextLine();
                //类型转换
                Constructor con = fieldClass.getConstructor(String.class);
                //方法执行（对象,参数）
                method.invoke(object, con.newInstance(value));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
            return object;
    }
}
