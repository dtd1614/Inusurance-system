package ui;

import domain.customer.Customer;
import domain.Employee;
import enumeration.employee.Department;
import enumeration.employee.Rank;
import exception.DataDuplicationException;
import exception.NoDataException;
import service.ServiceContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LoginUi {

	private final ServiceContainer serviceContainer;
	private final BufferedReader userInput;
	
	public LoginUi(ServiceContainer serviceContainer, BufferedReader userInput) throws RemoteException {
		this.serviceContainer = serviceContainer;
		this.userInput = userInput;

		try {
			this.serviceContainer.getCustomerService().registerCustomer(new Customer("1","1","1","1",1,"1",true,true));
			this.serviceContainer.getEmployeeService().registerEmployee(new Employee("1","1",Department.InsuranceDeveloper,"1","1",1,Rank.AssisantManager));
			this.serviceContainer.getEmployeeService().registerEmployee(new Employee("2","1",Department.InsuranceManager,"1","1",1,Rank.AssisantManager));
			this.serviceContainer.getEmployeeService().registerEmployee(new Employee("3","1",Department.UW,"1","1",1,Rank.AssisantManager));
			this.serviceContainer.getEmployeeService().registerEmployee(new Employee("4","1",Department.Salesperson,"1","1",1,Rank.AssisantManager));
			this.serviceContainer.getEmployeeService().registerEmployee(new Employee("5","1",Department.CompensationManager,"1","1",1,Rank.AssisantManager));
		} catch (DataDuplicationException e) {
			throw new RuntimeException(e);
		}
	}

	public void printMenu() throws IOException {
		while(true) {
			System.out.println("******************** 로그인 창 *********************");
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("x. 종료");
			switch(userInput.readLine().trim()) {
				case "1" : printLoginMenu(); break;
				case "2" : printRegisterMenu(); break;
				case "x" : System.exit(0);
				default: System.err.println("잘못된 입력입니다.");
			}
        }
	}

	private void printLoginMenu() throws IOException {
		while(true) {
			System.out.println("******************** 로그인 메뉴 *********************");
			System.out.println("사용자 유형을 선택하세요.");
			System.out.println("1. 고객");
			System.out.println("2. 직원");
			System.out.println("0. 뒤로가기");
			switch(userInput.readLine().trim()) {
				case "1" : printCustomerLoginForm(); return;
				case "2" : printEmployeeLoginForm(); return;
				case "0" : return;
				default: System.err.println("잘못된 입력입니다.");
			}	
		}
	}
	
	private void printEmployeeLoginForm() throws IOException {
		System.out.println("아이디와 비밀번호를 입력하세요.");
		System.out.print("아이디 : ");
		String id = userInput.readLine().trim();
		if(id.contains(" ")||id.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("비밀번호 : ");
		String password = userInput.readLine().trim();
		if(password.contains(" ")||password.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		Employee employee = null;
		try {employee = serviceContainer.getEmployeeService().loginEmployee(id, password);}
		catch (NoDataException e) {System.err.println(e.getMessage()); return;}
		System.out.println("로그인에 성공하였습니다!");
		switch(employee.getDepartment()) {
			case InsuranceDeveloper : new InsuranceDeveloperUi(employee.getId(), serviceContainer, userInput).printMenu(); break;
			case InsuranceManager : new InsuranceManagerUi(employee.getId(), serviceContainer, userInput).printMenu(); break;
			case UW : new UWUi(employee.getId(), serviceContainer, userInput).printMenu(); break;
//			case Salesperson : new SalespersonUi(employee.getId(), serviceContainer, userInput).printMenu(); break;
//			case CompensationManager: new CompensationManagerUi(employee.getId(), serviceContainer, userInput).printMenu(); break;

		}
	}

	private void printCustomerLoginForm() throws IOException {
		System.out.println("아이디와 비밀번호를 입력하세요.");
		System.out.print("아이디 : ");
		String id = userInput.readLine().trim();
		if(id.contains(" ")||id.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("비밀번호 : ");
		String password = userInput.readLine().trim();
		if(password.contains(" ")||password.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		Customer customer = null;
		try {customer = serviceContainer.getCustomerService().loginCustomer(id, password);}
		catch (NoDataException e) {System.err.println(e.getMessage());return;}
		System.out.println("로그인에 성공하였습니다!");
		new CustomerUi(customer.getId(), serviceContainer, userInput).printMenu();
	}

	private void printRegisterMenu() throws IOException {
		while(true) {
			System.out.println("******************** 회원가입 메뉴 *********************");
			System.out.println("사용자 유형을 선택하세요.");
			System.out.println("1. 고객");
			System.out.println("2. 직원");
			System.out.println("0. 뒤로가기");
			switch(userInput.readLine().trim()) {
				case "1" : printCustomerRegisterForm(); return;
				case "2" : printEmployeeRegisterForm(); return;
				case "0" : return;
				default: System.err.println("잘못된 입력입니다.");
			}
		}
	}
	
	private void printCustomerRegisterForm() throws IOException {
		System.out.println("고객 회원가입 양식을 입력하세요.");
		System.out.print("아이디 : ");
		String id = userInput.readLine().trim();
		if(id.contains(" ")||id.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("비밀번호 : ");
		String password = userInput.readLine().trim();
		if(password.contains(" ")||password.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("이름 : ");
		String name = userInput.readLine().trim();
		if(name.contains(" ")||name.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("이메일 : ");
		String email = userInput.readLine().trim();
		if(email.contains(" ")||email.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("전화번호 : ");
		int phoneNumber;
	    try{phoneNumber = Integer.parseInt(userInput.readLine());} 
	    catch(NumberFormatException e) { System.out.println("잘못된 입력입니다.");  return;}
		System.out.print("주소 : ");
		String address = userInput.readLine().trim();
		if(address.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.println("주택을 소유 혹은 임대하고 계십니까? (y/n)");
		boolean hasHome;
		switch(userInput.readLine().trim()) {
			case "y" :  hasHome = true; break;
			case "n" :  hasHome = false; break;
			default : System.err.println("잘못된 입력입니다."); return;
		}
		boolean hasWorkplace;
		System.out.println("사업장을 소유하고 계십니까? (Y/N)");
		switch(userInput.readLine().trim()) {
			case "y" :  hasWorkplace = true; break;
			case "n" :  hasWorkplace = false; break;
			default : System.err.println("잘못된 입력입니다."); return;
		}
		Customer customer = new Customer(id, password, name, email, phoneNumber, address, hasHome, hasWorkplace);
		try {serviceContainer.getCustomerService().registerCustomer(customer); }
		catch (DataDuplicationException e) {System.err.println(e.getMessage()); return;}
		System.out.println("회원가입이 완료되었습니다.");
	}
	
	private void printEmployeeRegisterForm() throws IOException {
		System.out.println("부서를 선택하세요.");
		for(int i = 0; i < Department.values().length; i++) {
			System.out.println((i + 1) + ". " + Department.values()[i].getName());
		}
		Department department;
		switch(userInput.readLine().trim()) {
			case "1" :  department = Department.values()[0]; break;
			case "2" :  department = Department.values()[1]; break;
			case "3" :  department = Department.values()[2]; break;
			case "4" :  department = Department.values()[3]; break;
			case "5" :  department = Department.values()[4]; break;
			default : System.err.println("잘못된 입력입니다."); return;
		}
		System.out.println("직급를 선택하세요.");
		for(int i = 0; i < Rank.values().length; i++) {
			System.out.println((i + 1) + ". " + Rank.values()[i].getName());
		}
		Rank rank;
		switch(userInput.readLine().trim()) {
			case "1" :  rank = Rank.values()[0]; break;
			case "2" :  rank = Rank.values()[1]; break;
			case "3" :  rank = Rank.values()[2]; break;
			case "4" :  rank = Rank.values()[3]; break;
			case "5" :  rank = Rank.values()[4]; break;
			case "6" :  rank = Rank.values()[5]; break;
			default : System.err.println("잘못된 입력입니다."); return;
		}
		System.out.println("직원 회원가입 양식을 입력하세요.");
		System.out.print("아이디 : ");
		String id = userInput.readLine().trim();
		if(id.contains(" ")||id.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("비밀번호 : ");
		String password = userInput.readLine().trim();		
		if(password.contains(" ")||password.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("이름 : ");
		String name = userInput.readLine().trim();
		if(name.contains(" ")||name.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("이메일 : ");
		String email = userInput.readLine().trim();
		if(email.contains(" ")||email.isEmpty()) {System.err.println("잘못된 입력입니다."); return;}
		System.out.print("전화번호 : ");
		int phoneNumber;
	    try{phoneNumber = Integer.parseInt(userInput.readLine());} 
	    catch(NumberFormatException e) { System.err.println("잘못된 입력입니다.");  return;}
		Employee employee = new Employee(id, password, department, name, email, phoneNumber, rank);
		try {serviceContainer.getEmployeeService().registerEmployee(employee);}
		catch (DataDuplicationException e) {System.err.println(e.getMessage()); return;}
		System.out.println("회원가입이 완료되었습니다.");
	}
}
