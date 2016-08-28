
public class SimpleCalendar {
	public static void main(String[] args){
		MyCalendar c = new MyCalendar();
		CalendarView view = new CalendarView(c);
		c.attach(view);
	}
}
