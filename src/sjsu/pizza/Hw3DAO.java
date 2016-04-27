package sjsu.pizza;
import edu.cs157b.util.*;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Hw3DAO {
	//service layer call this class to modify the data object
	
	private static SessionFactory sessionFactory;
	private static Transaction transaction;
    private static Session session; 
    
	public Hw3DAO(){
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	//curl http://localhost:8080/RestHibernate/api/orders
	public String getAllOrders(){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Order where DTYPE = :type");//order = name in of class not table name
			query.setString("type", "Order");
			List<Order> orders = query.list();
			for(Order o : orders){
				retString +="Order ID: " + o.orderID + "--";
				retString += "Delivery Time: " + o.deliveryTime + "--";
				retString += "Payment: " + o.payment + "--";
				retString += "Price " + o.price + "--";
				retString += "Size: " + o.size + "--";
				retString += "Customer ID: " + o.getCustomerID() + "----------";
				
			}
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	
	//curl http://localhost:8080/RestHibernate/api/discounted_order
	public String getAllDiscOrders(){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Order where DTYPE = :type");//order = name in of class not table name
			query.setString("type", "DiscountedOrder");
			List<Order> orders = query.list();
			for(Order o : orders){
				retString +="Order ID: " + o.orderID + "--";
				retString += "Delivery Time: " + o.deliveryTime + "--";
				retString += "Payment: " + o.payment + "--";
				retString += "Price " + o.price + "--";
				retString += "Size: " + o.size + "--";
				retString += "Customer ID: " + o.getCustomerID() + "----------";
			}
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	//curl http://localhost:8080/RestHibernate/api/orders/1
	public String getOrder(int id){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Order where orderID = :id");//order = name in of class not table name
			query.setInteger("id", id);
			Order o = (Order)query.uniqueResult();
			retString +="Order ID--" + o.orderID + " | ";
			retString += "Delivery Time--" + o.deliveryTime + " | ";
			retString += "Payment--" + o.payment + " | ";
			retString += "Price--" + o.price + " | ";
			retString += "Size--" + o.size + " | ";
			retString += "Customer ID--" + o.getCustomerID();
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	
	public String updatePrice(int id, double price){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Order where orderID = :id");//order = name in of class not table name
			query.setInteger("id", id);
			Order o = (Order)query.uniqueResult();
			System.out.println("The old price is " + o.getPrice());
			o.setPrice(price);
			System.out.println("The new price is " + price + "Updated: " + o.price);
			session.saveOrUpdate(o);
			
			retString +="Order ID--" + o.orderID + " | ";
			retString += "Delivery Time--" + o.deliveryTime + " | ";
			retString += "Payment--" + o.payment + " | ";
			retString += "Price--" + o.price + " | ";
			retString += "Size--" + o.size + " | ";
			retString += "Customer ID--" + o.getCustomerID();
			transaction.commit();
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}

	public Order makeOrder(Order order) {
		try{
			session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
		    
			session.save(order);
			for(Topping t : order.allToppings){
				session.save(t);
			}
			System.out.println(order.orderID);
			transaction.commit();
		}catch(HibernateException e){
			transaction.rollback();
			System.out.println("Transaction is rolled back.");
	        e.printStackTrace();
		}finally{
			session.close();
		}
		return order;
	}

	public void makeDiscountedOrder(Customer customer, DiscountedOrder order, ArrayList<Topping> list) {
		try{
			session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
		    
		    order.setCustomer(customer);
		    for(Topping t : list){
		    	t.getOrder().add(order);
		    }
		    session.save(order);
		    transaction.commit();
			
		}catch(HibernateException e){
			transaction.rollback();
			System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
		}finally{
			session.close();
		}
	}

	public Boolean deleteOrder(int orderNum) {
		try{
		    session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
	
			Query query = session.createQuery("FROM Order where orderID = :id");
			query.setInteger("id", orderNum);
			Order delOrder = (Order)query.uniqueResult();
			if(delOrder == null){
				return false;
			}else{
				session.delete(delOrder);
				transaction.commit();
				return true;
			}
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        e.printStackTrace();
	        return false;
		}
		finally{
			session.close();
		}
	}
	public String getAllCustomers(){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Customer");//order = name in of class not table name
			List<Customer> customers = query.list();
			for(Customer c : customers){
				retString += "User ID--" + c.getUserID() + " | ";
				retString += "Username--" + c.getUsername() + " | ";
				retString += "Password--" + c.getPassword() + " | ";
				retString += "Street--" + c.getAddress().getstreet() + " | ";
				retString += "City--" + c.getAddress().getcity() + " | ";
				retString += "State--" + c.getAddress().getstate() + " | ";
				retString += "Zipcode--" + c.getAddress().getzipCode() + "------------";
			}
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	public String getCustomer(int id){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Customer where userID = :id");//order = name in of class not table name
			query.setInteger("id", id);
			Customer c = (Customer)query.uniqueResult();
			retString += "User ID--" + c.getUserID() + " | ";
			retString += "Username--" + c.getUsername() + " | ";
			retString += "Password--" + c.getPassword() + " | ";
			retString += "Street--" + c.getAddress().getstreet() + " | ";
			retString += "City--" + c.getAddress().getcity() + " | ";
			retString += "State--" + c.getAddress().getstate() + " | ";
			retString += "Zipcode--" + c.getAddress().getzipCode();
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	public String signUp(Customer customer) {
		try{
		    session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
	
			Query query = session.createQuery("FROM Customer where username = :user");
			query.setString("user", customer.getUsername());
			Customer customerChec = (Customer)query.uniqueResult();
			if(customerChec == null){
				session.save(customer);
			    transaction.commit();
			    return "User successfully registered";
			}else{
				return "User already exists";
			}
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        //e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
	}
	
	public String updateUsername(int id, String name){

		String retString = "";
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Customer where userID = :id");//order = name in of class not table name
			query.setInteger("id", id);
			Customer c = (Customer)query.uniqueResult();
			c.setUsername(name);
			session.saveOrUpdate(c);
			
			retString += "User ID--" + c.getUserID() + " | ";
			retString += "Username--" + c.getUsername() + " | ";
			retString += "Password--" + c.getPassword() + " | ";
			retString += "Street--" + c.getAddress().getstreet() + " | ";
			retString += "City--" + c.getAddress().getcity() + " | ";
			retString += "State--" + c.getAddress().getstate() + " | ";
			retString += "Zipcode--" + c.getAddress().getzipCode();
			
			transaction.commit();
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        e.printStackTrace();
	        return null;
		}
		finally{
			session.close();
		}
		return retString;
	}
	
	public String deleteCustomer(int userID) {
		String retString = "";
		try{
		    session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
	
			Query query = session.createQuery("FROM Customer where userID = :id");
			query.setInteger("id", userID);
			Customer c = (Customer)query.uniqueResult();
			if(c == null){
				retString = "Element you are trying to delete does not exist";
			}else{
				session.delete(c);
				transaction.commit();
				retString = "Element sucessfully deleted";
			}
			
		}catch(HibernateException e){
	        transaction.rollback();
	        System.out.println("Transaction is rolled back.");
	        e.printStackTrace();
		}
		finally{
			session.close();
		}
		return retString;
	}
}
