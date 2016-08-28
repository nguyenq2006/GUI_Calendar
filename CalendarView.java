
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CalendarView extends JFrame implements ChangeListener{
    private final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
    "October", "November", "December" };
    private MyCalendar c;
    private Calendar caln;
    private JTable table;
    private JLabel month;
    private JLabel currentDate;
    private JTextArea eventsList;
    
    /**
     * @param c - the object of MyCanlendar
     */
    public CalendarView(MyCalendar c) {
        //setting up the JFrame and the calendar
        this.caln = new GregorianCalendar();
        this.c = c;
        setLayout(new BorderLayout());
        setTitle("Simple Calendar");
        setSize(600, 600);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        
        //move back and forth in the calendar
        JPanel subPanel = new JPanel();
        JButton prev = new JButton("<");
        prev.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                caln.add(Calendar.DAY_OF_MONTH, -1);
                updateCalendar(c.printCalendar(caln));
            }
        });
        JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                caln.add(Calendar.DAY_OF_MONTH, 1);
                updateCalendar(c.printCalendar(caln));
            }
        });
        subPanel.add(prev);
        subPanel.add(next);
        
        //create new event
        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField eventName = new JTextField("Untiled Event");
                String d = caln.get(Calendar.MONTH)+1 + "/" + caln.get(Calendar.DAY_OF_MONTH) + "/" + caln.get(Calendar.YEAR);
                JTextField eventDate = new JTextField(d);
                JTextField startTime = new JTextField("9:00");
                JTextField endTime = new JTextField("10:00");
                Object[] message = {eventName, eventDate, startTime, endTime};
                int i = JOptionPane.showConfirmDialog(null, message, "New Event", JOptionPane.OK_CANCEL_OPTION);
                if(i == JOptionPane.OK_OPTION){
                    String description	= eventName.getText();
                    String date = eventDate.getText();
                    String start = startTime.getText();
                    String end = endTime.getText();
                    CalendarEvent newEvent = new CalendarEvent(date, start, end, description);
                    if(!c.create(newEvent)){
                    	JOptionPane.showMessageDialog(null, "Time Conflict");
                    }
                    	
                }
            }
        });
        
        //quit and save the program
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                c.quit();
                System.exit(0);
            }
        });
        
        p.add(create, BorderLayout.WEST);
        p.add(subPanel, BorderLayout.CENTER);
        p.add(quit, BorderLayout.EAST);
        
        //showing the month view
        JPanel cPanel = new JPanel(new BorderLayout());
        String[] dayOfWeek = {"S","M","T","W","T","F","S"};
        table = new JTable(7, 7);
        table.setDefaultEditor(Object.class, null);
        for(int i = 0; i < table.getColumnCount(); i++){
            table.setValueAt(dayOfWeek[i], 0, i);
            table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
        month = new JLabel(MONTHS[caln.get(Calendar.MONTH)] + " " + caln.get(Calendar.YEAR));
        month.setHorizontalAlignment(JLabel.CENTER);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub
                JTable t = (JTable) e.getSource();
                if((t.getValueAt(t.getSelectedRow(), t.getSelectedColumn())) != null && t.getSelectedRow() != 0){
                    caln.set(Calendar.DAY_OF_MONTH, (int) (t.getValueAt(t.getSelectedRow(), t.getSelectedColumn())) );
                    updateCalendar(c.printCalendar(caln));
                }
            }
        });
        
        cPanel.add(month, BorderLayout.NORTH);
        cPanel.add(table, BorderLayout.CENTER);
        
        //showing the day view
        JPanel dPanel = new JPanel(new BorderLayout());
        this.currentDate = new JLabel("", JLabel.CENTER);
        this.eventsList = new JTextArea();
        eventsList.setLineWrap(true);
        eventsList.setEditable(false);
        JScrollPane scroll = new JScrollPane(eventsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                             JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dPanel.add(currentDate, BorderLayout.NORTH);
        dPanel.add(scroll, BorderLayout.CENTER);
        
        add(cPanel, BorderLayout.WEST);
        add(p, BorderLayout.NORTH);
        add(dPanel, BorderLayout.CENTER);
        
        setVisible(true);
        //		pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        updateCalendar(c.printCalendar(caln));
        
    }
    
    /**
     * @param monthLayout - the double array of the month layout
     */
    public void updateCalendar(int[][] monthLayout) {
        //update month view
        month.setText(MONTHS[caln.get(Calendar.MONTH)] + " " + caln.get(Calendar.YEAR));
        int date = 0;
        for(int i = 1; i < table.getRowCount(); i++){
            for(int j = 0; j < table.getColumnCount(); j++){
                date = monthLayout[i-1][j];
                if(date == 0)
                    table.setValueAt(null, i, j);
                else
                    table.setValueAt(date, i, j);
                if(date == caln.get(Calendar.DAY_OF_MONTH))
                    table.changeSelection(i, j, false, false);
            }
        }
        
        String temp = "" + caln.get(Calendar.YEAR) + (caln.get(Calendar.MONTH)+1) + caln.get(Calendar.DAY_OF_MONTH);
        int key = Integer.parseInt(temp);
        ArrayList<String> dayEvents = c.eventList(key, caln);
        this.currentDate.setText(dayEvents.get(0));
        String dayDescription = "";
        for(int i = 1; i < dayEvents.size(); i++){
            dayDescription += "\n" + dayEvents.get(i);
        }
        this.eventsList.setText(dayDescription);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        updateCalendar(c.printCalendar(caln));
    }
}
