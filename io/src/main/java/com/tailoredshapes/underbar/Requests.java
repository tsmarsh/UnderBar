package com.tailoredshapes.underbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

import com.tailoredshapes.stash.Stash;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.IO.slurp;

public interface Requests {

  static <R> R get(Stash options, Function<String, R> then){
    URL request_url = rethrow(() -> new URL(options.get("url")), () -> "Not a valid url");

    HttpURLConnection http_conn = rethrow(() -> (HttpURLConnection) request_url.openConnection());
    http_conn.setConnectTimeout(options.get("connectionTimeout"));
    http_conn.setReadTimeout(options.get("readTimeout"));
    http_conn.setInstanceFollowRedirects(true);
    String body = slurp(rethrow(http_conn::getInputStream));

    return then.apply(body);
  }

  static <R> R get(String url, Function<String, R> then){
    Stash options = stash("connectionTimeout", 100000,
                        "readTimeout", 100000,
                          "url", url);
    return get(options, then);
  }

}
