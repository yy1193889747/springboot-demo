package com.ocly.task;

import com.alibaba.fastjson.JSONObject;
import com.ocly.util.RuoKuai;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import sun.misc.BASE64Encoder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cy
 * 2017/12/30 11:11
 */
public class Weibo {
    private String username;    //登录帐号(明文)
    private String password;    //登录密码(明文)


    private String su;          //登录帐号(Base64加密)
    private String sp;          //登录密码(各种参数RSA加密后的密文)
    private String servertime;  //初始登录时，服务器返回的时间戳,用以密码加密以及登录用
    private String nonce;       //初始登录时，服务器返回的一串字符，用以密码加密以及登录用
    private String pcid;       //获取图片的参数
    private String pubkey;   //初始登录时，服务器返回的RSA公钥

    private String errInfo;     //登录失败时的错误信息
    private String location;    //登录成功后的跳转连接


    public void login(String usename, String password, String text) throws IOException, InterruptedException {
        this.username = usename;
        this.password = password;
        su = new BASE64Encoder().encode(usename.getBytes());
        String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=" + su + "&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.18)&_=" +
                getTimestamp();
        Connection.Response execute = Jsoup.connect(url).ignoreContentType(true).execute();
        String body = execute.body();
        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(StringUtils.substringBetween(body, "(", ")"));
        servertime = jsonObject.getString("servertime");
        nonce = jsonObject.getString("nonce");
        pubkey = jsonObject.getString("pubkey");
        pcid = jsonObject.getString("pcid");
        encodePwd();
        Map<String, String> photocookie = execute.cookies();
        url = "http://login.sina.com.cn/cgi/pin.php?r=54474015&s=0&p=" + pcid;
        byte[] bytes = Jsoup.connect(url).ignoreContentType(true).cookies(photocookie).execute().bodyAsBytes();
        String code = RuoKuai.getCode(bytes);

        Thread.sleep(2000);

        url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)";
        String post = "entry=weibo&gateway=1&from=&savestate=7&useticket=1&pagerefer=http%3A%2F%2Fpassport.weibo" +
                ".com%2Fvisitor%2Fvisitor%3Fentry%3Dminiblog%26a%3Denter%26url%3Dhttp%253A%252F%252Fweibo.com%252F%26domain%3D.weibo" +
                ".com%26ua%3Dphp-sso_sdk_client-0.6.14%26_rand%3D1441434306.495&pcid=" + pcid + "&door=" + code + "&vsnf=1&su=" + su + "&service=miniblog&servertime=" + servertime + "&nonce=" + nonce
                + "&pwencode=rsa2&rsakv=1330428213&sp=" + sp + "&sr=1366*768&encoding=UTF-8&url=http%3A%2F%2Fweibo.com%2Fajaxlogin.php%3Fframelogin%3D1%26callback%3Dparent.sinaSSOController.feedBackUrlCallBack&returntype=META";

        Connection.Response execute1 = Jsoup.connect(url).method(Connection.Method.POST).requestBody(post).cookies(photocookie).ignoreContentType(true).execute().charset("GBK");
        String body1 = execute1.body();
        Map<String, String> cookies = execute1.cookies();
        System.out.println(body1);
        System.out.println(body1.indexOf("正在登录") != -1 ? "登录成功" : "登录失败");

        url = StringUtils.substringBetween(body1, "location.replace('", "'");
        Connection.Response execute2 = Jsoup.connect(url).cookies(cookies).followRedirects(false).ignoreContentType(true).execute();
        Map<String, String> cookies1 = execute2.cookies();
        Map<String, String> headers = execute2.headers();
        System.out.println(headers.toString());
        System.out.println(cookies1.toString());
        url = "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&sudaref=login.sina.com.cn";
        String body2 = Jsoup.connect(url).cookies(cookies1).ignoreContentType(true).execute().body();
        String uid = StringUtils.substringBetween(body2, "uniqueid\":\"", "\",\"userid");

        String referer = "https://weibo.com/u/" + uid + "/home";

        url = "https://weibo.com/aj/mblog/add?ajwvr=6&__rnd=" + getTimestamp();
        post = "location=v6_content_home&appkey=&style_type=1&pic_id=&text=" + text + "&pdetail=&rank=0&rankid=&module=stissue&pub_source=main_&pub_type=dialog&_t=0";

        Connection.Response execute3 = Jsoup.connect(url).method(Connection.Method.POST).requestBody(post).cookies(cookies1).referrer(referer).ignoreContentType(true).execute().charset("GBK");
        System.out.println(execute3.body());


    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private String getTimestamp() {
        Date now = new Date();
        return Long.toString(now.getTime());
    }

    /**
     * js动态加密
     *
     * @return
     */
    private boolean encodePwd() {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        try {
            // FileReader fr = new FileReader("");
            se.eval(LOGIN_JS);
            Invocable invocableEngine = (Invocable) se;
            sp = (String) invocableEngine.invokeFunction("getPW", password, servertime, nonce, pubkey);
            return true;
        } catch (ScriptException e) {
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Weibo weibo = new Weibo();
        weibo.login("", "", "Jsoup_Login_Success");

    }

//retcode=101 账号密码错误
//retcode=80 请输入正确的密码
//retcode=4049 输入验证码
//retcode=2070 验证码错误


    private static final String LOGIN_JS = "var sinaSSOEncoder = sinaSSOEncoder || {};\n" +
            "var navigator =navigator||{};\n" +
            "(function () {\n" +
            "    var a = 0, b = 8;\n" +
            "    this.hex_sha1 = function (a) {\n" +
            "        return i(c(h(a), a.length * b));\n" +
            "    };\n" +
            "    var c = function (a, b) {\n" +
            "        a[b >> 5] |= 128 << 24 - b % 32;\n" +
            "        a[(b + 64 >> 9 << 4) + 15] = b;\n" +
            "        var c = Array(80), h = 1732584193, i = -271733879, j = -1732584194, k = 271733878, l = -1009589776;\n" +
            "        for (var m = 0; m < a.length; m += 16) {\n" +
            "            var n = h, o = i, p = j, q = k, r = l;\n" +
            "            for (var s = 0; s < 80; s++) {\n" +
            "                s < 16 ? c[s] = a[m + s] : c[s] = g(c[s - 3] ^ c[s - 8] ^ c[s - 14] ^ c[s - 16], 1);\n" +
            "                var t = f(f(g(h, 5), d(s, i, j, k)), f(f(l, c[s]), e(s)));\n" +
            "                l = k;\n" +
            "                k = j;\n" +
            "                j = g(i, 30);\n" +
            "                i = h;\n" +
            "                h = t;\n" +
            "            }\n" +
            "            h = f(h, n);\n" +
            "            i = f(i, o);\n" +
            "            j = f(j, p);\n" +
            "            k = f(k, q);\n" +
            "            l = f(l, r);\n" +
            "        }\n" +
            "        return [h, i, j, k, l];\n" +
            "    }, d = function (a, b, c, d) {\n" +
            "        return a < 20 ? b & c | ~b & d : a < 40 ? b ^ c ^ d : a < 60 ? b & c | b & d | c & d : b ^ c ^ d;\n" +
            "    }, e = function (a) {\n" +
            "        return a < 20 ? 1518500249 : a < 40 ? 1859775393 : a < 60 ? -1894007588 : -899497514;\n" +
            "    }, f = function (a, b) {\n" +
            "        var c = (a & 65535) + (b & 65535), d = (a >> 16) + (b >> 16) + (c >> 16);\n" +
            "        return d << 16 | c & 65535;\n" +
            "    }, g = function (a, b) {\n" +
            "        return a << b | a >>> 32 - b;\n" +
            "    }, h = function (a) {\n" +
            "        var c = [], d = (1 << b) - 1;\n" +
            "        for (var e = 0; e < a.length * b; e += b)\n" +
            "            c[e >> 5] |= (a.charCodeAt(e / b) & d) << 24 - e % 32;\n" +
            "        return c;\n" +
            "    }, i = function (b) {\n" +
            "        var c = a ? \"0123456789ABCDEF\" : \"0123456789abcdef\", d = \"\";\n" +
            "        for (var e = 0; e < b.length * 4; e++)\n" +
            "            d += c.charAt(b[e >> 2] >> (3 - e % 4) * 8 + 4 & 15) + c.charAt(b[e >> 2] >> (3 - e % 4) * 8 & 15);\n" +
            "        return d;\n" +
            "    }, j = function (a) {\n" +
            "        var b = \"\", c = 0;\n" +
            "        for (; c < a.length; c++)\n" +
            "            b += \"%\" + k(a[c]);\n" +
            "        return decodeURIComponent(b);\n" +
            "    }, k = function (a) {\n" +
            "        var b = \"0\" + a.toString(16);\n" +
            "        return b.length <= 2 ? b : b.substr(1);\n" +
            "    };\n" +
            "    this.base64 = { encode: function (a) {\n" +
            "            a = \"\" + a;\n" +
            "            if (a == \"\")\n" +
            "                return \"\";\n" +
            "            var b = \"\", c, d, e = \"\", f, g, h, i = \"\", j = 0;\n" +
            "            do {\n" +
            "                c = a.charCodeAt(j++);\n" +
            "                d = a.charCodeAt(j++);\n" +
            "                e = a.charCodeAt(j++);\n" +
            "                f = c >> 2;\n" +
            "                g = (c & 3) << 4 | d >> 4;\n" +
            "                h = (d & 15) << 2 | e >> 6;\n" +
            "                i = e & 63;\n" +
            "                isNaN(d) ? h = i = 64 : isNaN(e) && (i = 64);\n" +
            "                b = b + this._keys.charAt(f) + this._keys.charAt(g) + this._keys.charAt(h) + this._keys.charAt(i);\n" +
            "                c = d = e = \"\";\n" +
            "                f = g = h = i = \"\";\n" +
            "            } while(j < a.length);\n" +
            "            return b;\n" +
            "        }, decode: function (a, b, c) {\n" +
            "            var d = function (a, b) {\n" +
            "                for (var c = 0; c < a.length; c++)\n" +
            "                    if (a[c] === b)\n" +
            "                        return c;\n" +
            "                return -1;\n" +
            "            };\n" +
            "            typeof a == \"string\" && (a = a.split(\"\"));\n" +
            "            var e = [], f, g, h = \"\", i, j, k, l = \"\";\n" +
            "            a.length % 4 == 0;\n" +
            "            var m = /[^A-Za-z0-9+\\/=]/, n = this._keys.split(\"\");\n" +
            "            if (b == \"urlsafe\") {\n" +
            "                m = /[^A-Za-z0-9-_=]/;\n" +
            "                n = this._keys_urlsafe.split(\"\");\n" +
            "            }\n" +
            "            if (b == \"subp_v2\") {\n" +
            "                m = /[^A-Za-z0-9_=-]/;\n" +
            "                n = this._subp_v2_keys.split(\"\");\n" +
            "            }\n" +
            "            if (b == \"subp_v3_3\") {\n" +
            "                m = /[^A-Za-z0-9-_.-]/;\n" +
            "                n = this._subp_v3_keys_3.split(\"\");\n" +
            "            }\n" +
            "            var o = 0;\n" +
            "            if (b == \"binnary\") {\n" +
            "                n = [];\n" +
            "                for (o = 0; o <= 64; o++)\n" +
            "                    n[o] = o + 128;\n" +
            "            }\n" +
            "            if (b != \"binnary\" && m.test(a.join(\"\")))\n" +
            "                return c == \"array\" ? [] : \"\";\n" +
            "            o = 0;\n" +
            "            do {\n" +
            "                i = d(n, a[o++]);\n" +
            "                j = d(n, a[o++]);\n" +
            "                k = d(n, a[o++]);\n" +
            "                l = d(n, a[o++]);\n" +
            "                f = i << 2 | j >> 4;\n" +
            "                g = (j & 15) << 4 | k >> 2;\n" +
            "                h = (k & 3) << 6 | l;\n" +
            "                e.push(f);\n" +
            "                k != 64 && k != -1 && e.push(g);\n" +
            "                l != 64 && l != -1 && e.push(h);\n" +
            "                f = g = h = \"\";\n" +
            "                i = j = k = l = \"\";\n" +
            "            } while(o < a.length);\n" +
            "            if (c == \"array\")\n" +
            "                return e;\n" +
            "            var p = \"\", q = 0;\n" +
            "            for (; q < e.lenth; q++)\n" +
            "                p += String.fromCharCode(e[q]);\n" +
            "            return p;\n" +
            "        }, _keys: \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\", _keys_urlsafe: \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=\", _subp_v2_keys: \"uAL715W8e3jJCcNU0lT_FSXVgxpbEDdQ4vKaIOH2GBPtfzqsmYZo-wRM9i6hynrk=\", _subp_v3_keys_3: \"5WFh28sGziZTeS1lBxCK-HgPq9IdMUwknybo.LJrQD3uj_Va7pE0XfcNR4AOYvm6t\" };\n" +
            "    this.Cookie = { decode: function (a) {\n" +
            "            var b = [], c = a.substr(0, 3), d = a.substr(3);\n" +
            "            switch (c) {\n" +
            "                case \"v01\":\n" +
            "                    for (var e = 0; e < d.length; e += 2)\n" +
            "                        b.push(parseInt(d.substr(e, 2), 16));\n" +
            "                    return decodeURIComponent(j(sinaSSOEncoder.base64.decode(b, \"binnary\", \"array\")));\n" +
            "                case \"v02\":\n" +
            "                    d = d.replace(/\\./g, \"=\");\n" +
            "                    b = sinaSSOEncoder.base64.decode(d, \"urlsafe\", \"array\");\n" +
            "                    return j(sinaSSOEncoder.base64.decode(b, \"binnary\", \"array\"));\n" +
            "                default:\n" +
            "                    return decodeURIComponent(a);\n" +
            "            }\n" +
            "        } };\n" +
            "    this.getSUBPCookie = { __parse: function (a) {\n" +
            "            var b, c, d, e, f, g = 0, h, i = {}, k = \"\", l = \"\";\n" +
            "            if (!a)\n" +
            "                return i;\n" +
            "            do {\n" +
            "                c = a[g];\n" +
            "                b = ++g;\n" +
            "                for (h = g; h < c + b; h++, g++)\n" +
            "                    k += String.fromCharCode(a[h]);\n" +
            "                e = a[g];\n" +
            "                b = ++g;\n" +
            "                if (k == \"status\" || k == \"flag\")\n" +
            "                    for (h = g; h < e + b; h++, g++)\n" +
            "                        l += a[h];\n" +
            "                else {\n" +
            "                    l = a.slice(h, e + b);\n" +
            "                    try  {\n" +
            "                        l = j(l);\n" +
            "                    } catch (m) {\n" +
            "                        l = \"\";\n" +
            "                    }\n" +
            "                    g += e;\n" +
            "                }\n" +
            "                i[k] = l;\n" +
            "                k = \"\";\n" +
            "                l = \"\";\n" +
            "            } while(g < a.length);\n" +
            "            return i;\n" +
            "        }, decode: function (a) {\n" +
            "            var b = [], c, d = a.substr(0, 3), e = decodeURIComponent(a.substr(3));\n" +
            "            switch (d) {\n" +
            "                case \"002\":\n" +
            "                    b = sinaSSOEncoder.base64.decode(e, \"subp_v2\", \"array\");\n" +
            "                    return sinaSSOEncoder.getSUBPCookie.__parse(b);\n" +
            "                case \"003\":\n" +
            "                    c = e.substr(0, 1);\n" +
            "                    b = sinaSSOEncoder.base64.decode(e, \"subp_v3_\" + c, \"array\");\n" +
            "                    return sinaSSOEncoder.getSUBPCookie.__parse(b);\n" +
            "                default:\n" +
            "                    return decodeURIComponent(a);\n" +
            "            }\n" +
            "        } };\n" +
            "}).call(sinaSSOEncoder);\n" +
            "(function () {\n" +
            "    function bt(a) {\n" +
            "        var b = bp(a, this.n.bitLength() + 7 >> 3);\n" +
            "        if (b == null)\n" +
            "            return null;\n" +
            "        var c = this.doPublic(b);\n" +
            "        if (c == null)\n" +
            "            return null;\n" +
            "        var d = c.toString(16);\n" +
            "        return (d.length & 1) == 0 ? d : \"0\" + d;\n" +
            "    }\n" +
            "    function bs(a) {\n" +
            "        return a.modPowInt(this.e, this.n);\n" +
            "    }\n" +
            "    function br(a, b) {\n" +
            "        if (a != null && b != null && a.length > 0 && b.length > 0) {\n" +
            "            this.n = bm(a, 16);\n" +
            "            this.e = parseInt(b, 16);\n" +
            "        } else\n" +
            "            alert(\"Invalid RSA public key\");\n" +
            "    }\n" +
            "    function bq() {\n" +
            "        this.n = null;\n" +
            "        this.e = 0;\n" +
            "        this.d = null;\n" +
            "        this.p = null;\n" +
            "        this.q = null;\n" +
            "        this.dmp1 = null;\n" +
            "        this.dmq1 = null;\n" +
            "        this.coeff = null;\n" +
            "    }\n" +
            "    function bp(a, b) {\n" +
            "        if (b < a.length + 11) {\n" +
            "            alert(\"Message too long for RSA\");\n" +
            "            return null;\n" +
            "        }\n" +
            "        var c = [], e = a.length - 1;\n" +
            "        while (e >= 0 && b > 0) {\n" +
            "            var f = a.charCodeAt(e--);\n" +
            "            if (f < 128)\n" +
            "                c[--b] = f;\n" +
            "            else if (f > 127 && f < 2048) {\n" +
            "                c[--b] = f & 63 | 128;\n" +
            "                c[--b] = f >> 6 | 192;\n" +
            "            } else {\n" +
            "                c[--b] = f & 63 | 128;\n" +
            "                c[--b] = f >> 6 & 63 | 128;\n" +
            "                c[--b] = f >> 12 | 224;\n" +
            "            }\n" +
            "        }\n" +
            "        c[--b] = 0;\n" +
            "        var g = new bl, h = [];\n" +
            "        while (b > 2) {\n" +
            "            h[0] = 0;\n" +
            "            while (h[0] == 0)\n" +
            "                g.nextBytes(h);\n" +
            "            c[--b] = h[0];\n" +
            "        }\n" +
            "        c[--b] = 2;\n" +
            "        c[--b] = 0;\n" +
            "        return new d(c);\n" +
            "    }\n" +
            "    function bo(a) {\n" +
            "        return a < 16 ? \"0\" + a.toString(16) : a.toString(16);\n" +
            "    }\n" +
            "    function bn(a, b) {\n" +
            "        var c = \"\", d = 0;\n" +
            "        while (d + b < a.length) {\n" +
            "            c += a.substring(d, d + b) + \"\\n\";\n" +
            "            d += b;\n" +
            "        }\n" +
            "        return c + a.substring(d, a.length);\n" +
            "    }\n" +
            "    function bm(a, b) {\n" +
            "        return new d(a, b);\n" +
            "    }\n" +
            "    function bl() {\n" +
            "    }\n" +
            "    function bk(a) {\n" +
            "        var b;\n" +
            "        for (b = 0; b < a.length; ++b)\n" +
            "            a[b] = bj();\n" +
            "    }\n" +
            "    function bj() {\n" +
            "        if (bc == null) {\n" +
            "            bg();\n" +
            "            bc = ba();\n" +
            "            bc.init(bd);\n" +
            "            for (be = 0; be < bd.length; ++be)\n" +
            "                bd[be] = 0;\n" +
            "            be = 0;\n" +
            "        }\n" +
            "        return bc.next();\n" +
            "    }\n" +
            "    function bg() {\n" +
            "        bf((new Date).getTime());\n" +
            "    }\n" +
            "    function bf(a) {\n" +
            "        bd[be++] ^= a & 255;\n" +
            "        bd[be++] ^= a >> 8 & 255;\n" +
            "        bd[be++] ^= a >> 16 & 255;\n" +
            "        bd[be++] ^= a >> 24 & 255;\n" +
            "        be >= bb && (be -= bb);\n" +
            "    }\n" +
            "    function ba() {\n" +
            "        return new Z;\n" +
            "    }\n" +
            "    function _() {\n" +
            "        var a;\n" +
            "        this.i = this.i + 1 & 255;\n" +
            "        this.j = this.j + this.S[this.i] & 255;\n" +
            "        a = this.S[this.i];\n" +
            "        this.S[this.i] = this.S[this.j];\n" +
            "        this.S[this.j] = a;\n" +
            "        return this.S[a + this.S[this.i] & 255];\n" +
            "    }\n" +
            "    function $(a) {\n" +
            "        var b, c, d;\n" +
            "        for (b = 0; b < 256; ++b)\n" +
            "            this.S[b] = b;\n" +
            "        c = 0;\n" +
            "        for (b = 0; b < 256; ++b) {\n" +
            "            c = c + this.S[b] + a[b % a.length] & 255;\n" +
            "            d = this.S[b];\n" +
            "            this.S[b] = this.S[c];\n" +
            "            this.S[c] = d;\n" +
            "        }\n" +
            "        this.i = 0;\n" +
            "        this.j = 0;\n" +
            "    }\n" +
            "    function Z() {\n" +
            "        this.i = 0;\n" +
            "        this.j = 0;\n" +
            "        this.S = [];\n" +
            "    }\n" +
            "    function Y(a, b) {\n" +
            "        var c;\n" +
            "        a < 256 || b.isEven() ? c = new J(b) : c = new Q(b);\n" +
            "        return this.exp(a, c);\n" +
            "    }\n" +
            "    function X(a, b) {\n" +
            "        if (a > 4294967295 || a < 1)\n" +
            "            return d.ONE;\n" +
            "        var c = e(), f = e(), g = b.convert(this), h = y(a) - 1;\n" +
            "        g.copyTo(c);\n" +
            "        while (--h >= 0) {\n" +
            "            b.sqrTo(c, f);\n" +
            "            if ((a & 1 << h) > 0)\n" +
            "                b.mulTo(f, g, c);\n" +
            "            else {\n" +
            "                var i = c;\n" +
            "                c = f;\n" +
            "                f = i;\n" +
            "            }\n" +
            "        }\n" +
            "        return b.revert(c);\n" +
            "    }\n" +
            "    function W() {\n" +
            "        return (this.t > 0 ? this[0] & 1 : this.s) == 0;\n" +
            "    }\n" +
            "    function V(a, b, c) {\n" +
            "        a.multiplyTo(b, c);\n" +
            "        this.reduce(c);\n" +
            "    }\n" +
            "    function U(a, b) {\n" +
            "        a.squareTo(b);\n" +
            "        this.reduce(b);\n" +
            "    }\n" +
            "    function T(a) {\n" +
            "        while (a.t <= this.mt2)\n" +
            "            a[a.t++] = 0;\n" +
            "        for (var b = 0; b < this.m.t; ++b) {\n" +
            "            var c = a[b] & 32767, d = c * this.mpl + ((c * this.mph + (a[b] >> 15) * this.mpl & this.um) << 15) & a.DM;\n" +
            "            c = b + this.m.t;\n" +
            "            a[c] += this.m.am(0, d, a, b, 0, this.m.t);\n" +
            "            while (a[c] >= a.DV) {\n" +
            "                a[c] -= a.DV;\n" +
            "                a[++c]++;\n" +
            "            }\n" +
            "        }\n" +
            "        a.clamp();\n" +
            "        a.drShiftTo(this.m.t, a);\n" +
            "        a.compareTo(this.m) >= 0 && a.subTo(this.m, a);\n" +
            "    }\n" +
            "    function S(a) {\n" +
            "        var b = e();\n" +
            "        a.copyTo(b);\n" +
            "        this.reduce(b);\n" +
            "        return b;\n" +
            "    }\n" +
            "    function R(a) {\n" +
            "        var b = e();\n" +
            "        a.abs().dlShiftTo(this.m.t, b);\n" +
            "        b.divRemTo(this.m, null, b);\n" +
            "        a.s < 0 && b.compareTo(d.ZERO) > 0 && this.m.subTo(b, b);\n" +
            "        return b;\n" +
            "    }\n" +
            "    function Q(a) {\n" +
            "        this.m = a;\n" +
            "        this.mp = a.invDigit();\n" +
            "        this.mpl = this.mp & 32767;\n" +
            "        this.mph = this.mp >> 15;\n" +
            "        this.um = (1 << a.DB - 15) - 1;\n" +
            "        this.mt2 = 2 * a.t;\n" +
            "    }\n" +
            "    function P() {\n" +
            "        if (this.t < 1)\n" +
            "            return 0;\n" +
            "        var a = this[0];\n" +
            "        if ((a & 1) == 0)\n" +
            "            return 0;\n" +
            "        var b = a & 3;\n" +
            "        b = b * (2 - (a & 15) * b) & 15;\n" +
            "        b = b * (2 - (a & 255) * b) & 255;\n" +
            "        b = b * (2 - ((a & 65535) * b & 65535)) & 65535;\n" +
            "        b = b * (2 - a * b % this.DV) % this.DV;\n" +
            "        return b > 0 ? this.DV - b : -b;\n" +
            "    }\n" +
            "    function O(a, b) {\n" +
            "        a.squareTo(b);\n" +
            "        this.reduce(b);\n" +
            "    }\n" +
            "    function N(a, b, c) {\n" +
            "        a.multiplyTo(b, c);\n" +
            "        this.reduce(c);\n" +
            "    }\n" +
            "    function M(a) {\n" +
            "        a.divRemTo(this.m, null, a);\n" +
            "    }\n" +
            "    function L(a) {\n" +
            "        return a;\n" +
            "    }\n" +
            "    function K(a) {\n" +
            "        return a.s < 0 || a.compareTo(this.m) >= 0 ? a.mod(this.m) : a;\n" +
            "    }\n" +
            "    function J(a) {\n" +
            "        this.m = a;\n" +
            "    }\n" +
            "    function I(a) {\n" +
            "        var b = e();\n" +
            "        this.abs().divRemTo(a, null, b);\n" +
            "        this.s < 0 && b.compareTo(d.ZERO) > 0 && a.subTo(b, b);\n" +
            "        return b;\n" +
            "    }\n" +
            "    function H(a, b, c) {\n" +
            "        var f = a.abs();\n" +
            "        if (!(f.t <= 0)) {\n" +
            "            var g = this.abs();\n" +
            "            if (g.t < f.t) {\n" +
            "                b != null && b.fromInt(0);\n" +
            "                c != null && this.copyTo(c);\n" +
            "                return;\n" +
            "            }\n" +
            "            c == null && (c = e());\n" +
            "            var h = e(), i = this.s, j = a.s, k = this.DB - y(f[f.t - 1]);\n" +
            "            if (k > 0) {\n" +
            "                f.lShiftTo(k, h);\n" +
            "                g.lShiftTo(k, c);\n" +
            "            } else {\n" +
            "                f.copyTo(h);\n" +
            "                g.copyTo(c);\n" +
            "            }\n" +
            "            var l = h.t, m = h[l - 1];\n" +
            "            if (m == 0)\n" +
            "                return;\n" +
            "            var n = m * (1 << this.F1) + (l > 1 ? h[l - 2] >> this.F2 : 0), o = this.FV / n, p = (1 << this.F1) / n, q = 1 << this.F2, r = c.t, s = r - l, t = b == null ? e() : b;\n" +
            "            h.dlShiftTo(s, t);\n" +
            "            if (c.compareTo(t) >= 0) {\n" +
            "                c[c.t++] = 1;\n" +
            "                c.subTo(t, c);\n" +
            "            }\n" +
            "            d.ONE.dlShiftTo(l, t);\n" +
            "            t.subTo(h, h);\n" +
            "            while (h.t < l)\n" +
            "                h[h.t++] = 0;\n" +
            "            while (--s >= 0) {\n" +
            "                var u = c[--r] == m ? this.DM : Math.floor(c[r] * o + (c[r - 1] + q) * p);\n" +
            "                if ((c[r] += h.am(0, u, c, s, 0, l)) < u) {\n" +
            "                    h.dlShiftTo(s, t);\n" +
            "                    c.subTo(t, c);\n" +
            "                    while (c[r] < --u)\n" +
            "                        c.subTo(t, c);\n" +
            "                }\n" +
            "            }\n" +
            "            if (b != null) {\n" +
            "                c.drShiftTo(l, b);\n" +
            "                i != j && d.ZERO.subTo(b, b);\n" +
            "            }\n" +
            "            c.t = l;\n" +
            "            c.clamp();\n" +
            "            k > 0 && c.rShiftTo(k, c);\n" +
            "            i < 0 && d.ZERO.subTo(c, c);\n" +
            "        }\n" +
            "    }\n" +
            "    function G(a) {\n" +
            "        var b = this.abs(), c = a.t = 2 * b.t;\n" +
            "        while (--c >= 0)\n" +
            "            a[c] = 0;\n" +
            "        for (c = 0; c < b.t - 1; ++c) {\n" +
            "            var d = b.am(c, b[c], a, 2 * c, 0, 1);\n" +
            "            if ((a[c + b.t] += b.am(c + 1, 2 * b[c], a, 2 * c + 1, d, b.t - c - 1)) >= b.DV) {\n" +
            "                a[c + b.t] -= b.DV;\n" +
            "                a[c + b.t + 1] = 1;\n" +
            "            }\n" +
            "        }\n" +
            "        a.t > 0 && (a[a.t - 1] += b.am(c, b[c], a, 2 * c, 0, 1));\n" +
            "        a.s = 0;\n" +
            "        a.clamp();\n" +
            "    }\n" +
            "    function F(a, b) {\n" +
            "        var c = this.abs(), e = a.abs(), f = c.t;\n" +
            "        b.t = f + e.t;\n" +
            "        while (--f >= 0)\n" +
            "            b[f] = 0;\n" +
            "        for (f = 0; f < e.t; ++f)\n" +
            "            b[f + c.t] = c.am(0, e[f], b, f, 0, c.t);\n" +
            "        b.s = 0;\n" +
            "        b.clamp();\n" +
            "        this.s != a.s && d.ZERO.subTo(b, b);\n" +
            "    }\n" +
            "    function E(a, b) {\n" +
            "        var c = 0, d = 0, e = Math.min(a.t, this.t);\n" +
            "        while (c < e) {\n" +
            "            d += this[c] - a[c];\n" +
            "            b[c++] = d & this.DM;\n" +
            "            d >>= this.DB;\n" +
            "        }\n" +
            "        if (a.t < this.t) {\n" +
            "            d -= a.s;\n" +
            "            while (c < this.t) {\n" +
            "                d += this[c];\n" +
            "                b[c++] = d & this.DM;\n" +
            "                d >>= this.DB;\n" +
            "            }\n" +
            "            d += this.s;\n" +
            "        } else {\n" +
            "            d += this.s;\n" +
            "            while (c < a.t) {\n" +
            "                d -= a[c];\n" +
            "                b[c++] = d & this.DM;\n" +
            "                d >>= this.DB;\n" +
            "            }\n" +
            "            d -= a.s;\n" +
            "        }\n" +
            "        b.s = d < 0 ? -1 : 0;\n" +
            "        d < -1 ? b[c++] = this.DV + d : d > 0 && (b[c++] = d);\n" +
            "        b.t = c;\n" +
            "        b.clamp();\n" +
            "    }\n" +
            "    function D(a, b) {\n" +
            "        b.s = this.s;\n" +
            "        var c = Math.floor(a / this.DB);\n" +
            "        if (c >= this.t)\n" +
            "            b.t = 0;\n" +
            "        else {\n" +
            "            var d = a % this.DB, e = this.DB - d, f = (1 << d) - 1;\n" +
            "            b[0] = this[c] >> d;\n" +
            "            for (var g = c + 1; g < this.t; ++g) {\n" +
            "                b[g - c - 1] |= (this[g] & f) << e;\n" +
            "                b[g - c] = this[g] >> d;\n" +
            "            }\n" +
            "            d > 0 && (b[this.t - c - 1] |= (this.s & f) << e);\n" +
            "            b.t = this.t - c;\n" +
            "            b.clamp();\n" +
            "        }\n" +
            "    }\n" +
            "    function C(a, b) {\n" +
            "        var c = a % this.DB, d = this.DB - c, e = (1 << d) - 1, f = Math.floor(a / this.DB), g = this.s << c & this.DM, h;\n" +
            "        for (h = this.t - 1; h >= 0; --h) {\n" +
            "            b[h + f + 1] = this[h] >> d | g;\n" +
            "            g = (this[h] & e) << c;\n" +
            "        }\n" +
            "        for (h = f - 1; h >= 0; --h)\n" +
            "            b[h] = 0;\n" +
            "        b[f] = g;\n" +
            "        b.t = this.t + f + 1;\n" +
            "        b.s = this.s;\n" +
            "        b.clamp();\n" +
            "    }\n" +
            "    function B(a, b) {\n" +
            "        for (var c = a; c < this.t; ++c)\n" +
            "            b[c - a] = this[c];\n" +
            "        b.t = Math.max(this.t - a, 0);\n" +
            "        b.s = this.s;\n" +
            "    }\n" +
            "    function A(a, b) {\n" +
            "        var c;\n" +
            "        for (c = this.t - 1; c >= 0; --c)\n" +
            "            b[c + a] = this[c];\n" +
            "        for (c = a - 1; c >= 0; --c)\n" +
            "            b[c] = 0;\n" +
            "        b.t = this.t + a;\n" +
            "        b.s = this.s;\n" +
            "    }\n" +
            "    function z() {\n" +
            "        return this.t <= 0 ? 0 : this.DB * (this.t - 1) + y(this[this.t - 1] ^ this.s & this.DM);\n" +
            "    }\n" +
            "    function y(a) {\n" +
            "        var b = 1, c;\n" +
            "        if ((c = a >>> 16) != 0) {\n" +
            "            a = c;\n" +
            "            b += 16;\n" +
            "        }\n" +
            "        if ((c = a >> 8) != 0) {\n" +
            "            a = c;\n" +
            "            b += 8;\n" +
            "        }\n" +
            "        if ((c = a >> 4) != 0) {\n" +
            "            a = c;\n" +
            "            b += 4;\n" +
            "        }\n" +
            "        if ((c = a >> 2) != 0) {\n" +
            "            a = c;\n" +
            "            b += 2;\n" +
            "        }\n" +
            "        if ((c = a >> 1) != 0) {\n" +
            "            a = c;\n" +
            "            b += 1;\n" +
            "        }\n" +
            "        return b;\n" +
            "    }\n" +
            "    function x(a) {\n" +
            "        var b = this.s - a.s;\n" +
            "        if (b != 0)\n" +
            "            return b;\n" +
            "        var c = this.t;\n" +
            "        b = c - a.t;\n" +
            "        if (b != 0)\n" +
            "            return b;\n" +
            "        while (--c >= 0)\n" +
            "            if ((b = this[c] - a[c]) != 0)\n" +
            "                return b;\n" +
            "        return 0;\n" +
            "    }\n" +
            "    function w() {\n" +
            "        return this.s < 0 ? this.negate() : this;\n" +
            "    }\n" +
            "    function v() {\n" +
            "        var a = e();\n" +
            "        d.ZERO.subTo(this, a);\n" +
            "        return a;\n" +
            "    }\n" +
            "    function u(a) {\n" +
            "        if (this.s < 0)\n" +
            "            return \"-\" + this.negate().toString(a);\n" +
            "        var b;\n" +
            "        if (a == 16)\n" +
            "            b = 4;\n" +
            "        else if (a == 8)\n" +
            "            b = 3;\n" +
            "        else if (a == 2)\n" +
            "            b = 1;\n" +
            "        else if (a == 32)\n" +
            "            b = 5;\n" +
            "        else if (a == 4)\n" +
            "            b = 2;\n" +
            "        else\n" +
            "            return this.toRadix(a);\n" +
            "        var c = (1 << b) - 1, d, e = !1, f = \"\", g = this.t, h = this.DB - g * this.DB % b;\n" +
            "        if (g-- > 0) {\n" +
            "            if (h < this.DB && (d = this[g] >> h) > 0) {\n" +
            "                e = !0;\n" +
            "                f = n(d);\n" +
            "            }\n" +
            "            while (g >= 0) {\n" +
            "                if (h < b) {\n" +
            "                    d = (this[g] & (1 << h) - 1) << b - h;\n" +
            "                    d |= this[--g] >> (h += this.DB - b);\n" +
            "                } else {\n" +
            "                    d = this[g] >> (h -= b) & c;\n" +
            "                    if (h <= 0) {\n" +
            "                        h += this.DB;\n" +
            "                        --g;\n" +
            "                    }\n" +
            "                }\n" +
            "                d > 0 && (e = !0);\n" +
            "                e && (f += n(d));\n" +
            "            }\n" +
            "        }\n" +
            "        return e ? f : \"0\";\n" +
            "    }\n" +
            "    function t() {\n" +
            "        var a = this.s & this.DM;\n" +
            "        while (this.t > 0 && this[this.t - 1] == a)\n" +
            "            --this.t;\n" +
            "    }\n" +
            "    function s(a, b) {\n" +
            "        var c;\n" +
            "        if (b == 16)\n" +
            "            c = 4;\n" +
            "        else if (b == 8)\n" +
            "            c = 3;\n" +
            "        else if (b == 256)\n" +
            "            c = 8;\n" +
            "        else if (b == 2)\n" +
            "            c = 1;\n" +
            "        else if (b == 32)\n" +
            "            c = 5;\n" +
            "        else if (b == 4)\n" +
            "            c = 2;\n" +
            "        else {\n" +
            "            this.fromRadix(a, b);\n" +
            "            return;\n" +
            "        }\n" +
            "        this.t = 0;\n" +
            "        this.s = 0;\n" +
            "        var e = a.length, f = !1, g = 0;\n" +
            "        while (--e >= 0) {\n" +
            "            var h = c == 8 ? a[e] & 255 : o(a, e);\n" +
            "            if (h < 0) {\n" +
            "                a.charAt(e) == \"-\" && (f = !0);\n" +
            "                continue;\n" +
            "            }\n" +
            "            f = !1;\n" +
            "            if (g == 0)\n" +
            "                this[this.t++] = h;\n" +
            "            else if (g + c > this.DB) {\n" +
            "                this[this.t - 1] |= (h & (1 << this.DB - g) - 1) << g;\n" +
            "                this[this.t++] = h >> this.DB - g;\n" +
            "            } else\n" +
            "                this[this.t - 1] |= h << g;\n" +
            "            g += c;\n" +
            "            g >= this.DB && (g -= this.DB);\n" +
            "        }\n" +
            "        if (c == 8 && (a[0] & 128) != 0) {\n" +
            "            this.s = -1;\n" +
            "            g > 0 && (this[this.t - 1] |= (1 << this.DB - g) - 1 << g);\n" +
            "        }\n" +
            "        this.clamp();\n" +
            "        f && d.ZERO.subTo(this, this);\n" +
            "    }\n" +
            "    function r(a) {\n" +
            "        var b = e();\n" +
            "        b.fromInt(a);\n" +
            "        return b;\n" +
            "    }\n" +
            "    function q(a) {\n" +
            "        this.t = 1;\n" +
            "        this.s = a < 0 ? -1 : 0;\n" +
            "        a > 0 ? this[0] = a : a < -1 ? this[0] = a + DV : this.t = 0;\n" +
            "    }\n" +
            "    function p(a) {\n" +
            "        for (var b = this.t - 1; b >= 0; --b)\n" +
            "            a[b] = this[b];\n" +
            "        a.t = this.t;\n" +
            "        a.s = this.s;\n" +
            "    }\n" +
            "    function o(a, b) {\n" +
            "        var c = k[a.charCodeAt(b)];\n" +
            "        return c == null ? -1 : c;\n" +
            "    }\n" +
            "    function n(a) {\n" +
            "        return j.charAt(a);\n" +
            "    }\n" +
            "    function h(a, b, c, d, e, f) {\n" +
            "        var g = b & 16383, h = b >> 14;\n" +
            "        while (--f >= 0) {\n" +
            "            var i = this[a] & 16383, j = this[a++] >> 14, k = h * i + j * g;\n" +
            "            i = g * i + ((k & 16383) << 14) + c[d] + e;\n" +
            "            e = (i >> 28) + (k >> 14) + h * j;\n" +
            "            c[d++] = i & 268435455;\n" +
            "        }\n" +
            "        return e;\n" +
            "    }\n" +
            "    function g(a, b, c, d, e, f) {\n" +
            "        var g = b & 32767, h = b >> 15;\n" +
            "        while (--f >= 0) {\n" +
            "            var i = this[a] & 32767, j = this[a++] >> 15, k = h * i + j * g;\n" +
            "            i = g * i + ((k & 32767) << 15) + c[d] + (e & 1073741823);\n" +
            "            e = (i >>> 30) + (k >>> 15) + h * j + (e >>> 30);\n" +
            "            c[d++] = i & 1073741823;\n" +
            "        }\n" +
            "        return e;\n" +
            "    }\n" +
            "    function f(a, b, c, d, e, f) {\n" +
            "        while (--f >= 0) {\n" +
            "            var g = b * this[a++] + c[d] + e;\n" +
            "            e = Math.floor(g / 67108864);\n" +
            "            c[d++] = g & 67108863;\n" +
            "        }\n" +
            "        return e;\n" +
            "    }\n" +
            "    function e() {\n" +
            "        return new d(null);\n" +
            "    }\n" +
            "    function d(a, b, c) {\n" +
            "        a != null && (\"number\" == typeof a ? this.fromNumber(a, b, c) : b == null && \"string\" != typeof a ? this.fromString(a, 256) : this.fromString(a, b));\n" +
            "    }\n" +
            "    var a, b = 0xdeadbeefcafe, c = (b & 16777215) == 15715070;\n" +
            "    if (c && navigator.appName == \"Microsoft Internet Explorer\") {\n" +
            "        d.prototype.am = g;\n" +
            "        a = 30;\n" +
            "    } else if (c && navigator.appName != \"Netscape\") {\n" +
            "        d.prototype.am = f;\n" +
            "        a = 26;\n" +
            "    } else {\n" +
            "        d.prototype.am = h;\n" +
            "        a = 28;\n" +
            "    }\n" +
            "    d.prototype.DB = a;\n" +
            "    d.prototype.DM = (1 << a) - 1;\n" +
            "    d.prototype.DV = 1 << a;\n" +
            "    var i = 52;\n" +
            "    d.prototype.FV = Math.pow(2, i);\n" +
            "    d.prototype.F1 = i - a;\n" +
            "    d.prototype.F2 = 2 * a - i;\n" +
            "    var j = \"0123456789abcdefghijklmnopqrstuvwxyz\", k = [], l, m;\n" +
            "    l = \"0\".charCodeAt(0);\n" +
            "    for (m = 0; m <= 9; ++m)\n" +
            "        k[l++] = m;\n" +
            "    l = \"a\".charCodeAt(0);\n" +
            "    for (m = 10; m < 36; ++m)\n" +
            "        k[l++] = m;\n" +
            "    l = \"A\".charCodeAt(0);\n" +
            "    for (m = 10; m < 36; ++m)\n" +
            "        k[l++] = m;\n" +
            "    J.prototype.convert = K;\n" +
            "    J.prototype.revert = L;\n" +
            "    J.prototype.reduce = M;\n" +
            "    J.prototype.mulTo = N;\n" +
            "    J.prototype.sqrTo = O;\n" +
            "    Q.prototype.convert = R;\n" +
            "    Q.prototype.revert = S;\n" +
            "    Q.prototype.reduce = T;\n" +
            "    Q.prototype.mulTo = V;\n" +
            "    Q.prototype.sqrTo = U;\n" +
            "    d.prototype.copyTo = p;\n" +
            "    d.prototype.fromInt = q;\n" +
            "    d.prototype.fromString = s;\n" +
            "    d.prototype.clamp = t;\n" +
            "    d.prototype.dlShiftTo = A;\n" +
            "    d.prototype.drShiftTo = B;\n" +
            "    d.prototype.lShiftTo = C;\n" +
            "    d.prototype.rShiftTo = D;\n" +
            "    d.prototype.subTo = E;\n" +
            "    d.prototype.multiplyTo = F;\n" +
            "    d.prototype.squareTo = G;\n" +
            "    d.prototype.divRemTo = H;\n" +
            "    d.prototype.invDigit = P;\n" +
            "    d.prototype.isEven = W;\n" +
            "    d.prototype.exp = X;\n" +
            "    d.prototype.toString = u;\n" +
            "    d.prototype.negate = v;\n" +
            "    d.prototype.abs = w;\n" +
            "    d.prototype.compareTo = x;\n" +
            "    d.prototype.bitLength = z;\n" +
            "    d.prototype.mod = I;\n" +
            "    d.prototype.modPowInt = Y;\n" +
            "    d.ZERO = r(0);\n" +
            "    d.ONE = r(1);\n" +
            "    Z.prototype.init = $;\n" +
            "    Z.prototype.next = _;\n" +
            "    var bb = 256, bc, bd, be;\n" +
            "    if (bd == null) {\n" +
            "        bd = [];\n" +
            "        be = 0;\n" +
            "        var bh;\n" +
            "        if (navigator.appName == \"Netscape\" && navigator.appVersion < \"5\" && window.crypto && typeof window.crypto.random == \"function\") {\n" +
            "            var bi = window.crypto.random(32);\n" +
            "            for (bh = 0; bh < bi.length; ++bh)\n" +
            "                bd[be++] = bi.charCodeAt(bh) & 255;\n" +
            "        }\n" +
            "        while (be < bb) {\n" +
            "            bh = Math.floor(65536 * Math.random());\n" +
            "            bd[be++] = bh >>> 8;\n" +
            "            bd[be++] = bh & 255;\n" +
            "        }\n" +
            "        be = 0;\n" +
            "        bg();\n" +
            "    }\n" +
            "    bl.prototype.nextBytes = bk;\n" +
            "    bq.prototype.doPublic = bs;\n" +
            "    bq.prototype.setPublic = br;\n" +
            "    bq.prototype.encrypt = bt;\n" +
            "    this.RSAKey = bq;\n" +
            "}).call(sinaSSOEncoder);\n" +
            "\n" +
            "function getPW(pass,servertime,nonce,pubkey){\n" +
            "\t\n" +
            "\tvar e = new sinaSSOEncoder.RSAKey;\n" +
            "        e.setPublic(pubkey, \"10001\");\n" +
            "\tvar PW=e.encrypt([servertime, nonce].join(\"\\t\") + \"\\n\" +pass);\n" +
            "\treturn PW;\n" +
            "\t\n" +
            "\t\n" +
            "}";


}
