package com.tailoredshapes.underbar.io;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static com.tailoredshapes.underbar.ocho.Die.die;
import static com.tailoredshapes.underbar.io.IO.slurp;
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
        case "HEAD":
          break;
        case "DELETE":
          response = "deleted".getBytes();
          break;
        case "POST":
          response = String.format("%s:POST", (slurp(exchange.getRequestBody()))).getBytes();
          break;
        case "PUT":
          response = String.format("%s:PUT", (slurp(exchange.getRequestBody()))).getBytes();
          break;
        case "PATCH":
          response = String.format("%s:PATCH", (slurp(exchange.getRequestBody()))).getBytes();
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
    }).join();
  }

  @Test
  public void shouldDeleteAValueFromAServer() throws Exception {
    Requests.delete(url, (body) -> {
      assertNull(body);
      return "OK";
    }).join();
  }

  @Test
  public void shouldCheckTheHeadOnAServer() throws Exception {
    Requests.head(url, (body) -> {
      assertNull(body);
      return "OK";
    }).join();
  }


  @Test
  public void shouldPatchTheHeadOnAServer() throws Exception {
    String body = "PATCH Test";
    Requests.patch(url,body,  (b) -> {
      assertEquals( "PATCH Test:POST", b); //because Java doesnt' believe in POST
      return "OK";
    }).join();
  }



  @Test
  public void shouldPostAValueFromAServer() throws Exception {
    String body = "POST Test";
    Requests.post(url, body, b -> {
      assertEquals( "POST Test:POST", b);
      return "OK";
    }).join();
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