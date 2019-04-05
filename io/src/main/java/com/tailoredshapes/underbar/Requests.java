package com.tailoredshapes.underbar;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;

import com.tailoredshapes.stash.Stash;
import com.tailoredshapes.underbar.function.ExceptionalFunctions;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.Die.die;
import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.IO.slurp;

public interface Requests {

  static <R> R get(Stash options, Function<String, R> then){
    return requestNoBody(options, then, (error) -> die(error, "Failed to requestNoBody"));
  }

  static <R> R get(String url, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
                        "readTimeout", 100000,
                          "url", url);
    return get(options, then);
  }

  static <R> R requestNoBody(Stash options, Function<String, R> then, Function<Exception, R> error){
    URL request_url = rethrow(() -> new URL(options.get("url")), () -> "Not a valid url");

    HttpURLConnection http_conn = (HttpURLConnection) rethrow((ExceptionalFunctions.SupplierWithOops<URLConnection>) request_url::openConnection);

    http_conn.setConnectTimeout(options.get("connectionTimeout"));
    http_conn.setReadTimeout(options.get("readTimeout"));

    try {
      String body = slurp(rethrow(http_conn::getInputStream));
      return then.apply(body);
    }catch(Exception e){
      return error.apply(e);
    }
  }

  static <R> R post(Stash options, String body, Function<String, R> then){
    return requestWithBody(options, body, "POST", then, (error) -> die(error, "Failed to requestNoBody"));
  }


  static <R> R post(String url, String body, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return post(options, body, then);
  }

  static <R> R put(Stash options, String body, Function<String, R> then){
    return requestWithBody(options, body, "PUT", then, (error) -> die(error, "Failed to requestNoBody"));
  }


  static <R> R put(String url, String body, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return post(options, body, then);
  }

  static <R> R requestWithBody(Stash options, String body, String method, Function<String, R> then, Function<Exception, R> error){
    URL request_url = rethrow(() -> new URL(options.get("url")), () -> "Not a valid url");

    HttpURLConnection http_conn = (HttpURLConnection) rethrow((ExceptionalFunctions.SupplierWithOops<URLConnection>) request_url::openConnection);

    http_conn.setDoOutput(true);
    rethrow(()-> http_conn.setRequestMethod(method));

    OutputStream outputStream = rethrow(http_conn::getOutputStream);

    rethrow(() -> outputStream.write(body.getBytes()));
    rethrow(outputStream::close);

    http_conn.setConnectTimeout(options.get("connectionTimeout"));
    http_conn.setReadTimeout(options.get("readTimeout"));

    try {

      return then.apply(slurp(rethrow(http_conn::getInputStream)));
    }catch(Exception e){
      return error.apply(e);
    }
  }

}
