package com.tailoredshapes.underbar;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class RequestsTest {

  private HttpServer httpServer;

  @Before
  public void setUp() throws Exception {
    httpServer = HttpServer.create(new InetSocketAddress(6464), 0);
    httpServer.createContext("/", exchange -> {
      byte[] response = "{\"success\": true}".getBytes();
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
    Requests.get("http://localhost:6464", (body) -> {
      assertEquals( "{\"success\": true}", body);
      return "OK";
    });
  }
}