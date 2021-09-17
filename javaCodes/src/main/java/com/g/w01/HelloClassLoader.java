package com.g.w01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        HelloClassLoader helloClassLoader=new HelloClassLoader();
        try {
            //获取类对象
           Class clazz=  helloClassLoader.findClass("Hello");
            Object object =clazz.newInstance();

            //获取类方法
            Method[] methods = clazz.getDeclaredMethods();

            for(Method method:methods){
                //类对象调用方法
                System.out.println("---方法名："+method.getName());
                method.invoke(object);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected Class<?> findClass(String name) {
        String filePath = "Hello.xlass";
        byte[] b = getFileBytes(filePath);
        this.decode(b);
        System.out.println(new String(b));
        return defineClass(name, b, 0, b.length);
    }

    private byte[] getFileBytes(String fileName) {
        byte[] result = null;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            URL url = HelloClassLoader.class.getClassLoader().getResource(fileName);
            File file = new File(url.getFile());
            InputStream   inputStream = new FileInputStream(file);

            //特别注意 ！！！！！！！！！！！！！！！！！！！！！！！！！
            byte[] byteBuffer = new byte[1];//需要一个一个字节的读取，当是1024时，报错Exception in thread "main" java.lang.ClassFormatError: Extra bytes at the end of class file Parent
            while(true){
                if (!(-1 != inputStream.read(byteBuffer))) break;
                baos.write(byteBuffer);
            }
            result = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void decode(byte[] bytes){
        for(int i=0;i<bytes.length;i++){
            bytes[i]=(byte)(255-bytes[i]);
        }
    }

}
