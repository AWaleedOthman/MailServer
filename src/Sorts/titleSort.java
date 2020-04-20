package Sorts;

import Classes.MailServer.Mail;
import Interfaces.MailServer.ISort;

import java.util.Comparator;

public class titleSort implements ISort {
	@Override
	public Comparator sortAttribute() {
		return new TitleOrder();
    }

    private class TitleOrder implements Comparator {
    	
    	@Override
 	   	public int compare(Object o1, Object o2) { 
	    	String ttl1 = ((Mail)o1).getTitle();
	    	String ttl2 = ((Mail)o2).getTitle();
		 		
		    int l1 = ttl1.length(); 
	        int l2 = ttl2.length(); 
	        int lmin = Math.min(l1, l2); 
	  
	        for (int i = 0; i < lmin; i++) { 
	            int name1_ch = (int)Character.toLowerCase(ttl1.charAt(i)); 
	            int name2_ch = (int)Character.toLowerCase(ttl2.charAt(i)); 
	  
	            if (name1_ch != name2_ch) { 
	                return name1_ch - name2_ch;
	                //      65 a  -  68 d = -3 
	            } 
	        } 
		  
		    // Edge case for strings when one word is sub of the other 
		        if (l1 != l2) { 
		            return l1 - l2; 
		        } 
		  
		    // If none of the above conditions is true, 
		    // it implies both the strings are equal 
		    else { 
		        return 0; 
		    }
    	}
    }
}
