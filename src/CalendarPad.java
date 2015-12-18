import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * 左上角显示日期的那个框框
 *
 */
public class CalendarPad extends JPanel{
	
	private static final long serialVersionUID = 865985664708897713L;
	
	int year, month, day;
	
    CalendarMessage calendarMessage;           
    JTextField [] jTF_showDay;
    /*某一月的每一天用JLabel表示*/
    JLabel jL_title[];                   
    String [] 星期={"SUN日","MON一","TUE二","WED三","THU四","FRI五","SAT六"};
    
    JPanel jP_north;
    JPanel jP_center;
    
    public CalendarPad(){
    	
       setLayout(new BorderLayout());
       jP_north=new JPanel();
       jP_north.setLayout(new GridLayout(1,7));
       jP_center=new JPanel(); 
       jP_center.setLayout(new GridLayout(6,7));   
       add(jP_center,BorderLayout.CENTER);  
       add(jP_north,BorderLayout.NORTH);
       jL_title=new JLabel[7];
       
       for(int j=0;j<7;j++){                         
         jL_title[j]=new JLabel();
         jL_title[j].setFont(new Font("TimesRoman",Font.BOLD,12));
         jL_title[j].setText(星期[j]);
         jL_title[j].setHorizontalAlignment(JLabel.CENTER);
         jL_title[j].setBorder(BorderFactory.createRaisedBevelBorder());
         jP_north.add(jL_title[j]);
       } 
       jL_title[0].setForeground(Color.red);
       jL_title[6].setForeground(Color.blue);
    }
    public void setShowDayTextField(JTextField [] text){
       jTF_showDay=text;
       for(int i=0;i<jTF_showDay.length;i++){                        
         jTF_showDay[i].setFont(new Font("TimesRoman",Font.BOLD,15));
         jTF_showDay[i].setHorizontalAlignment(JTextField.CENTER);
         jTF_showDay[i].setEditable(false);
         jP_center.add(jTF_showDay[i]);
       }
       
    }
    public void setCalendarMessage(CalendarMessage calendarMessage){
       this.calendarMessage=calendarMessage;
    } 
    public void showMonthCalendar(){
       String [] a=calendarMessage.getMonthCalendar();
       for(int i=0;i<42;i++)                        
          jTF_showDay[i].setText(a[i]);
       validate();  
    }
}  
