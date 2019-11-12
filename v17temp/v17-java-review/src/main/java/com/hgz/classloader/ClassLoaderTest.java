package com.hgz.classloader;

/**
 * @author huangguizhao
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //为什么不加载项目中存在的java.lang.String
        //类加载机制
        String str = new String();
        System.out.println(str.getClass().getClassLoader());//null

        Student student = new Student();
        System.out.println(student.getClass().getClassLoader());//AppClassLoader
        System.out.println(student.getClass().getClassLoader().getParent());//ExtClassLoader
        System.out.println(student.getClass().getClassLoader().getParent().getParent());//BootStrapClassLoader null


        Class<?> clazz = Class.forName("com.hgz.Product");
        System.out.println(clazz.getClassLoader());//java.lang.ClassNotFoundException: com.hgz.Product
    }
}
