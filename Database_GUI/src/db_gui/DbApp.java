package db_gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;



public class DbApp {

	protected static String user_email;
	static Connection conn;
	static boolean succeed_connection;

	public DbApp() {
		// TODO Auto-generated constructor stub
		
		boolean success=false;
		do{
			
			try {
	     		Class.forName("org.postgresql.Driver");
	     		String url;
	     		Properties props;
	     		props = new Properties();
	     		

	     		
			 		int reply = JOptionPane.showConfirmDialog(null, "Press 'Yes' to use the default Credentials to connect to the Database.\n\n"
							+ "If you want to insert a new Database Url, User and Password press 'No'.", 
							"Connecting to the Database...", JOptionPane.YES_NO_CANCEL_OPTION);
				        		
				    if (reply == JOptionPane.YES_OPTION){
						url = "jdbc:postgresql://localhost:5432/proj1part";
						props.setProperty("user","postgres");
						props.setProperty("password","0000");
						success=true;
						succeed_connection=true;
			 		}else if (reply == JOptionPane.CANCEL_OPTION){
						url = "jdbc:postgresql://localhost:5432/proj1part";
						props.setProperty("user","postgres");
						props.setProperty("password","0000");
			 			succeed_connection=false;
			 			success=true;
			 		}
				    else{		 			
			 			db_Credentials a = new db_Credentials();
			 			succeed_connection=true;
			 			a=gui_insert_dbs_credentials();
						//System.out.println(a.getUrl() + a.getPasswd() + a.getUser());
			 				
			 			url = a.getUrl();
						props.setProperty("user",a.getUser());
						props.setProperty("password", a.getPasswd());
			 		}
			 		
				    if(succeed_connection){
				    
						//props.setProperty("ssl","true");
						conn = DriverManager.getConnection(url, props);
						conn.setAutoCommit(false);
						success=true;
				    }
				    
			 		//conn.setAutoCommit(false);
					//conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			} catch(Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null,
	            	    "Error attempting to connect to the Database cause , \n\n'' " + e.toString() + " wt''\n\nConnection denied.\nPlease try again.." ,
	            		"Connection error",
	            	    JOptionPane.ERROR_MESSAGE);
	            success=false;
	            succeed_connection=false;
			}
		
		}while(!success && !succeed_connection);
		
	}

	@SuppressWarnings("deprecation")
	public static db_Credentials gui_insert_dbs_credentials() {
		
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        JOptionPane a = new JOptionPane();
        JTextField userField;
        JTextField urlField;        
        JPasswordField pSecretField;
        JLabel lbLabel;
        
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbLabel = new JLabel("Database Url: ");
        a.add(lbLabel);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbLabel, cs);

        
        urlField = new JTextField(30);
        urlField.setText("jdbc:postgresql://localhost:5432/proj1part");
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(urlField, cs);
        
        lbLabel = new JLabel("DB Login: ");
        a.add(lbLabel);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbLabel, cs);
 
        userField = new JTextField(30);
        userField.setText("postgres");
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(userField, cs);
 
        lbLabel = new JLabel("DB Password: ");
        a.add(lbLabel);
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbLabel, cs);
 
        pSecretField = new JPasswordField(30);
        pSecretField.setText("0000");
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pSecretField, cs);
        
        a.add(panel);
        
        panel.setBorder(new LineBorder(Color.GRAY));
        
	    int result = JOptionPane.showConfirmDialog(null, panel, 
	               "Connection to the Database ...", JOptionPane.OK_CANCEL_OPTION);
    
	      if (result == JOptionPane.OK_OPTION) {
	    	  //DbApp db2 = new DbApp();
	    	  return new db_gui.db_Credentials(urlField.getText(),userField.getText(),pSecretField.getText()); 
	      }else{
	    	  succeed_connection=false;
	      }
		
	      return new db_Credentials("jdbc:postgresql://localhost:5432/proj1part","postgres","0000");
		
	}
		
	private static boolean loginField() throws SQLException {
		
		JOptionPane.showMessageDialog(null,
        	    "You are now connected to the Server !" ,
        		"Connection established",
        	    JOptionPane.INFORMATION_MESSAGE);
		
		boolean success=false;
		do{
			
			JTextField userField = new JTextField(15);
			JPasswordField paswdField = new JPasswordField(15);
		     
			JPanel myPanel = new JPanel();
		      
		      myPanel.add(new JLabel("User Email : "));
		      myPanel.add(userField);
		      myPanel.add(Box.createHorizontalStrut(20)); // a spacer
		      myPanel.add(new JLabel("Password : "));
		      myPanel.add(paswdField);
			
		      int result = JOptionPane.showConfirmDialog(null, myPanel, 
		               "Login", JOptionPane.OK_CANCEL_OPTION);
		      
		      if (result == JOptionPane.OK_OPTION){
		    	  
		  		Statement pst = conn.createStatement();	
				
		  		user_email=userField.getText();
		  		
				ResultSet login = pst.executeQuery("select exists(select * from member where email='"+user_email+"' and \"thePassword\" ='"+paswdField.getText()+"');");
				login.next();
				String login_res=login.getString(1);
				
				
				if (login_res.equals("t")){
					JOptionPane.showMessageDialog(null,
			        	    "You are succesfully logged in!" ,
			        		"Login success",
			        	    JOptionPane.INFORMATION_MESSAGE);
					success=true;
					return true;
				}else{
					JOptionPane.showMessageDialog(null,
			        	    "The password you’ve entered is incorrect." ,
			        		"Login attempt",
			        	    JOptionPane.ERROR_MESSAGE);
				}			
		      }else{
		    	  success=true;
		    	  return false;
		      }
		      
		}while(!success);
		return true;  
}

	public static void main(String args[]) throws SQLException{
		
		//connecting to the database ...
		DbApp db = new DbApp();
		
		if(succeed_connection){				
			if(db.loginField()){

				//Create and set up the window.
		        JFrame frame = new JFrame("Menu");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        
		        //label
		        JLabel label = new JLabel("Please choose from the following actions:");
		        JPanel panel1 = new JPanel();
				frame.add(panel1);
				panel1.add(label);
		        
		        //Set up the content pane.
		        addComponentsToMenu(frame.getContentPane());
		
		        //Display the window.
		        //frame.pack();
		        frame.setVisible(true);
		        frame.setSize(500,400);}
	
			}
	}	
	
	public static void addComponentsToMenu(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        addAButton("1. Find your n degree network.", pane,1);
        addAButton("2. Write a comment on a specific article.", pane,2);
        addAButton("3. Send a message.", pane,3);
        addAButton("4. Submit the transaction.", pane,4);
        addAButton("5. Cancel the transaction.", pane,5);
        addAButton("6. Exit and Save transaction.", pane,6);
        
        
    }
	
	private static void addAButton(String text, Container container, Integer k) {
        
		JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(180,120));
        container.add(button);

       if (k ==1){//network
			button.addActionListener( new Action1(conn,user_email));
        }
        else if (k ==2){//comment
			button.addActionListener( new Action2(conn,user_email));
        }
        else if (k ==3){//email
			button.addActionListener( new Action3(conn,user_email));
        }
        else if (k ==4){//submit
			button.addActionListener( new Action4(conn));
        }
        else if (k ==5){//cancel
        	button.addActionListener( new Action5(conn));
        }else{//exit program
        	button.addActionListener( new Action6(conn));
        }
        	
        	
	}
	
}

final class db_Credentials {
    private final String url;
    private final String user;
    private final String passwd;
    

    public db_Credentials(String url, String user,String passwd) {
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }

	public db_Credentials() {
		// TODO Auto-generated constructor stub
        url = "";
        user = "";
        passwd = "";
	}

	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
 	public String getPasswd() {
		return passwd;
	}

}

//Button 1
class Action1 implements ActionListener {        

    static Connection conn;
	private static String user_email;
	private static int n;

	public Action1(Connection conn, String user_email) {
		this.conn=conn;
		this.user_email=user_email;
	}

	public void actionPerformed (ActionEvent e) {     

		JTextField netField;
		int result;
	do{	
		netField = new JTextField(15);
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Insert the Maximum Connection Degree value to filter your connections : "));
		myPanel.add(netField);

		result = JOptionPane.showConfirmDialog(null, myPanel, 
		       "Connection to the Database ...", JOptionPane.OK_CANCEL_OPTION);
		
		n=Integer.parseInt(netField.getText());
		
	}while(!(result==JOptionPane.OK_OPTION)&&(n>0));



		try {
			print_userNetwork_filteredbyN(n);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  }
  
	public static void print_userNetwork_filteredbyN(int n) throws SQLException{
				
		Statement pst = conn.createStatement();
		int table_size=0;		
		
		// Ypologismos kai arxikopoihsh tou pinaka pou tha apothikeytei to epistrefomeno query..
		
		ResultSet count = pst.executeQuery("select count(*)"
				+ " from q8_search_usernetwork('" + user_email + "') as u_network where connection_degree2<="+n+";");
		
		if (count.next()) {
			table_size=count.getInt(1);
		}
			
		//H arxikopoihsh tou pinaka ws disdiastato pinaka 2 sthlwn (logw tou query) kai table_size grammwn analoga me tis syndeseis
		//tou xrhsth..
		Object[][] rows = new Object[table_size][2];
		
		ResultSet rs = pst.executeQuery("select u_network.connectedwithuser_email,u_network.connection_degree2"
				+ " from q8_search_usernetwork('" + user_email + "') as u_network where connection_degree2<="+n+";");
		
		int i=0;
		
		//anathesh twn epistrefomenwn stoixeiwn tou query , ston pinaka rows..
		while (rs.next()) {
			//System.out.println(" title: "+rs.getString(1)+"ID: "+rs.getInt(2));
			rows[i][0]=rs.getString(1);
			rows[i][1]=rs.getInt(2);
			i++;
		}
		
		//arxikopoihsh tou pinaka onomasias twn sthlwn..
		Object[] cols = {
			    "Connected with Users","Connection Degree (n="+n+")"
			};
		
		JTable table = new JTable(rows, cols);
		JScrollPane jsw = new JScrollPane(table);
		jsw.setPreferredSize(new Dimension(500, 500));
		
		//emfanish tou epaggelmatikou diktyou tou xrhsth -mexri vathmo N-
		JOptionPane.showMessageDialog(null,jsw,"Your Network", JOptionPane.INFORMATION_MESSAGE);

	}
	
	
}

//Button 2
class Action2 implements ActionListener {        

  static Connection conn;
	private static String user_email;

	public Action2(Connection conn, String user_email) {
		this.conn=conn;
		this.user_email=user_email;
	}

	public void actionPerformed (ActionEvent e) {     
		try {
			comment_article();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void comment_article() throws SQLException{

		Statement pst = conn.createStatement();
		int table_size=0;		
		
		// Ypologismos kai arxikopoihsh tou pinaka pou tha apothikeytei to epistrefomeno query..
		
		ResultSet count = pst.executeQuery("select count(*) from article");
		
		if (count.next()) {
			table_size=count.getInt(1);
		}
			
		//H arxikopoihsh tou pinaka ws dysdiastato.. 6 sthlwn logw twn 6 sthlwn tou table article kai table_size grammwn analoga me ta posa
		//arthra entopise...
		Object[][] rows = new Object[table_size][6];

		ResultSet rs = pst.executeQuery("select * from article");
				
		int i=0;
		while (rs.next()) {
			rows[i][0]=rs.getInt(1);
			rows[i][1]=rs.getString(2);
			rows[i][2]=rs.getInt(3);
			rows[i][3]=rs.getString(4);
			rows[i][4]=rs.getDate(5);
			rows[i][5]=rs.getString(6);
			i++;
		}
		
		Object[] cols = {
			    "Article ID","Title","Category ID","The Text","Date Posted","Email"
			};
		
		JTable table = new JTable(rows, cols);
		
		JOptionPane pane = new JOptionPane();
		 //int result = pane.showConfirmDialog(null, "aa");
		JScrollPane jsw = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(1000, 500));
		pane.add(jsw);
		
		JTextField article_Field = new JTextField(3);
		JTextField comment_Field = new JTextField(65);
		
		JPanel myPanel = new JPanel();
		  
		myPanel.add(new JLabel("ArticleID:"));
		myPanel.add(article_Field);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Comment here:"));
		myPanel.add(comment_Field);
		
		pane.add(myPanel);
	
			
		//emfanish twn arthtrwn , kai ths epiloghs an o xrhsths thelei na sxoliasei...
		int result = JOptionPane.showConfirmDialog(null,pane,"Articles", JOptionPane.OK_CANCEL_OPTION);

		
		if (result == JOptionPane.OK_OPTION) {
	    	  //DbApp db2 = new DbApp();
			int articleID = Integer.parseInt(article_Field.getText());
			String comment=comment_Field.getText(); 
	    	//if(articleID)
			
	    	rs = pst.executeQuery("select max(\"commentID\") from article_comment;");
	    	rs.next();
	    	int article_commentID= rs.getInt(1);
	 	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   //get current date time with Date()
		   Date date = new Date();
		   try{
			   pst.executeUpdate("insert into article_comment values("+(article_commentID+1)+",'"+comment+"',"+articleID+",'"+user_email+"',date '"+dateFormat.format(date)+"');");
	           JOptionPane.showMessageDialog(null,
	            	    "Comment Inserted Successfully !",
	            		"Comment inserted",
	            	    JOptionPane.INFORMATION_MESSAGE);
			   
			   
		   }catch(Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null,
	            	    "Error attempting the insertion cause , \n\n'' " + e.toString(),
	            		"Insertion error",
	            	    JOptionPane.ERROR_MESSAGE);
	    	
		   }
	
	}
	}

	
}	

//Button 3
class Action3 implements ActionListener {        

    static Connection conn;
	private static String user_email;

	public Action3(Connection conn, String user_email) {
		this.conn=conn;
		this.user_email=user_email;
	}

	public void actionPerformed (ActionEvent e) {     
		
		try {
			send_message_toUser();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

	public void send_message_toUser() throws SQLException{
		
		Statement pst = conn.createStatement();
		int table_size=0;		

		// Ypologismos kai arxikopoihsh tou pinaka pou tha apothikeytei to epistrefomeno query..

		ResultSet count = pst.executeQuery("select count(*) from member");

		if (count.next()) {
			table_size=count.getInt(1);
		}
			
		//H arxikopoihsh tou pinaka ws dysdiastato.. 6 sthlwn logw twn 6 sthlwn tou table article kai table_size grammwn analoga me ta posa
		//arthra entopise...
		Object[][] rows = new Object[table_size][6];

		ResultSet rs = pst.executeQuery("select email from member order by email");
				
		int i=0;
		while (rs.next()) {
			rows[i][0]=rs.getString(1);
			i++;
		}

		Object[] cols = {
			    "DataBase Users"
			};

		JTable table = new JTable(rows, cols);
		JOptionPane pane = new JOptionPane();
		 //int result = pane.showConfirmDialog(null, "aa");
		JScrollPane jsw = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(1100, 300));
		pane.add(jsw);

		JTextField usermail_Field  = new JTextField(20);
		JTextField subject_Field = new JTextField(55);

		JPanel myPanel = new JPanel();
		  
		myPanel.add(new JLabel("Copy/Type User's email: "));
		myPanel.add(usermail_Field);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Subject :"));
		myPanel.add(subject_Field);

		pane.add(myPanel);

			
		//emfanish twn arthtrwn , kai ths epiloghs an o xrhsths thelei na sxoliasei...
		int result = JOptionPane.showConfirmDialog(null,pane,"Send Message", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {

			String receiver_email = usermail_Field.getText();
			String subject=subject_Field.getText(); 
			
			
			myPanel = new JPanel();
			myPanel.setPreferredSize(new Dimension( 400, 10 ));
			JTextField msg_Field = new JTextField(35);
			myPanel.add(msg_Field);
						
			result = JOptionPane.showConfirmDialog(null,myPanel,("Sending Message to "+receiver_email), JOptionPane.OK_CANCEL_OPTION);
			
			if (result == JOptionPane.OK_OPTION) {
		    	  //DbApp db2 = new DbApp();
				String message =msg_Field.getText();
				rs = pst.executeQuery("select max(\"msgID\") from msg;");
		    	rs.next();
		    	int msgID= rs.getInt(1);
		 	   
		    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			   //get current date time with Date()
			   Date date = new Date();
			   try{
				   pst.executeUpdate("insert into msg values("+(msgID+1)+",date '"+dateFormat.format(date)+"','"+subject+"','"+message+"','"+user_email+"','"+receiver_email+"');");
		           
				   JOptionPane.showMessageDialog(null,
		            	    "Your message has been sent to "+receiver_email+" !",
		            		"Message",
		            	    JOptionPane.INFORMATION_MESSAGE);
			   }catch(Exception e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(null,
		            	    "Error at sending the message cause , \n\n'' " + e.toString(),
		            		"Send Message error",
		            	    JOptionPane.ERROR_MESSAGE);
		    	
			   }

		}
			
			
		}
	}

}


//Button 4
class Action4 implements ActionListener {        

	static Connection conn;
		private static String user_email;

		public Action4(Connection conn) {
			this.conn=conn;
			this.user_email=user_email;

		}

		public void actionPerformed (ActionEvent e) {     
		
			try {
				//save user's actions..
				conn.commit();
				//conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

}

//Button 5
class Action5 implements ActionListener {        

  static Connection conn;
	private static String user_email;

	public Action5(Connection conn) {
		this.conn=conn;
		this.user_email=user_email;

	}

	public void actionPerformed (ActionEvent e) {     
	
		try {
			//cancel user's actions..
			conn.rollback();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
		
//Button 6 - Exit Programm
class Action6 implements ActionListener {        

static Connection conn;
	private static String user_email;

	public Action6(Connection conn) {
		this.conn=conn;
	}

	public void actionPerformed (ActionEvent e) {     
	
		try {
			//exit programm
			//Commit uncommited data and close connection..
			conn.commit();
			conn.close();
			 System.exit(0);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

