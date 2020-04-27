package GUI;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import Classes.App;
import Classes.Mail;
import Classes.Priority;
import Classes.SinglyLinkedList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComposeController implements Initializable {

	private App app;
	private Stage currentStage;
	
	private boolean isSent = false;
	@FXML
	private Button addRecieverBtn;
	@FXML
	private Button removeRecieverBtn;
	@FXML
	private Button composeBtn;
	@FXML
	private Button closeBtn;
	@FXML
	private Button removeAttachmentBtn;
	@FXML
	private Button attachBtn;
	@FXML
	private ChoiceBox<String> priorityChoiceBox;
	@FXML
	private Label recieverErrorLbl;
	@FXML
	private ListView<String> recieversList;
	@FXML
	private ListView<String> attachList;
	@FXML
	private TextArea bodyTxtArea;
	@FXML
	private TextField recieverTxt;
	@FXML
	private TextField subjectTxt;
	
	FileChooser fil_chooser = new FileChooser(); 
	
	public void setParameters(App app, Stage stage) {
		this.app = app;
		this.currentStage = stage;
	}
	
	public void addRecieverBtnClicked() {
		String address = recieverTxt.getText();
		// Check the address here
		if (app.addressExists(address.toLowerCase())) {
			recieverTxt.setText("");
			recieversList.getItems().add(address);
			recieverErrorLbl.setText("");
		}
		else {
			recieverErrorLbl.setText("Invalid address!!");
		}
		
	}
    public void attachBtnClicked() {
    	Stage stageFileChooser = new Stage();
        // get the file selected 
        File file = fil_chooser.showSaveDialog(stageFileChooser); 

        if (file != null) { 
        	attachList.getItems().add(file.getAbsolutePath()); 
        }
    }
    
    private Mail getMail() {
    	
    	String title = subjectTxt.getText();
    	Date date = new Date();
    	Priority priority = Priority.valueOf(priorityChoiceBox.getSelectionModel().getSelectedItem()); 
    	ObservableList<String> list = recieversList.getItems();
    	SinglyLinkedList recievers = new SinglyLinkedList();
    	Iterator<String> it = list.iterator();
    	while (it.hasNext()) {
    		recievers.add(it.next());
    	}
    	list = attachList.getItems();
    	SinglyLinkedList attachments = new SinglyLinkedList();
    	it = list.iterator();
    	while (it.hasNext()) {
    		attachments.add(new File(it.next()));
    	}
    	String message = bodyTxtArea.getText();
    	Mail mail = new Mail(app.updateMailID(), title, app.getLoggedinUser().getAddress(), app.getLoggedinUser().getName(), date, priority);
    	mail.setText(message);
    	mail.setRecieverAddress(recievers);
    	mail.setAttachments(attachments);
    	
    	return mail;
    }
    
    @FXML
    public void composeBtnClicked() {
    	// Compose mail Compose(getMail)
    	app.compose(getMail());
    	isSent = true;
    	currentStage.close();
    	//Stage stage = (Stage) composeBtn.getScene().getWindow();
        //stage.close();
    }
    
    public void removeAttachmentBtnClicked() {
    	if(attachList.getSelectionModel().getSelectedIndex() < 0) { return;}
    	attachList.getItems().remove(attachList.getSelectionModel().getSelectedIndex());
    }

    public void removeRecieverBtnClicked() {
    	if(recieversList.getSelectionModel().getSelectedIndex() < 0) { return;}
    	recieversList.getItems().remove(recieversList.getSelectionModel().getSelectedIndex());
    }
    
    @FXML
    public void draft() {
    	if (isSent) {
    		return;
    	}
    	app.draft(getMail());
    	currentStage.close();
    }
    
    public void loadDraft(Mail mail) {
    	subjectTxt.setText(mail.getTitle());
    	Iterator<Object> it = mail.getRecieverAddress().iterator();
    	while (it.hasNext()) {
    		recieversList.getItems().add(it.next().toString());
    	}
    	it = mail.getAttachments().iterator();
    	while (it.hasNext()) {
    		attachList.getItems().add(it.next().toString());
    	}
    	bodyTxtArea.setText(mail.getText());
    	priorityChoiceBox.getSelectionModel().select(mail.getPriority().toString());
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		priorityChoiceBox.getItems().addAll(Priority.Serious.toString(), Priority.Essential.toString(), 
				Priority.Ordinary.toString(), Priority.Secondary.toString());
		priorityChoiceBox.getSelectionModel().select(Priority.Ordinary.toString());
	}
}
