package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.util.Utils;

import Classes.App;
import Classes.Folder;
import Classes.Mail;
import Classes.Priority;
import Classes.Sort;
import Classes.SortAttribute;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	private App app;
	
	@FXML
	private AnchorPane rootPane;
	@FXML
	private Button sortBtn;
	@FXML
	private Button priorityBtn;
	@FXML
	private Button searchBtn;
	@FXML
	private Button refreshBtn;
	@FXML
	private CheckBox reverseChkBox;
	@FXML
	private ChoiceBox<String> sortChoiceBox;
	@FXML
	private ChoiceBox<String> searchChoiceBox;
	@FXML
	private Label bannerLbl;
	@FXML
	private Pagination pgr;
	@FXML
	private TableView<MailHeader> mailsTbl;
	@FXML
	private TextField searchTxt;
	@FXML
	private TitledPane inboxTPane;
	@FXML
	private Button composeBtn;
	
	
	public class MailHeader {
		
		private int ID;
		
		private String title;
		
		private String senderName;
		
		private String address;
		
		private String date;
		
		private Priority priority;
		
		public MailHeader(int ID, String title, String sendername, String address, String date, Priority priority) {
	        this.ID = ID;
			this.title = title;
	        this.senderName = sendername;
	        this.address = address;
	        this.date = date;
	        this.priority = priority;
	    }

		public String getTitle() {
			return title;
		}

		public String getSenderName() {
			return senderName;
		}

		public String getAddress() {
			return address;
		}

		public String getDate() {
			return date;
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}

		public Priority getPriority() {
			return priority;
		}

		public void setPriority(Priority priority) {
			this.priority = priority;
		}
		
	}
	
	private void showMails(int i) {
		mailsTbl.getItems().clear();
		Mail[] mails = (Mail[])app.listEmails(i);
		for (int j = 0; j < 10 && mails[j] != null; j++) {
			MailHeader MH = new MailHeader(mails[j].getID(), mails[j].getTitle(), mails[j].getSenderName(), mails[j].getSenderAddress(), mails[j].getDate().toString(), mails[j].getPriority());
			mailsTbl.getItems().add(MH);
		}
	}
	
	public void initialize(App app) {
		this.app = app;
		bannerLbl.setText("Hello User"); // Replace user name here
		sortChoiceBox.getItems().addAll(SortAttribute.Date.toString(), 
				SortAttribute.Title.toString(), SortAttribute.SenderName.toString());
		sortChoiceBox.getSelectionModel().select(SortAttribute.Date.toString());
		// Preparing table columns and setting attributes
		TableColumn<MailHeader, String> IDColumn=new TableColumn<>();
		IDColumn.setVisible(false);
	    IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
		TableColumn<MailHeader, String> titleColumn=new TableColumn<>("Title");
	    titleColumn.setMinWidth(150);
	    titleColumn.setResizable(false);
	    titleColumn.setSortable(false);
	    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
	    TableColumn<MailHeader, String> senderNameColumn=new TableColumn<>("Sender name");
	    senderNameColumn.setMinWidth(145);
	    senderNameColumn.setResizable(false);
	    senderNameColumn.setSortable(false);
	    senderNameColumn.setCellValueFactory(new PropertyValueFactory<>("SenderName"));
	    TableColumn<MailHeader, String> addressColumn=new TableColumn<>("Address");
	    addressColumn.setMinWidth(150);
	    addressColumn.setResizable(false);
	    addressColumn.setSortable(false);
	    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
	    TableColumn<MailHeader, String> dateColumn=new TableColumn<>("Date");
	    dateColumn.setMinWidth(200);
	    dateColumn.setResizable(false);
	    dateColumn.setSortable(false);
	    dateColumn.setCellValueFactory(new PropertyValueFactory<MailHeader, String>("Date"));
	    TableColumn<MailHeader, String> priorityColumn=new TableColumn<>("Priority");
	    priorityColumn.setMinWidth(150);
	    priorityColumn.setResizable(false);
	    priorityColumn.setSortable(false);
	    priorityColumn.setCellValueFactory(new PropertyValueFactory<MailHeader, String>("Priority"));
	    mailsTbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	                if(mouseEvent.getClickCount() == 2){
	                    openMail();
	                }
	            }
	        }
	    }); 
	    mailsTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    // Add columns to the table view
	    mailsTbl.getColumns().addAll(titleColumn, senderNameColumn, addressColumn, dateColumn, priorityColumn);
		// Max page count depending on the number of loaded mails in the current folder and filter
	    pgr.setPageCount((int)(((float)app.availableMailsCount()/10) + 0.9));
	    // Load mails in the page 1
		showMails(1);
    }
	
	public void loadInbox() {
		// VBox to carry the user folders
		// Replace 5 with the user number of folders
		VBox folders = new VBox(5);
		// Generating titled pane for each folder and set its attributes
		// Generate the basic user folders
		TitledPane tPane = new TitledPane();
		tPane.setText("Primary");
		tPane.setOnMouseClicked(new FoldersEventHandler());
		tPane.setCollapsible(false);
		tPane.setExpanded(false);
		folders.getChildren().add(tPane);
		tPane = new TitledPane();
		tPane.setText("Personal");
		tPane.setOnMouseClicked(new FoldersEventHandler());
		tPane.setCollapsible(false);
		tPane.setExpanded(false);
		folders.getChildren().add(tPane);
		// Generate user defined folders
		/* We will iterate on the user folders and add them*/
		inboxTPane.setContent(folders);
		bannerLbl.setText("Inbox Clicked");
	}
	
	/*
	 * Event for handling user accessing folders
	 * */
	private class FoldersEventHandler implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
           System.out.println(((TitledPane)evt.getSource()).getText() + " Is Clicked");
           app.setViewingOptions(new Folder(((TitledPane)evt.getSource()).getText()), null, null);
           showMails(1);
           // Here We call setViewOptions at the current selected folder and then show mails
        }
    }
	
	/*
	 * Load mails in the user sent folder
	 * */
	public void loadSent() {
		// SetviewingOption
		app.setViewingOptions(new Folder("."), null, null);
		// Show mails
		bannerLbl.setText("Sent Clicked");
	}
	
	/*
	 * Load mails in the user trash folder
	 * */
	public void loadTrash() {
		// SetviewingOption
		// Show mails
	}
	
	/*
	 * Handle page changing event
	 * */
	public void pageSelection() {
		showMails(pgr.getCurrentPageIndex()+1);
	}
	
	
	public void sortBtnClicked() {
		// Sorting type
		if (SortAttribute.valueOf(sortChoiceBox.getSelectionModel().getSelectedItem()) == SortAttribute.Date) {
			app.setViewingOptions(null, null, new Sort(SortAttribute.Date));
		}
		else if (SortAttribute.valueOf(sortChoiceBox.getSelectionModel().getSelectedItem()) == SortAttribute.Title) {
			app.setViewingOptions(null, null, new Sort(SortAttribute.Title));
		}
		else if (SortAttribute.valueOf(sortChoiceBox.getSelectionModel().getSelectedItem()) == SortAttribute.SenderName) {
			app.setViewingOptions(null, null, new Sort(SortAttribute.SenderName));
		}
		// Reverse Sorting
		app.reverseSort(reverseChkBox.isSelected());
		// Load the mails of the first page after sorting
		showMails(1);
	}
	
	/*bannerLbl.setText("Hello Again");
	ObservableList<MailHeader> hh = mailsTbl.getSelectionModel().getSelectedItems();
	Iterator<MailHeader> it = hh.iterator(); 
	while (it.hasNext()) {
		MailHeader tmpHeader = it.next(); 
		System.out.println(tmpHeader.getTitle());
		System.out.println(tmpHeader.getID());
	}*/
	
	public void priorityBtnClicked() {
		// Call function in app class to sort loaded mails by priority
		app.SortByPriority();
		app.reverseSort(false);
		// Show mails in the first page
		showMails(1);
	}
	
	public void searchBtnClicked() {
		// Searching I recommend using filters
		
		// Show mails in the first page
		showMails(1);
	}
	
	public void openMail() {
		MailHeader MH = mailsTbl.getSelectionModel().getSelectedItem();
		// We have mail ID then we can load it in the viewMail scene
		
		
		System.out.println("Hello");
	}
	
	public void refresh() {
		app.loadMails();
		sortBtnClicked();
		showMails(1);
	}
	
	public void composeBtnClicked() {
		AnchorPane pane = null;
		Parent root;
        
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Compose.fxml"));
        	try {
				root = loader.load();
				ComposeController composeController = loader.getController();
	        	Stage stage = new Stage();
	            stage.setTitle("Compose");
	            stage.setScene(new Scene(root, 948, 500));
	            stage.setOnCloseRequest(event -> {composeController.draft();} );
	            stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            
            
        
        
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
