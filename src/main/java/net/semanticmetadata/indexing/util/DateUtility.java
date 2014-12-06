package net.semanticmetadata.indexing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateUtility {

	
	
	 public static String toUtcDate(final Object dateStr) {
	        if (dateStr == null) {
	            return null;
	        }
	        final SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	        // Add other parsing formats to try as you like:
	        final String[] dateFormats = { "dd-MMM-yyyy H:mm:ss", "yyyy-MM-dd", "MMM dd, yyyy hh:mm:ss Z" };
	        for (final String dateFormat : dateFormats) {
	            try {

	                return out.format(new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateStr.toString()));
	            } catch (final ParseException ignore) {
	                System.out.println(ignore);
	            }
	        }
	        throw new IllegalArgumentException("Invalid date: " + dateStr);
	    }
}
