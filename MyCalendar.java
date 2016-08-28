import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


enum MONTHS {
	January, February, March, April, May, June, July, August, September, October, November, December;
}

enum DAYS {
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
}

public class MyCalendar {
	private boolean isLeap;
	private final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private TreeMap<Integer, TreeSet<CalendarEvent>> calendar;
	private Scanner in;
	private ArrayList<ChangeListener> listeners;

	/**
	 * construct a new calendar
	 */
	public MyCalendar() {
		this.calendar = new TreeMap<>();
		this.listeners = new ArrayList<ChangeListener>();
		load();
	}

	/**
	 * @param month - the month of the year	
	 * @return number of days in a month
	 */
	public int daysPerMonth(Calendar c, int month){
		if(((GregorianCalendar) c).isLeapYear(c.get(Calendar.YEAR)))
			return DAYS_PER_MONTH[month] + 1;
		return DAYS_PER_MONTH[month];
	}

	/**
	 * print calendar of the month
	 * @param c - an Calendar object
	 */
	public int[][] printCalendar(Calendar c) {
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		int days = DAYS_PER_MONTH[c.get(Calendar.MONTH)];
		int month = temp.get(Calendar.MONTH);
		int year = temp.get(Calendar.YEAR);
		int firstDay = temp.get(Calendar.DAY_OF_WEEK) - 1;
		int[][] monthLayout	= new int[6][7];
		if (((GregorianCalendar) c).isLeapYear(c.get(Calendar.YEAR)) && month == 1)	days++;
		int date = 1;
		for (int i = 0; i < monthLayout.length && date <= days; i++) {
			for(int j = 0; j < monthLayout[i].length && date <= days; j++){
				if (date == 1) {
					j = firstDay;
				}
				monthLayout[i][j] = date;
				date++;
			}
		}
		return monthLayout;
	}

	/**
	 * load all the events from the text file
	 */
	public void load() {
		try {
			Scanner readFile = new Scanner(new File("hw4/eventsList.txt"));
			///
			while (readFile.hasNextLine()) {
				String line = readFile.nextLine();
				if(!line.equals("")){
					int startIndex = 0;
					int endIndex = line.indexOf(":");
					String date = line.substring(startIndex, endIndex).trim();
					String startTime = "";
					String endTime = "";
					startIndex = endIndex + 2;
					if (line.contains("-")) {
						endIndex = line.indexOf("-");
						startTime = line.substring(startIndex, endIndex).trim();
						startIndex = endIndex + 2;
						endIndex = line.indexOf(" ", startIndex);
						endTime = line.substring(startIndex, endIndex).trim();
					}
					else{
						endIndex = line.indexOf(" ", startIndex);
						startTime = line.substring(startIndex, endIndex).trim();
					}
					String description = line.substring(endIndex + 1).trim();
					CalendarEvent event = new CalendarEvent(date, startTime, endTime, description);
					int key = Integer.parseInt(""+event.getYear()+event.getMonth()+event.getDate());
					TreeSet<CalendarEvent> temp = new TreeSet<>();
					if (calendar.containsKey(key)){
						temp = calendar.get(key);
					}
					temp.add(event);
					calendar.put(key, temp);
				}
			}
			readFile.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * create a new event
	 * @param event - the object of CalendarEvent
	 */
	public boolean create(CalendarEvent event){
		int date = Integer.parseInt("" + event.getYear() + event.getMonth() + event.getDate());
		TreeSet<CalendarEvent> temp = new TreeSet<>();
		boolean conflict = false;
		if(calendar.containsKey(date)){
			temp = calendar.get(date);
			for(CalendarEvent e : temp)
				conflict = e.conflict(event);
			if (!conflict)
				temp.add(event);
		}
		else temp.add(event);
		calendar.put(date, temp);
		for(ChangeListener l : listeners){
			l.stateChanged(new ChangeEvent(this));
		}
		return !conflict;
	}

	/**
	 * quit the program and save all the events to the file
	 */
	public void quit() {
		try{
			PrintStream out = new PrintStream("hw4/eventsList.txt");
			for(int key : calendar.keySet())
			{
				TreeSet<CalendarEvent> events = calendar.get(key);
				for(CalendarEvent e : events)
				{
					out.println(e.toString());
				}
			}
			out.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @param date - the date of the month
	 * @param c - the Calendar object
	 * @return - the list of the events in the days
	 */
	public ArrayList<String> eventList(int date, Calendar c){
		MONTHS[] months = MONTHS.values();
		DAYS[] dayOfWeek = DAYS.values();
		ArrayList<String> eventList = new ArrayList<>();
		eventList.add(dayOfWeek[c.get(Calendar.DAY_OF_WEEK)-1] + " " + months[c.get(Calendar.MONTH)] 
				+ " " + c.get(Calendar.DAY_OF_MONTH) + ", " + c.get(Calendar.YEAR));
		TreeSet<CalendarEvent> temp = calendar.get(date);
		if(temp != null){
			for(CalendarEvent e : temp){
				if(e.getEndTime().equals(""))
					eventList.add(e.getStartTime() + " " + e.getDescription());
				else
					eventList.add(e.getStartTime() + "-" + e.getEndTime() + ": " + e.getDescription());
			}
		}
		return eventList;
	}

	/**
	 * @param l - change listener
	 */
	public void attach(ChangeListener l){
		this.listeners.add(l);
	}
}