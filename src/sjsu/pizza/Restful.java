package sjsu.pizza;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api")
public class Restful {
	private Hw3DAO dao = new Hw3DAO(); 
	
	/*
	@Path("/orders")
	@GET
	@Produces(MediaType.TEXT_HTML)
	*/
	//retrieve all orders
	
	//curl http://localhost:8080/RestHibernate/api/orders
	@GET
	@Path("/orders")
	@Produces(MediaType.TEXT_HTML)
	public String retrieveAllOrders(){
		return dao.getAllOrders();
	}
	
	//curl http://localhost:8080/RestHibernate/api/discounted_order
	@GET
	@Path("/discounted_order")
	@Produces(MediaType.TEXT_HTML)
	public String retrieveAllDiscOrders(){
		return dao.getAllDiscOrders();
	}
	
	//curl http://localhost:8080/RestHibernate/api/orders/1
	@GET @Path("orders/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String retrieveOrder(@PathParam("id")int id){
		return dao.getOrder(id);
	}
	
	
	// curl -i -X POST -H "Content-Type: application/json" -d "{""deliveryTime"":""4:50am"",""payment"":""VISA"",""price"":""3.5"",""size"":""SMALL"",""allToppings"":[{""topping"":""Pepperoni"",""price"":"".5""},{""topping"":""Sausage"",""price"":"".5""}],""customer"":{""userID"":""2"",""password"":""lai"",""username"":""kevin"",""address"":{""city"":""san jose"",""state"":""ca"",""street"":""1212"",""zipCode"":""94577""}}}" http://localhost:8080/RestHibernate/api/orders
	@POST
	@Path("/orders")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Order addOrder(Order order){
		order.setPrice(order.calculatePrice(order.size.toString())); //overrides entered price to correctly calculate price
		order.setDeliveryTime(); //overrides entered delivery time to one hour after the post request
		return dao.makeOrder(order);
	}
	
	//curl -i -X PUT -H "Content-Type: application/json" -d "{""orderID"": ""1"", ""price"": ""10""}" http://localhost:8080/RestHibernate/api/orders/1/10
	@Path("orders/{id}/{newprice}")
	@PUT 
	@Produces(MediaType.TEXT_HTML)
	public String updatePrice(
			@PathParam("id") int id,
			@PathParam("newprice") double price){
		return dao.updatePrice(id,price);
	}
	
	//curl -i -X DELETE http://localhost:8080/RestHibernate/api/orders/4
	@Path("orders/{id}")
	@DELETE 
	@Produces(MediaType.TEXT_HTML)
	public String deleteOrder(@PathParam("id") int id){
		return dao.deleteOrder(id).toString();
	}	
	
	//curl http://localhost:8080/RestHibernate/api/customers
	@GET
	@Path("/customers")
	@Produces(MediaType.TEXT_HTML)
	public String retrieveAllCustomers(){
		return dao.getAllCustomers();
	}
	
	//curl http://localhost:8080/RestHibernate/api/customers/2
	@GET
	@Path("/customers/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String retrieveCustomer(@PathParam("id") int id){
		return dao.getCustomer(id);
	}
	  
	//curl -i -X POST -H "Content-Type: application/json" -d "{""password"":""password"",""username"":""username"",""address"":{""city"":""san jose"",""state"":""ca"",""street"":""148 williams"",""zipCode"":""95112""}}" http://localhost:8080/RestHibernate/api/customers
	@POST @Path("/customers")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String addCustomer(Customer customer){
		return dao.signUp(customer);
	}
	
	//
	@Path("customers/{id}/{name}")
	@PUT 
	@Produces(MediaType.TEXT_HTML)
	public String updateUsername(
			@PathParam("id") int id,
			@PathParam("name") String name){
		return dao.updateUsername(id,name);
	}
	
	//
	@DELETE @Path("customers/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String deleteUser(@PathParam("id") int id){
		return dao.deleteCustomer(id);
	}
	
	
	
	// curl -i -X POST http://localhost:8080/FirstRestfulService/api/database/test
   /*
	@Path("/{param}")
    @POST
    public Response postMsg(@PathParam("param") String msg) {
        String output = "POST:Jersey say : " + msg;
        System.out.println(msg);
        return Response.status(200).entity(output).build();
    }
    
	*/
}
