import java.util.Comparator;

public class CalendarEvent implements Comparable<CalendarEvent>{
	private String startTime;
	private String endTime;
	private String description;
	private int year;
	private int date;
	private int month;

	/**
	 * Construct a new event 
	 * @param date - date of the event
	 * @param startTime - start time of the event
	 * @param endTime - end time of the event
	 * @param description - event's description
	 */
	public CalendarEvent(String date, String startTime, String endTime, String description) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		int index = date.indexOf("/");
		this.month = Integer.parseInt(date.substring(0, index));
		this.date = Integer.parseInt(date.substring(index+1, date.indexOf("/", index+1)));
		index = date.indexOf("/", index+1);
		this.year =Integer.parseInt(date.substring(index+1));
	}

	/**
	 * Construct new event without end time
	 * @param startTime - start time of the event
	 * @param description - event's description
	 */
	//	public Event(String startTime, String description){
	//		this(startTime, "", description);
	//	}

	/**
	 * get the start time of the event
	 * @return the event's start time
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * get the end time of the event
	 * @return the event's start time
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * get the event's description
	 * @return the event's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * get the year of the event
	 * @return year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * get the date of the event
	 * @return date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * get the month of the event
	 * @return month
	 */
	public int getMonth() {
		return month;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(endTime.equals(""))
			return month + "/" + date + "/" + year + ": " + startTime + " " + description;
		return month + "/" + date + "/" + year + ": " + startTime + " - " + endTime + " " + description;
	}

	@Override
	public int compareTo(CalendarEvent o) {
		// TODO Auto-generated method stub
		String temp = "" + this.year + this.month + this.date;
		int date = Integer.parseInt(temp);
		String temp1 = "" + o.year + o.month + o.date;
		int date1 = Integer.parseInt(temp1);
		int comp = Integer.compare(date, date1); // compare calue
		if(comp == 0)
		{
			int start1 = Integer.parseInt(this.startTime.replace(":", "").trim());
			int start2 = Integer.parseInt(o.startTime.replace(":", "").trim());
			comp = Integer.compare(start1, start2);
			if(comp == 0)
				comp = this.description.compareTo(o.description);
		}
		return comp;
	}

	/**
	 * check if the events have time conflict
	 * @param e - the other event to compare with
	 * @return true if the event are conflicted, else otherwise
	 */
	public boolean conflict(CalendarEvent e){
		if(this.startTime.equals(e.startTime) && this.endTime.equals(e.endTime)){
			return true;
		}
		else
			return false;
	}

}
