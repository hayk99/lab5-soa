package soa.eip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";

  @Override
  public void configure() {
    from(DIRECT_URI)
      .log("Body contains \"${body}\"")
      .log("Searching twitter for \"${body}\"!")
      .process(exchange -> {
        //NewBody without max
        String newBody = "";
        //get the body
        String body = exchange.getIn().getBody(String.class);
        //set default tweets size to 5
        int maxNum = 5;
        for (String splited : body.split(" ")){
          if (splited.matches("max:[0-9]+")){
            maxNum = Integer.parseInt(splited.split(":")[1]);
          }
          else{
            newBody += splited + " ";
          }
        }
        exchange.getIn().setBody(newBody);
        exchange.getIn().setHeader("count", maxNum);
      })
      .toD("twitter-search:${body}?count=${header.count}")
      .log("Body now contains the response from twitter:\n${body}");
  }
}
