package com.example.tomy.acg_project.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tomy on 18-3-9.
 */
public class HttpUnit {
    public static void postHttpRequest(final JSONObject requestMsg, final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Charset","UTF-8");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //connection.setRequestProperty("Content-Type","application/string;charset=UTF-8");
                    connection.setRequestProperty("requestMsg",requestMsg.toString());
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String response=reader.readLine();
                    //JSONObject responseJson=new JSONObject(response);
                    if(listener!=null){
                        listener.onFinish(response);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    if(listener!=null){
                        listener.onError(e);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            }
        }).start();
    }

//    public static void photoHttpRequest(final String file, final String address, final HttpCallbackListener listener){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String end = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "*****";
//                String newName = "image.jpg";
//                //String uploadFile = "storage/sdcard1/bagPictures/102.jpg";
//                String uploadFile = file;
//                //String actionUrl = "http://192.168.1.123:8080/upload/servlet/UploadServlet";
//                String actionUrl = address;
//                try {
//                    URL url = new URL(actionUrl);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            /* 允许Input、Output，不使用Cache */
//                    con.setDoInput(true);
//                    con.setDoOutput(true);
//                    con.setUseCaches(false);
//            /* 设置传送的method=POST */
//                    con.setRequestMethod("POST");
//            /* setRequestProperty */
//                    con.setRequestProperty("Connection", "Keep-Alive");
//                    con.setRequestProperty("Charset", "UTF-8");
//                    con.setRequestProperty("Content-Type",
//                            "multipart/form-data;boundary=" + boundary);
//
//            /* 设置DataOutputStream */
//                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//                    ds.writeBytes(twoHyphens + boundary + end);
//                    ds.writeBytes("Content-Disposition: form-data; "
//                            + "name=\"file1\";filename=\"" + newName + "\"" + end);
//                    ds.writeBytes(end);
//            /* 取得文件的FileInputStream */
//                    FileInputStream fStream = new FileInputStream(uploadFile);
//            /* 设置每次写入1024bytes */
//                    int bufferSize = 1024;
//                    byte[] buffer = new byte[bufferSize];
//                    int length = -1;
//            /* 从文件读取数据至缓冲区 */
//                    while ((length = fStream.read(buffer)) != -1) {
//                /* 将资料写入DataOutputStream中 */
//                        ds.write(buffer, 0, length);
//                    }
//                    ds.writeBytes(end);
//                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
//            /* close streams */
//                    fStream.close();
//                    ds.flush();
//            /* 取得Response内容 */
//                    InputStream is = con.getInputStream();
//                    int ch;
//                    StringBuffer b = new StringBuffer();
//                    while ((ch = is.read()) != -1) {
//                        b.append((char) ch);
//                    }
//            /* 将Response显示于Dialog */
//                    System.out.println("上传成功" + b.toString().trim());
//            /* 关闭DataOutputStream */
//                    ds.close();
//                    con.disconnect();
//                } catch (Exception e) {
//                    System.out.println("上传失败" + e);
//                }
//            }
//        }).start();
//    }

    /* 上传文件至Server的方法 */
    public static void uploadFile(final String file, final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                String newName = Domain.getUserId()+".png";
                String uploadFile = file;
                //String actionUrl = "http://192.168.1.123:8080/upload/servlet/UploadServlet";
                String actionUrl = address;
                try {
                    URL url = new URL(actionUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                /* 设置DataOutputStream */
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; "
                            + "name=\"file1\";filename=\"" + newName + "\"" + end);
                    ds.writeBytes(end);
                /* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(uploadFile);
                /* 设置每次写入1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                /* 从文件读取数据至缓冲区 */
                    while ((length = fStream.read(buffer)) != -1) {
                    /* 将资料写入DataOutputStream中 */
                        ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                /* close streams */
                    fStream.close();
                    ds.flush();
                /* 取得Response内容 */
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                /* 将Response显示于Dialog */
                    System.out.println("上传成功" + b.toString().trim());
                /* 关闭DataOutputStream */
                    ds.close();
                } catch (Exception e) {
                    System.out.println("上传失败" + e);
                }
            }
        }).start();

    }
}
