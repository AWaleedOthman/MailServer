package Sorts;

import java.util.Comparator;

import Classes.Mail;
import Interfaces.ISort;

public class senderNameSort implements ISort {
	@Override
	public Comparator sortAttribute() {
		return new SenderNameOrder();
    }

    private class SenderNameOrder implements Comparator {
    	
    	@Override
 	   	public int compare(Object o1, Object o2) { 
	    	String name1 = ((Mail)o1).getSenderName();
	    	String name2 = ((Mail)o2).getSenderName();
		 		
		    int l1 = name1.length(); 
	        int l2 = name2.length(); 
	        int lmin = Math.min(l1, l2); 
	  
	        for (int i = 0; i < lmin; i++) { 
	            int name1_ch = (int)Character.toLowerCase(name1.charAt(i)); 
	            int name2_ch = (int)Character.toLowerCase(name2.charAt(i)); 
	  
	            if (name1_ch != name2_ch) { 
	                return name1_ch - name2_ch; 
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
