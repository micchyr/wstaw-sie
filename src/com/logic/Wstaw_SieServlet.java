package com.logic;

import java.io.IOException;
import javax.servlet.http.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bean.Mail;
import com.bean.Pair;
import com.bean.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@SuppressWarnings("serial")
public class Wstaw_SieServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		play();
	}


	private static List<Person> people;	//list of people in a database
	private static List<Pair> pary;		//list of pairs for the week
		
   public static void play(){  
			   
	   getPeople();		   
	   System.out.println("data got successfully.");
			   
	   if(people.size()%2==1){
		   Random rand = new Random();
			   
		   int num2del = rand.nextInt(people.size());
		   while(0==people.get(num2del).getOmadla() || notYet(0, people.get(num2del).getHistory())){
			   num2del = rand.nextInt(people.size());
		   }
		   System.out.println(people.get(num2del).getSecondNameBasic()+" is a lucky guy.");
		   updateDatabase(people.get(num2del).getId(), 0, people.get(num2del).getOmadla(), 0); 
		   people.remove(num2del);
	   }	
			   
	   System.out.println("drawing...");		   
	   pary=new ArrayList<Pair>();
	   int count=0;
	   int noUno=0;
			   
	   while(!people.isEmpty()){			   			   
				
		   //to avoid never ending loops...
		   if(count>10){
			   undo();	//removes previous pair - probably a reason of the endless loop...
			   count=0;
		   }
		   count++;
		   //
			   
		   //get random number
		   Random rand = new Random();
		   int num = rand.nextInt(people.size() - 1) + 1;
		   System.out.println(num);
		   //			   
				   
		   //check if it is allowed number and save pair
		   if(num!=noUno && people.get(num).getId()!=people.get(noUno).getOmadla() && !notYet(people.get(num).getId(), people.get(noUno).getHistory())){
			   Pair para=new Pair(people.get(noUno), people.get(num));			
			   pary.add(para);
					   
			   people.remove(num);
			   people.remove(noUno);
			   count=0;				   				   				   
			}
			//
	   }
			   
	   send();
   }
		   
   private static void undo(){
	   //get rid of changes in history
	   people.get(0).history=people.get(0).getHistoryStat();
		   
	   //remove pair and add them to people list with updated history (forbidden numbers)
	   if(pary.size()>0){
		   pary.get(pary.size()-1).one.history.add(pary.get(pary.size()-1).two.getId());
		   pary.get(pary.size()-1).two.history.add(pary.get(pary.size()-1).one.getId());
		   people.add(pary.get(pary.size()-1).one);
		   people.add(pary.get(pary.size()-1).two);
		   pary.remove(pary.size()-1);
	   }
   }
		   
   private static void send(){
	   
	   for(Pair para : pary){
		   //update history
		   updateDatabase(para.one.getId(), para.two.getId(), para.one.getOmadla(), para.two.getOmadla()); 
		   System.out.println("updated");

		   //Mail wiadomosc=new Mail(para);
		   //wiadomosc.sendMail();
		   
	   }
   }
		   
   private static boolean notYet(long idx, List<Integer> hist){
   
	   for(int idxH :hist){			   
		   if(idxH==idx) return true;			   
	   }
		   return false;
   }
		   
   private static void getPeople(){
		   
	   people=new ArrayList<Person>();
	   try{
		   	  Connection connect = null;
			 Statement statement = null;
			 Statement statement1 = null;
			 Statement statement2 = null;
			 ResultSet resultSet = null;
			 ResultSet resultSetHistory1 = null;

			 Class.forName("com.mysql.jdbc.GoogleDriver");
			 connect = DriverManager.getConnection("jdbc:mysql://sql3.freesqldatabase.com/sql349930?" + "user=sql349930&password=hY6*dS9%25");
			//connect = DriverManager.getConnection("jdbc:mysql://localhost/Dominiki?" + "user=root&password=./pkawamu3");

				      
		     statement = connect.createStatement();
  		     statement1 = connect.createStatement();

			 resultSet = statement.executeQuery("select * from wstaw_sie_uzytkownicy");			      			      			      			     			      
				      
		      java.sql.Date maxdata=null;
		      int maxHist=0;
				      
		      while (resultSet.next()) {
		    	  List<Integer> hi= new ArrayList<Integer>();
			    	  
		    	  resultSetHistory1 = statement1.executeQuery("select * from wstaw_sie_archiwum where wstawiennik="+resultSet.getInt("id"));
				    	  
		    	  while (resultSetHistory1.next()){
				    		  
		    		  java.sql.Date tmpData = resultSetHistory1.getDate("data");
				    		  
		    		  if(maxdata==null || tmpData.before(maxdata))
		    			  maxdata=tmpData;
				    		  
		    		  hi.add(resultSetHistory1.getInt("omadlany"));
		    	  }
				    	  
		    	  Person person=new Person(
		    			  resultSet.getInt("id"),
		    			  resultSet.getString("imie"),
		    			  resultSet.getString("nazwisko"),
		    			  resultSet.getString("imie_b"),
		    			  resultSet.getString("nazwisko_b"),
		    			  resultSet.getString("email"),
		    			  resultSet.getString("intencja"),
		    			  resultSet.getInt("omadla"),
		    			  hi			    			  
		    			  );
		    	  System.out.println(person.getFirstNameBasic());
		    	  if(maxHist<hi.size())
		    		  maxHist=hi.size();
		    	  people.add(person);
		      }

		      if(maxHist==3){
			      PreparedStatement p1;
			      p1 = connect.prepareStatement("delete from wstaw_sie_archiwum where data= ?;");
			      p1.setDate(1, maxdata);
			      p1.executeUpdate();
		      }
				      
		      resultSet.close();
		      resultSetHistory1.close();
		      statement.close();
		      statement1.close();
		      //p1.close();
		      connect.close();
		   }
		   catch(Exception e) {	 
			   System.out.println(e.getMessage()+" exc");
		   }		   
   }
		   
   private static void updateDatabase(int id1, int id2, int num1, int num2){
		  
	   try{
		   	  Connection connect = null;
		   	  PreparedStatement p1=null;

		      Class.forName("com.mysql.jdbc.GoogleDriver");
		      connect = DriverManager.getConnection("jdbc:mysql://sql3.freesqldatabase.com/sql349930?" + "user=sql349930&password=hY6*dS9%25");		      
		      //connect = DriverManager.getConnection("jdbc:mysql://localhost/Dominiki?" + "user=root&password=./pkawamu3");
		      
		      java.util.Calendar cal = java.util.Calendar.getInstance();
		      java.util.Date utilDate = cal.getTime();
		      SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		      java.sql.Date date = new java.sql.Date(sdf.parse("26/Aug/2014").getTime());
		      //java.sql.Date date = new java.sql.Date(utilDate.getTime());
				      
		      p1 = connect.prepareStatement("update wstaw_sie_uzytkownicy set omadla = ? where id = ?;");
		      p1.setInt(1, id2);
		      p1.setInt(2, id1);
		      p1.executeUpdate();
		      p1 = connect.prepareStatement("insert into  wstaw_sie_archiwum values (?,?,?);");
		      p1.setInt(1, id1);
		      p1.setInt(2, num1);
		      p1.setDate(3, date);
		      p1.executeUpdate();
			      
		      if(id2!=0){
			      p1 = connect.prepareStatement("update wstaw_sie_uzytkownicy set omadla = ? where id = ?;");
			      p1.setInt(1, id1);
			      p1.setInt(2, id2);
			      p1.executeUpdate();
			      p1 = connect.prepareStatement("insert into  wstaw_sie_archiwum values (?,?,?);");
			      p1.setInt(1, id2);
			      p1.setInt(2, num2);
			      p1.setDate(3, date);
			      p1.executeUpdate();			      			      		    				     
		      }
				      
		      p1.close();
		      connect.close();
		   }
		   catch(Exception e) {	 
			   System.out.println(e.getMessage()+" exc");
		   }		   
			   
   }
}	
	


