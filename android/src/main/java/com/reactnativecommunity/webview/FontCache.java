package com.reactnativecommunity.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FontCache {


  static private String url_prefix       = "http://oo63.palmmob.com/6.3.0-111/fonts/";
  static private String cache_url_prefix = "http://oo63-cache.palmmob.com/6.3.0-111/fonts/";

  static private String fontdir = "webviewfontcache";

  public static WebResourceResponse getCache(Context context, String url){
    if(url.indexOf(FontCache.url_prefix) < 0) {
      return null;
    }
    String cacheDir = context.getCacheDir() + "/" + FontCache.fontdir + "/";
    String fontname = url.replace(FontCache.url_prefix,"");
    String filepath = cacheDir + fontname;

    File filedir = new File(cacheDir);
    if(!filedir.exists()) {
      filedir.mkdir();
    }

    File fontfile = new File(filepath);
    if(!fontfile.exists()){
      FontCache.download(url, filepath);
    } else {
      Log.e("FontCache", "local cache:" + filepath);
    }

    try{
      WebResourceResponse response = new WebResourceResponse("application/octet-stream", "gzip", new FileInputStream(fontfile));
      return response;
    } catch (Exception e){
      Log.e("FontCache", e.getMessage());
    }

    return null;
  }

  static void download(String downloadUrl, String filepath) {
    try {
      downloadUrl = downloadUrl.replaceFirst(FontCache.url_prefix, FontCache.cache_url_prefix);
      URL url = new URL(downloadUrl);
      URLConnection conn = url.openConnection();
      InputStream is = conn.getInputStream();
      File file1 = new File(filepath);
      if (file1.exists()) {
        file1.delete();
      }
      byte[] bs = new byte[1024];
      int len;
      OutputStream os = new FileOutputStream(filepath);
      while ((len = is.read(bs)) != -1) {
        os.write(bs, 0, len);
      }
      os.close();
      is.close();
    } catch (Exception e) {
      Log.e("FontCache", e.getMessage());
    }
  }

}