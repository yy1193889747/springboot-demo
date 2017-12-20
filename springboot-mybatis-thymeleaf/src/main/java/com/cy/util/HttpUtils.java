package com.cy.util;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cy
 * 2017/12/20 18:42
 */
public class HttpUtils {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * java net 包 发送post请求
     *
     * @param path url
     * @param post xx=xx&yy=yy
     * @return 根据需要返回 页面 或者协议头
     */

    public static String netpost(String path, String post) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置禁止重定向
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);// post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            // 开始获取数据
            Map<String, List<String>> ss = httpURLConnection.getHeaderFields();
            List cookie = ss.get("Set-Cookie");
            for (int i = 0; i < cookie.size(); i++) {
                System.out.println(cookie.get(i).toString());
            }
            String cc = cookie.get(2).toString().substring(21, 53);
            System.out.println(cc);

            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            System.out.println(bos.toString());
            // return bos.toString("utf-8");
            return cc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> getCookie() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        Map<String, String> cookiemap = new HashMap<String, String>();
        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).build();// 不允许重定向
        CookieStore cookieStore = new BasicCookieStore();// 整个cookiestore

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).setSSLSocketFactory(sslsf).build();
        // 创建httppost
        HttpPost httppost = new HttpPost("");
        // 创建参数队列
        List formparams = new ArrayList();
        formparams.add(new BasicNameValuePair("username", "cy"));
        System.out.println(formparams.toString());
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpClient.execute(httppost);
            try {
                HeaderIterator headerIterator = response.headerIterator();
                if (headerIterator != null) {
                    System.out.println("--------------------------------------");
                    while (headerIterator.hasNext()) {
                        Header header = headerIterator.nextHeader();
                        System.out.println("Response header: " + header.getName() + "---" + header.getValue());
                    }
                    System.out.println("--------------------------------------");
                }
                List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("cookie ---" + cookies.get(i).toString());
                }
                return cookiemap;
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
      }
        return cookiemap;
    }
    public static void main(String[] args) throws KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        getCookie();
    }

}
