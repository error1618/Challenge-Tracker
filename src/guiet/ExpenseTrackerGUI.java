package guiet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ExpenseTrackerGUI extends JFrame implements ActionListener {
    private JTextField descriptionField, dateField, amountField;
    private JTextArea displayArea;
    private DefaultListModel<String> expenseListModel;
    private JList<String> expenseList;
    private JComboBox<String> categoryDropdown;
    private JButton addBtn, removeBtn, showTotal, manageCategories;
    // Connect to the database
    static Connection conn = null;
    static Statement stmt;
    String[] cats  = {"CategoryA", "CategoryB", "CategoryC"};
    //ArrayList<String> clist = new ArrayList<String>(Arrays.asList(cats));

    public void deleteElement(String ele) {
    	int index = -1;
    	for (int i = 0; i < cats.length; i++) {
    	    if (cats[i].equals(ele)) {
    	        index = i;
    	        break;
    	    }
    	}

    	if (index != -1) {
    	    String[] newArr = new String[cats.length - 1];
    	    int destIndex = 0;
    	    for (int i = 0; i < cats.length; i++) {
    	        if (i != index) {
    	            newArr[destIndex++] = cats[i];
    	        }
    	    }
    	    cats = newArr;
    }
    };
    public  void addElement(String ele)
    {
       
        // create a new array of size n+1
    	String[] newarr = new String[cats.length+1];
    
        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (int i = 0; i < cats.length; i++) {
            newarr[i] = cats[i];
        }
        newarr[cats.length] = ele;
        cats = newarr;
    };
    
    
    private static void DatabaseFunct() throws ClassNotFoundException, SQLException {
    	try {
    	Class.forName("org.hsqldb.jdbc.JDBCDriver");
    	}catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong !", "Notification", JOptionPane.INFORMATION_MESSAGE);    		
    	}
        conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/", "SA", "");
        stmt = conn.createStatement();    
        try {
        String sql = "CREATE TABLE expn (description varchar(500), ladate varchar(20) not null, amount DECIMAL (13,3),category varchar(15))"; //, CHECK (category IN ('CategoryA', 'CategoryB', 'CategoryC') ) ) ";
        stmt.executeUpdate(sql);
        }catch(SQLSyntaxErrorException e1) {
        	// already created ;
        }
        DatabaseMetaData metaData = conn.getMetaData();
        String dbUsername = metaData.getUserName();
        System.out.println("Database Username: " + dbUsername);
        String dbName = conn.getCatalog();
        System.out.println("Database Name: " + dbName);
        /*String sql ="SET DATABASE SQL NAMES TRUE";
    	stmt.executeUpdate(sql);
    	sql = "GRANT ALL PRIVILEGES ON PUBLIC.* TO 'sa'@'localhost'";
    	stmt.executeUpdate(sql);*/
    }
    		
    public ExpenseTrackerGUI() {
    
    	   setTitle("Expense Tracker");
           setSize(500, 400);
           setLocationRelativeTo(null); // center the window
           setDefaultCloseOperation(EXIT_ON_CLOSE);

           // initialize UI components
           descriptionField = new JTextField();
           dateField = new JTextField();
           amountField = new JTextField();
           categoryDropdown = new JComboBox<String>(cats);
           addBtn = new JButton("Add Expense");
           removeBtn = new JButton("Remove Expense");
           showTotal = new JButton("Show Total");
           manageCategories = new JButton("Manage Categories");

           // initialize the list of expenses
           expenseListModel = new DefaultListModel<>();
           expenseList = new JList<>(expenseListModel);
           JScrollPane expenseListScrollPane = new JScrollPane(expenseList);
           expenseListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
           expenseList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

           // add UI components to the window
           JPanel inputPanel = new JPanel(new GridLayout(4, 2));
           inputPanel.add(new JLabel("Description:"));
           inputPanel.add(descriptionField);
           inputPanel.add(new JLabel("Date:"));
           inputPanel.add(dateField);
           inputPanel.add(new JLabel("Amount:"));
           inputPanel.add(amountField);
           inputPanel.add(new JLabel("Category:"));
           inputPanel.add(categoryDropdown);

           JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
           buttonPanel.add(addBtn);
           buttonPanel.add(removeBtn);
           buttonPanel.add(showTotal);
           buttonPanel.add(manageCategories);

           add(inputPanel, BorderLayout.NORTH);
           add(expenseListScrollPane, BorderLayout.CENTER);
           add(buttonPanel, BorderLayout.SOUTH);

           //action listeners for the buttons
           addBtn.addActionListener(this);
           removeBtn.addActionListener(this);
           showTotal.addActionListener(this);
           manageCategories.addActionListener(this);         
       	   filllist();
         
    }
    
    public void filllist() {
    	expenseListModel.clear();
    	try {
    	    ResultSet re = stmt.executeQuery("select * from expn");
    	    while (re.next()) {
                String ele = "Description: " + re.getString(1) + " Date: " + re.getString(2) + " Amount: " + re.getString(3) + " Category: " + re.getString(4);
                expenseListModel.addElement(ele);
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    }

    public void actionPerformed(ActionEvent e) {  	
    	if (e.getSource() == addBtn) {
        	String description = "";
        	String date = "1/1/1999";
        	Double amount = 0d;
        	String category = "CategoryA";
        	try {
        		if (!(descriptionField.getText() instanceof String)) {
                    throw new IOException("not string");
                }
                 description = descriptionField.getText();
                
        	}catch(IOException e1) {
                JOptionPane.showMessageDialog(null, "Wrong Type description", "Notification", JOptionPane.INFORMATION_MESSAGE);
				return;
        	}
        	try {
        		if (!(dateField.getText() instanceof String)) {
                    throw new IOException("not string");
                }
                date = dateField.getText();
                if(date.split("/",0).length!=3) {
                    throw new IOException("not a date");
                }      
                int counter = 0;
                for(String s : date.split("/",0)) {
                	try {
                	    int num = Integer.parseInt(s);
                	    if(counter == 2 && s.length()!=4) {throw new IOException("not a date");}
                	    if(counter == 1 && num>12 || num<1) {throw new IOException("not a date");}
                	    if(counter == 0 && num>31 || num<1) {throw new IOException("not a date");}
                         counter++;
                	} catch (NumberFormatException e1) {
                        throw new IOException("not a date");  
                	}
                }
        	}catch(IOException e1) {
                JOptionPane.showMessageDialog(null, "Wrong Type Date dd/mm/yyyy", "Notification", JOptionPane.INFORMATION_MESSAGE);
				return;
        	}
        	try {
        		if (!((Double) Double.parseDouble(amountField.getText()) instanceof Double)) {
                    throw new IOException("not double");
                }
                 amount = (Double)Double.parseDouble(amountField.getText());
        	}catch(IOException e1) {
                JOptionPane.showMessageDialog(null, "Wrong Type Money", "Notification", JOptionPane.INFORMATION_MESSAGE);
				return;
        	}
        	try {
        		if (!((String) categoryDropdown.getSelectedItem() instanceof String)) {
        			throw new IOException("not string");
                }
                 category = (String) categoryDropdown.getSelectedItem();
        	}catch(IOException e1) {
                JOptionPane.showMessageDialog(null, "Wrong Type Category", "Notification", JOptionPane.INFORMATION_MESSAGE);	
				return;
        	}
        	// push into database 
            try {
            	String sql = "INSERT INTO expn VALUES ('"+description+"','"+date+"',"+amount+",'"+category+"')";
            	stmt.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
				return;
			}
        	
        	//update display
            // add the expense to the list and clear the input fields
            String expenseString = "Description: " + description + " Date: " + date + " Amount: " + amount + " Category: " + category;
            expenseListModel.addElement(expenseString);
            descriptionField.setText("");
            dateField.setText("");
            amountField.setText("");        	
            
            
            // TODO: add expense to the list and update display
        } else if (e.getSource() == removeBtn) {
            // TODO: remove expense from the list and update display
        	String selectedItem =  expenseList.getSelectedValue();
        	String[] exp = selectedItem.split("(Description:\\s+)|(\\s+Date:\\s+)|(\\s+Amount:\\s+)|(\\s+Category:\\s+)");
            
            String sql = "DELETE FROM expn WHERE description = '"+exp[1]+"' and ladate = '"+exp[2]+"' and amount = "+(double)Double.parseDouble(exp[3])+" and category ='" +exp[4]+"'";
            int rowsDeleted = 0;
			try {
				rowsDeleted = stmt.executeUpdate(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            if (rowsDeleted > 0) {
            	//expenseList.remove(expenseList.getSelectedIndex());
            	expenseListModel.remove(expenseList.getSelectedIndex());
            	JOptionPane.showMessageDialog(null, "deleted successfully", "Notification", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "doent exist", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        	
        } else if (e.getSource() == showTotal) {
        	String sql = "SELECT SUM(amount) FROM expn";
        	ResultSet rs = null;
			try {
				rs = stmt.executeQuery(sql);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	try {
				if (rs.next()) {
	                JOptionPane.showMessageDialog(null, "total = " + rs.getDouble(1), "Notification", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
            // TODO: calculate and display total expense
        } else if (e.getSource() == manageCategories) {
            // TODO: open a dialog box to manage expense categories
        	//    categoryDropdown = new JComboBox<String>(new String[]{"CategoryA", "CategoryB", "CategoryC"});
        	String[] options = {"delete category", "add category"};
        	int Option1 = JOptionPane.showOptionDialog(null, "Select an option", "Options",
        	    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        	if (Option1 != JOptionPane.CLOSED_OPTION) {
        		if(options[Option1] == "delete category") {
        			String Option2 = (String) JOptionPane.showInputDialog(null, "Select a category :",
        			        "Input Dialog", JOptionPane.QUESTION_MESSAGE, null, cats, cats[0]);
                           	if (Option2 != null) {
                           		deleteElement(Option2);
                           		categoryDropdown.removeItem(Option2);
                           	}  

        		}else if(options[Option1] == "add category") {
        			String Option3 = (String) JOptionPane.showInputDialog(null,"hi", JOptionPane.PLAIN_MESSAGE);
        			if (Option3 != null) {
                   		addElement( Option3) ;
                   		categoryDropdown.addItem(Option3);
                   	}  
        		}
        	}
        		
        	}
        	/*
        	String input = (String) JOptionPane.showInputDialog(null, "Select an option:",
        	        "Input Dialog", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
*/
      }
    
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
    	 try {    
             ProcessBuilder pb = new ProcessBuilder("java", "-cp", "src/hsqldb.jar", "org.hsqldb.Server", "--database", "mydb");
             pb.redirectErrorStream(true);
             Process p = pb.start();
             BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
             String line;
             while ((line = br.readLine()) != null) {
                 System.out.println("im heeere"+line);
             }
             p.waitFor();
         } catch (IOException | InterruptedException e) {
             e.printStackTrace();
         }

        DatabaseFunct();
        ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
        gui.setVisible(true);
    }
}
