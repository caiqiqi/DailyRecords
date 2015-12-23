import java.util.Calendar;

public class CalendarMessage {
	
	int year = -1;
	int month = -1;
	int day = -1;
	
	/*为了确保一个月的每一天都显示在“周几”中，用了42个格子*/
	public static final int CELL_LENGTH = 6 *7;
	private static final String TAG = "CalendarMessage :" ;

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setMonth(int month) {
		if (month <= 12 && month >= 1)
			this.month = month;
		else
			this.month = 1;
	}

	public int getMonth() {
		return month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public String[] getMonthCalendar() {
		
		//day是一个String[]类型，是到时候等着被显示到日历版上的
		String[] dayShownInTheCalendarPad = new String[CELL_LENGTH];
		
		Calendar calendar = Calendar.getInstance();
		
		//public final void set(int year, int month, int date, int hourOfDay, int minute)
		// 将日历翻到year年month月1日,注意0表示一月...11表示十二月,最后一个参数minute没有写
		calendar.set(year, month - 1, 1);

		// dayInTheWeek用来显示今天（程序运行的当天）是星期几
		int dayInTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int dayAmountInTheMonth = getDayAmountInTheMonth();
		
		/*这里是在准备dayShownInTheCalendarPad，准备好之后，显示在CalendarPad上面*/
		
		// dayInTheWeek前面的 
		// 统统设置为""
	   for (int i = 0; i < dayInTheWeek; i++)
			dayShownInTheCalendarPad[i] = "";
	   
	   // dayInTheWeek与  dayInTheWeek + dayAmountInTheMonth之间的，
	   // 从1开始设置起
		for (int i = dayInTheWeek, n = 1; i < dayInTheWeek+ dayAmountInTheMonth; i++) {
			//因为要将数字转化为String类型，显示在CalendaPad上
			dayShownInTheCalendarPad[i] = String.valueOf(n);
			n++;
		}
		// dayInTheWeek + dayAmountInTheMonth到42之间的
		// 也设置为 ""
		for (int i = dayInTheWeek+ dayAmountInTheMonth; i < 42; i++)
			dayShownInTheCalendarPad[i] = "";
		
		return dayShownInTheCalendarPad;
	}

	/**
	 * 获取给定某年某月的天数(与当前对象中的 year和 month的值有关)
	 * @return
	 */
	private int getDayAmountInTheMonth() {
		
		int dayAmount = 0;
		
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12)
			dayAmount = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			dayAmount = 30;
		if (month == 2)
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
				dayAmount = 29;
			else
				dayAmount = 28;
		
		return dayAmount;
	}
}
