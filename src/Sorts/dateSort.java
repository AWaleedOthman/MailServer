package Sorts;

import Classes.MailServer.Mail;
import Interfaces.MailServer.ISort;

import java.util.Comparator;

public class dateSort implements ISort {

	@Override
	public Comparator sortAttribute() {
		return new DateOrder();
    }

    private class DateOrder implements Comparator {
    	
    	@Override
 	   public int compare(Object o1, Object o2) { 
    	Mail mail1 = (Mail)o1;
 		Mail mail2 = (Mail)o2;
 		// We sort from the oldest to the newest
		if (mail1.getDate().compareTo(mail2.getDate()) > 0) {
			return 1;
		}
		else if (mail1.getDate().compareTo(mail2.getDate()) < 0) {
			return -1;
		}
		return 0;
 	   }
	}

}
