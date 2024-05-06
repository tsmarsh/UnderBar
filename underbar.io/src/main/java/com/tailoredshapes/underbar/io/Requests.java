package com.tailoredshapes.underbar.io;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.tailoredshapes.stash.Stash;
import com.tailoredshapes.underbar.ocho.function.ExceptionalFunctions;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.ocho.Die.die;
import static com.tailoredshapes.underbar.ocho.Die.rethrow;
import static com.tailoredshapes.underbar.io.IO.slurp;

public interface Requests {

  static <R> CompletableFuture<R> get(Stash options, Function<String, R> then){
    return requestNoBody(options, "GET", then, (error) -> die(error, "Failed to requestNoBody"));
  }

  static <R> CompletableFuture<R> get(String url, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return get(options, then);
  }

  static <R> CompletableFuture<R> post(Stash options, String body, Function<String, R> then){
    return requestWithBody(options, body, "POST", then, (error) -> die(error, "Failed to requestNoBody"));
  }


  static <R> CompletableFuture<R> post(String url, String body, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return post(options, body, then);
  }

  static <R> CompletableFuture<R> put(Stash options, String body, Function<String, R> then){
    return requestWithBody(options, body, "PUT", then, (error) -> die(error, "Failed to requestNoBody"));
  }


  static <R> CompletableFuture<R> put(String url, String body, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return put(options, body, then);
  }

  static <R> CompletableFuture<R> patch(Stash options, String body, Function<String, R> then){
    return requestWithBody(options, body, "PATCH", then, (error) -> die(error, "Failed to requestNoBody"));
  }


  static <R> CompletableFuture<R> patch(String url, String body, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return patch(options, body, then);
  }


  static <R> CompletableFuture<R> delete(Stash options, Function<String, R> then){
    return requestNoBody(options, "DELETE", then, (error) -> die(error, "Failed to requestNoBody"));
  }

  static <R> CompletableFuture<R> delete(String url, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return delete(options, then);
  }

  static <R> CompletableFuture<R> head(Stash options, Function<String, R> then){
    return requestNoBody(options, "HEAD", then, (error) -> die(error, "Failed to requestNoBody"));
  }

  static <R> CompletableFuture<R> head(String url, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
            "readTimeout", 100000,
            "url", url);
    return head(options, then);
  }


  static <R> CompletableFuture<R> requestWithBody(Stash options, String body, String method, Function<String, R> then, Function<Exception, R> error){
    return CompletableFuture.supplyAsync(() -> {
      URL request_url = rethrow(() -> new URL(options.grab("url")), () -> "Not a valid url");

      HttpURLConnection http_conn = (HttpURLConnection) rethrow((ExceptionalFunctions.SupplierWithOops<URLConnection>) request_url::openConnection);

      http_conn.setDoOutput(true);
      if(method.equals("PATCH")){
        rethrow(() -> http_conn.setRequestMethod("POST"));
        http_conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
      } else {
        rethrow(()-> http_conn.setRequestMethod(method));
      }

      try(OutputStream outputStream = rethrow(http_conn::getOutputStream)) {
        rethrow(() -> outputStream.write(body.getBytes()));
        rethrow(outputStream::close);

        http_conn.setConnectTimeout(options.grab("connectionTimeout"));
        http_conn.setReadTimeout(options.grab("readTimeout"));

        return then.apply(slurp(rethrow(http_conn::getInputStream)));
      }catch(Exception e){
        return error.apply(e);
      }
    });
  }

  static <R> CompletableFuture<R> requestNoBody(Stash options, String method,  Function<String, R> then, Function<Exception, R> error){
    return CompletableFuture.supplyAsync(() -> {
      URL request_url = rethrow(() -> new URL(options.grab("url")), () -> "Not a valid url");

      HttpURLConnection http_conn = (HttpURLConnection) rethrow((ExceptionalFunctions.SupplierWithOops<URLConnection>) request_url::openConnection);

      rethrow(() -> http_conn.setRequestMethod(method));
      http_conn.setConnectTimeout(options.grab("connectionTimeout"));
      http_conn.setReadTimeout(options.grab("readTimeout"));


      try{
        String body = null;
        if(method.equals("GET")) {
          body = slurp(rethrow(http_conn::getInputStream));
        }
        return then.apply(body);
      }catch(Exception e){
        return error.apply(e);
      }
    });

  }

  static void head(String url) {
  }
}
