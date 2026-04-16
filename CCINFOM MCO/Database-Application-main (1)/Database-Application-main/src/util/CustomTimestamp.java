
package util;

public class CustomTimestamp {
	private int year;
	private int month;
	private int day;
	
	public CustomTimestamp(int year, int month, int day) {
		this.year = year;
		this.month = month; 
		this.day = day;
	}
	
	public void displayDate() { 
		System.out.println(year + "/" + month + "/" + day);
	}

	public String getDisplayDate() {
		 String monthName = "";
		 switch(month) {
			  case 1:  monthName = "January";   break;
			  case 2:  monthName = "February";  break;
			  case 3:  monthName = "March";     break;
			  case 4:  monthName = "April";     break;
			  case 5:  monthName = "May";       break;
			  case 6:  monthName = "June";      break;
			  case 7:  monthName = "July";      break;
			  case 8:  monthName = "August";    break;
			  case 9:  monthName = "September"; break;
			  case 10: monthName = "October";   break;
			  case 11: monthName = "November";  break;
			  case 12: monthName = "December";  break;
			  default: monthName = "Unknown";   break;
		 }

		 return monthName + " " + day + ", " + year;
	}

	public String toStringDate() {
		return year + "-" + month + "-" + day;	
	}

	public int getYear() { return year; }
	public int getMonth() { return month; }
	public int getDay() { return day; }

	public void setYear(int year) { this.year = year; }
	public void setMonth(int month) { this.month = month; }
	public void setDay(int day) { this.day = day; }
}
