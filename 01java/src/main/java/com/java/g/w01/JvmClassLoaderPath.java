package com.java.g.w01;

import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JvmClassLoaderPath {
    public static void main(String[] args) {
        URL[] urls=Launcher.getBootstrapClassPath().getURLs();
        List<URL> urlList1= Arrays.asList(urls);

        System.out.println("启动类加载器");
        for(URL url:urls){
              System.out.println("===>"+url.getPath());
        }
        //扩展类加载器
//        URLClassLoader extClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader().getParent();
//        URL[] urls_ext=extClassLoader.getURLs();
//        List<URL> urlList_ext= Arrays.asList(urls_ext);
//        System.out.println("扩展类加载器");
//        for(URL url:urls_ext){
//            if( ! urlList1.contains(url)){
//                System.out.println("===>"+url.getPath());
//            }
//
//
//        }

        printClassLoader("扩展类加载器",JvmClassLoaderPath.class.getClassLoader().getParent());

        //应用类加载器
//        System.out.println("应用类加载器");
//        URLClassLoader appClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
//        URL[] urls_app=appClassLoader.getURLs();
//        for(URL url:urls_app){
//            if( !( urlList1.contains(url) || urlList_ext.contains(url))){
//                System.out.println("===>"+url.getPath());
//            }
//
//        }
        printClassLoader("应用类加载器",JvmClassLoaderPath.class.getClassLoader());
    }


    public static  void printClassLoader(String name,ClassLoader cl){
        if(cl !=null){
            System.out.println(name+" classLoader---"+cl.toString());
            printUrlForClassLoader(cl);
        }
    }

    public static void printUrlForClassLoader(ClassLoader cl){
        Object ucp= insightField(cl,"ucp");
        Object path= insightField(ucp,"path");
        ArrayList pth=(ArrayList)path;
        if(pth==null){
            return;
        }
        for(Object p:pth){
            System.out.println("=====>"+p.toString());
        }

    }

    private static Object insightField(Object obj,String fName){
        try{
            Field f=null;
            if(obj instanceof URLClassLoader){
                f=URLClassLoader.class.getDeclaredField(fName);
            }else{
                f=obj.getClass().getDeclaredField(fName);
            }
            f.setAccessible(true);
            return f.get(obj);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
