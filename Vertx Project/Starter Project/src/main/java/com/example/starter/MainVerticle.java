package com.example.starter;

import com.sun.org.slf4j.internal.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
public class MainVerticle {
  private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  private static int numberOfConnections = 0;
  private static int noOfClient = 0;
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createNetServer()
      .connectHandler(MainVerticle::handleNewClient)
      .listen(3000);
//    vertx.setPeriodic(5000, id -> System.out.println(howMany()));
    vertx.createHttpServer()
      .requestHandler(request -> request.response().end(howMany(noOfClient++)))
      .listen(8080);
  }
  private static void handleNewClient(NetSocket socket) {
    numberOfConnections++;
    noOfClient++;
    for(int i =0 ; i< 10000000; i++) {
      i = i + 1;
      System.out.println("inside loop " + i + " and server is " + noOfClient);
    }
    socket.handler(buffer -> {
      socket.write(buffer);
      if (buffer.toString().endsWith("/quit\n")) {
        socket.close();
      }
    });
    socket.closeHandler(v -> numberOfConnections = numberOfConnections);
  }
  private static String howMany(int no) {
    System.out.println("We now have " + numberOfConnections + " connections");
    return "We now have " + numberOfConnections + " connections";
  }
}

//public class MainVerticle extends AbstractVerticle {
//
//  @Override
//  public void start(Promise<Void> startPromise) throws Exception {
//    vertx.createHttpServer().requestHandler(req -> {
//      req.response()
//        .putHeader("content-type", "text/plain")
//        .end("Hello from Vert.x!");
//    }).listen(8888, http -> {
//      if (http.succeeded()) {
//        startPromise.complete();
//        System.out.println("HTTP server started on port 8888");
//      } else {
//        startPromise.fail(http.cause());
//      }
//    });
//  }
//}
