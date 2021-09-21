package com.java.g.w01;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;


public  class HelloXClassLoader extends ClassLoader{

    public static void main(String[] args) {
        HelloXClassLoader helloXClassLoader =new HelloXClassLoader();
        try {
            // 加载相应的类
            Class<?> clazz = helloXClassLoader.loadClass("Hello");
            //获取类对象
           // Class clazz=  helloXClassLoader.findClass("Hello");
            Object object =clazz.newInstance();
            //获取类方法
            Method[] methods = clazz.getDeclaredMethods();

            for(Method method:methods){
                //类对象调用方法
                System.out.println("---方法名："+method.getName());
                method.invoke(object);
            }

        } catch (Exception e) {
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
        InputStream inputStream =null;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            URL url = HelloXClassLoader.class.getClassLoader().getResource(fileName);
            File file = new File(url.getFile());
            inputStream = new FileInputStream(file);

            //  InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");
            byte[] byteBuffer = new byte[1];
            while(true){
                if (!(-1 != inputStream.read(byteBuffer))) break;
                baos.write(byteBuffer);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream !=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public void decode(byte[] bytes){
        for(int i=0;i<bytes.length;i++){
            bytes[i]=(byte)(255-bytes[i]);
        }
    }

}
