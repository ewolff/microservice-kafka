package com.ewolff.microservice.invoicing;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class OrderRestServiceStub {

	@RequestMapping(value="/feed", method=RequestMethod.GET)
	public String feed() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
				"<feed xmlns=\"http://www.w3.org/2005/Atom\">" + 
				"  <title>Order</title>" + 
				"  <link rel=\"self\" href=\"http://localhost:8080/feed\" />" + 
				"  <author>" + 
				"    <name>Big Money Online Commerce Inc.</name>" + 
				"  </author>" + 
				"  <subtitle>List of all orders</subtitle>" + 
				"  <id>https://github.com/ewolff/microservice-atom/order</id>" + 
				"  <updated>2017-04-20T15:28:50Z</updated>" + 
				"  <entry>" + 
				"    <title>Order 1</title>" + 
				"    <link rel=\"alternate\" href=\"http://localhost:8080/order/1\" />" + 
				"    <id>http://localhost:8080/1</id>" + 
				"    <updated>2017-04-20T15:27:58Z</updated>" + 
				"    <content type=\"application/json\" src=\"http://localhost:8080/order/1\" />" + 
				"    <summary>This is the order 1</summary>" + 
				"  </entry>" + 
				"</feed>";
	}
	
	@RequestMapping(value="/order/1", method=RequestMethod.GET)
	public String order1() {
		return "{" + 
				"  \"id\" : 1," + 
				"  \"customer\" : {" + 
				"    \"customerId\" : 1," + 
				"    \"name\" : \"Wolff\"," + 
				"    \"firstname\" : \"Eberhard\"," + 
				"    \"email\" : \"eberhard.wolff@gmail.com\"," + 
				"    \"street\" : \"Unter den Linden\"," + 
				"    \"city\" : \"Berlin\"" + 
				"  }," + 
				"  \"updated\" : \"2017-04-20T15:42:12.351+0000\"," + 
				"  \"shippingAddress\" : {" + 
				"    \"street\" : \"Ohlauer Str. 43\"," + 
				"    \"zip\" : \"10999\"," + 
				"    \"city\" : \"Berlin\"" + 
				"  }," + 
				"  \"billingAddress\" : {" + 
				"    \"street\" : \"Krischerstr. 100\"," + 
				"    \"zip\" : \"40789\"," + 
				"    \"city\" : \"Monheim am Rhein\"" + 
				"  }," + 
				"  \"orderLine\" : [ {" + 
				"    \"count\" : 42," + 
				"    \"item\" : {" + 
				"      \"itemId\" : 1," + 
				"      \"name\" : \"iPod\"," + 
				"      \"price\" : 42.0" + 
				"    }" + 
				"  } ]," + 
				"  \"numberOfLines\" : 1," + 
				"  \"_links\" : {" + 
				"    \"self\" : {" + 
				"      \"href\" : \"http://localhost:8080/order/1\"" + 
				"    }," + 
				"    \"order\" : {" + 
				"      \"href\" : \"http://localhost:8080/order/1\"" + 
				"    }" + 
				"  }" + 
				"}";
	}

	
}
