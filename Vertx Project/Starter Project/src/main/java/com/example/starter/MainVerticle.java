package com.example.starter;

import io.vertx.core.*;
import io.vertx.core.json.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import io.vertx.core.net.NetSocket;
public class MainVerticle extends AbstractVerticle {
  private final Logger logger = LogManager.getLogger(MainVerticle.class);
  private static int numberOfConnections = 0;
  private long counter = 1;

  @Override
  public void start(Promise<Void> promise) throws Exception {
    vertx.setPeriodic(5000, count -> {
      try {
        logger.info("Zzz...");
        Thread.sleep(8000);
        logger.info("Up!");
      } catch(InterruptedException e) {
        logger.error("Oops",e);
      }
//      logger.info("tick");
//      logger.info("n = {}", config().getInteger("n", -1));
    });
    vertx.createHttpServer()
            .requestHandler(req -> {
              logger.info("Request #{} from {}", counter++,
                      req.remoteAddress().host());
              req.response().end("Hello!");
            })
            .listen(8080, arr -> {
              if(arr.succeeded()) {
                promise.complete();
              } else {
                promise.fail(arr.cause());
              }
            });
    logger.info("Open http://localhost:8080/");
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DeploymentOptions opts = new DeploymentOptions()
//      .setConfig(conf)
      .setWorker(true)
      .setInstances(2);
    vertx.deployVerticle("com.example.starter.MainVerticle",opts);
//    for(int n=0;n<4;n++) {
//      JsonObject conf = new JsonObject().put("n",n);
//      DeploymentOptions opts = new DeploymentOptions()
//        .setConfig(conf)
//        .setInstances(n);
//      vertx.deployVerticle("com.example.starter.MainVerticle",opts);
//    }
//    vertx.deployVerticle(new MainVerticle());
  }

  private static void handleNewClient(NetSocket socket) {
    numberOfConnections++;
    for(int i =0 ; i< 10000000; i++) {
      i = i + 1;
//      System.out.println("inside loop " + i + " and server is " + noOfClient);
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
