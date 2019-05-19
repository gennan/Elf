package com.example.elf.utils.netrequsethelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetRequestHelper {
    /**
     * 网络请求工具类
     * 通过Request类来传参数，POST时默认字符编码为UTF-8
     */

        private static volatile NetRequestHelper sWebioToolInstance = null;
        private static ExecutorService sWebioToolThreadPool =
                Executors.newCachedThreadPool();//一个项目就只创建一个线程池

        public interface Callback {
            void onSucceeded(String response);

            void onFailed(String response);
        }

        private NetRequestHelper() {
        }

        public static NetRequestHelper obtainNetRequestHelper() {
            if (sWebioToolInstance == null) {
                synchronized (NetRequestHelper.class) {
                    if (sWebioToolInstance == null) {
                        sWebioToolInstance = new NetRequestHelper();
                    }
                }
            }
            return sWebioToolInstance;
        }

        public void excute(NetRequestOptions netWrokOption, Callback callback) {
            //对线程池是否开启做一次判断
            if (sWebioToolThreadPool.isShutdown()) {
                sWebioToolThreadPool = Executors.newCachedThreadPool();
                doRequest(netWrokOption, callback);
            } else {
                doRequest(netWrokOption, callback);
            }
        }

        public void doRequest(final NetRequestOptions netWrokOption, final Callback callback) {
            sWebioToolThreadPool.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(netWrokOption.getUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        if ("POST".equals(netWrokOption.getMethod())) {
                            connection.setConnectTimeout(5 * 1000);
                            connection.setReadTimeout(5 * 1000);
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setUseCaches(false);//不适用缓存
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type",
                                    netWrokOption.getProperty());//设置Content-Type
                            connection.setRequestProperty("Accept-Charset", "UTF-8");//设置字符编码
                            connection.connect();
                            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                            dos.write(netWrokOption.getParams().getBytes());
                            dos.flush();
                            dos.close();
                            if (connection.getResponseCode() == 200) {
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                callback.onSucceeded(response.toString());
                                in.close();
                                connection.disconnect();
                            } else {
                                callback.onFailed(connection.getResponseMessage());//返回失败的信息
                                connection.disconnect();
                            }
                        } else if ("GET".equals(netWrokOption.getMethod())) {
                            connection.setConnectTimeout(5 * 1000);
                            connection.setReadTimeout(5 * 1000);
                            connection.setUseCaches(true);//设置使用缓存
                            connection.setRequestMethod("GET");
//                        connection.setRequestProperty("Content-Type",);//设置请求中的媒体类型信息。
//                        connection.addRequestProperty("Connection", );//设置客户端与服务连接类型
                            connection.connect();
                            if (connection.getResponseCode() == 200) {
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                callback.onSucceeded(response.toString());
                                in.close();
                                connection.disconnect();
                            } else {
                                callback.onFailed(connection.getResponseMessage());//返回失败的信息
                                connection.disconnect();
                            }
                        } else {
                            throw new Exception("ClientUtil中的request.getMethod()的值不合法");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

}
