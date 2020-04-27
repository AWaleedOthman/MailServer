package GUI;



import java.net.URL;
import java.util.ResourceBundle;

//import eg.edu.alexu.csd.datastructure.linkedList.cs77_cs84.doublyLinkedLists;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class control implements Initializable {
	
	//ObservableList<String> list = FXCollections.observableArrayList("male","female");

@FXML
private Label lblstatus;
@FXML
private TextField txtUserName;
@FXML
private TextField txPassword;
@FXML
private Label lblstatus1;
@FXML
private Label lblstatus2;
@FXML
private TextField txtUserName1;
@FXML
private TextField txPassword1;
@FXML
private TextField txtmail;
@FXML
private TextField txRePass;

@FXML
private TextField txtfirstName;
@FXML 
private TextField txtlastName;
@FXML 
private DatePicker date;

@FXML 
private ComboBox<String> gender;

@FXML 
private TableView<person> table;

@FXML 
private TableColumn<person,SimpleStringProperty> subject;
@FXML 
private TableColumn<person,SimpleStringProperty> sender;
@FXML 
private TableColumn<person,SimpleStringProperty> Date;



public class person {
	private String subject;
	private String sender;
	private String Date;
	
	person(String subject, String sender, String Date) {
		this.setSubject(subject);
		this.setSender(sender);
		this.setDate(Date);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}
	
	
}
///////////////////////////////////////////////////////////////////////////////////////
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	System.out.println("hi");
	/*table =new TableView();
	subject =new TableColumn();
	sender =new TableColumn();
	Date =new TableColumn();*/
   // ObservableList<person> list =getData();
   
subject.setCellValueFactory( new PropertyValueFactory<person,SimpleStringProperty>("subject"));
sender.setCellValueFactory( new PropertyValueFactory<person,SimpleStringProperty>("sender"));
Date.setCellValueFactory( new PropertyValueFactory<person,SimpleStringProperty>("Date"));
table.setItems( getData());

}
/////////////////////////////////////////////////////
public ObservableList<person> getData(){
	
	ObservableList<person> list = FXCollections.observableArrayList(
			new person("me","him","we")
			);
	list.add(new person("me","him","we"));
	
	return list;
	
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*public void login (ActionEvent event) {
  ObjectReaderWriter object=new ObjectReaderWriter();
  App app=new App();

  doublyLinkedLists	 users =  object.readFile(app.index);

  verify Verify = new verify();

if(app.signin(txtUserName.getText(),txPassword.getText()))
{

	try {
		 Parent  root=FXMLLoader.load(getClass().getResource("/application/login.fxml"));
		 Main test= new Main() ;
	     test.setScene(event, root);	
}
    catch(Exception e) {
         e.printStackTrace();	}
}
	
else {
 
int n=	Verify.correctPass(txtUserName.getText(), txPassword.getText(), users);
	if(n==2) {
		lblstatus.setText("wrong password ! try again");
	}
	else if(n==1) {
		lblstatus.setText("Email not found ! try again");

	}
	
}
}*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*public void signUp1 (ActionEvent event ) {
	try {
	 Parent  root=FXMLLoader.load(getClass().getResource("/application/sign up.fxml"));
     Main test= new Main() ;
     test.setScene(event, root);
	}catch(Exception e) {
        e.printStackTrace();	}
}*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*public void inbox (ActionEvent event ) {
try {
Parent  root=FXMLLoader.load(getClass().getResource("inbox.fxml"));
Main test= new Main() ;
test.setScene(event, root);
}catch(Exception e) {
e.printStackTrace();	}
}*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*public void signUp (ActionEvent event ) {
	try {
     ObjectReaderWriter object=new ObjectReaderWriter();
     App app=new App();
     doublyLinkedLists users =  object.readFile(app.index);
     verify Verify = new verify();
     
     if(!(date.getValue() == null)&& Verify.verifyContact(txtfirstName.getText(), txtlastName.getText(),txtUserName1.getText(),txtmail.getText(), txPassword1.getText())) {
     Contact contact=new Contact(txtfirstName.getText(), txtlastName.getText(),txtUserName1.getText(),txtmail.getText(), txPassword1.getText(),date.getValue().getDayOfMonth(),date.getValue().getMonthValue(), date.getValue().getYear());

    	 if(Verify.verifyPass( txPassword1.getText(), txRePass.getText())) {
    		 if( app.signup(contact)) {
    			 Parent root=FXMLLoader.load(getClass().getResource("/application/login.fxml"));
    		     Main test= new Main() ;

    	     test.setScene(event, root);
    	 }
    		 else { 
    			  if(!Verify.checkEmail(txtmail.getText(), users)) {
				 lblstatus1.setText("Email already exist,");
	    			lblstatus2.setText("enter another one");
			 }
    			  else if(!Verify.checkUserName(txtUserName1.getText(), users)) {
    	    			lblstatus1.setText("Username already exist,");
    	    			lblstatus2.setText("enter another one");
    			 }
    			 
    		 }
    	 }
    	 else {
    			lblstatus1.setText("Verify the password");

    	 }
	} else {
 			lblstatus1.setText("Please fill all the boxes");

    	 }
    	
	}catch(Exception e) {
         e.printStackTrace();	}
}*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*public void backMain(ActionEvent event ) {
	try {
	  Parent  root=FXMLLoader.load(getClass().getResource("/application/main.fxml"));
       Main test= new Main() ;
       test.setScene(event, root);
}
    catch(Exception e) {
         e.printStackTrace();	}
}*/






}




 