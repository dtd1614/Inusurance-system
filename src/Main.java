import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import enumeration.employee.Department;
import service.ServiceContainer;
import ui.LoginUi;
public class Main {
	public static void main(String[] args) {
try {
	LoginUi loginUI = new LoginUi(new ServiceContainer(), new BufferedReader(new InputStreamReader(System.in)));
	loginUI.printMenu();
} catch (IOException e) {
	throw new RuntimeException(e);
}
	}
}
