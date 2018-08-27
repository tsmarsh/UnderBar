package com.tailoredshapes.underbar;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static com.tailoredshapes.underbar.Die.die;
import static com.tailoredshapes.underbar.IO.slurp;
import static org.junit.Assert.*;

public class RequestsTest {

  private HttpServer httpServer;
  int port = 6464;
  private String url = "http://localhost:" + port;

  @Before
  public void setUp() throws Exception {
    httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    httpServer.createContext("/", exchange -> {
      byte[] response = null;

      switch(exchange.getRequestMethod()){
        case "GET":
          response = "{\"success\": true}".getBytes();
          break;
        case "POST":
        case "PUT":
          response = slurp(exchange.getRequestBody()).getBytes();
          break;
        default:
          die("Unsupported HTTP Method");
      }

      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
      exchange.getResponseBody().write(response);
      exchange.close();
    });
    httpServer.start();
  }

  @After
  public void tearDown() throws Exception {
    httpServer.stop(0);
  }

  @Test
  public void shouldGetAValueFromAServer() throws Exception {
    Requests.get(url, (body) -> {
      assertEquals( "{\"success\": true}", body);
      return "OK";
    });
  }

  @Test
  public void shouldPostAValueFromAServer() throws Exception {
    String body = "POST Test";
    Requests.post(url, body, b -> {
      assertEquals( body, b);
      return "OK";
    });
  }

  @Test
  public void shouldPutAValueOnAServer() throws Exception {
    String body = "PUT Test";
    Requests.put(url, body, b -> {
      assertEquals( body, b);
      return "OK";
    });
  }
}