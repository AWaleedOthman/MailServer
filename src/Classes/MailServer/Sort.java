package Classes;

import java.util.Comparator;

import Interfaces.ISort;

public class Sort implements ISort {

	private SortAttribute sortType;
	public Sort (SortAttribute sortType) {
		this.sortType = sortType;
	}
	
	public Comparator<Object> sortAttribute() {
		if (sortType == SortAttribute.Date) {
			return new DateOrder();
		}
		else if (sortType == SortAttribute.Title) {
			return new TitleOrder();
		}
		else if (sortType == SortAttribute.SenderName) {
			return new SenderNameOrder();
		}
		return null;
    }
	
	private class DateOrder implements Comparator<Object> {
    	
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
	
	private class SenderNameOrder implements Comparator<Object> {
    	
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
	
	private class TitleOrder implements Comparator<Object> {
    	
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
