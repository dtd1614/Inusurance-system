package ui;
import domain.Insurance;
import domain.calculationFormula.CalculationFormula;
import domain.calculationFormula.HomeFormula;
import domain.calculationFormula.WorkplaceFormula;
import enumeration.calculationFormula.OutwallType;
import enumeration.calculationFormula.PillarType;
import enumeration.calculationFormula.RiskLevel;
import enumeration.calculationFormula.RoofType;
import enumeration.calculationFormula.homeFormula.HomeCompensation;
import enumeration.calculationFormula.homeFormula.HomeSquareMeter;
import enumeration.calculationFormula.homeFormula.HouseType;
import enumeration.calculationFormula.homeFormula.ResidenceType;
import enumeration.calculationFormula.workplaceFormula.Floor;
import enumeration.calculationFormula.workplaceFormula.WorkplaceCompensation;
import enumeration.calculationFormula.workplaceFormula.WorkplaceSquareMeter;
import enumeration.calculationFormula.workplaceFormula.WorkplaceUsage;
import enumeration.insurance.InsuranceStatus;
import exception.EmptyListException;
import exception.NoDataException;
import exception.TimeDelayException;
import service.ServiceContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map.Entry;
public class InsuranceManagerUi {
    private final String employeeId;
    private final ServiceContainer serviceContainer;
    private final BufferedReader userInput;
    public InsuranceManagerUi(String employeeId, ServiceContainer serviceContainer, BufferedReader userInput) {
        this.employeeId = employeeId;
        this.serviceContainer = serviceContainer;
        this.userInput = userInput;
    }
	public void printMenu() throws IOException{
		while(true) {
			System.out.println("******************** 상품관리자 메인 메뉴 *********************");
			System.out.println("1. 인가하기");
			System.out.println("0. 로그아웃");
			System.out.println("x. 종료");
			switch(userInput.readLine().trim()) {
				case "1" : printExamineMenu();  break;
				case "0" : return;
				case "x" : System.exit(0);
				default : System.out.println("! 잘못된 입력입니다.");
			}
		}
	}
	private void printExamineMenu() throws IOException{
	    while(true) {
			ArrayList<Insurance> insuranceList;
			try {insuranceList = serviceContainer.getInsuranceService().getInsuranceList(InsuranceStatus.UnderAuthorize);}
			catch (EmptyListException | TimeDelayException e) {System.out.println(e.getMessage()); return;}
			System.out.println("******************** 상품 인가 메뉴 *********************");
	        System.out.println("인가할 상품의 아이디를 입력하세요. 뒤로가려면 0을 입력하세요.");
			System.out.println("아이디\t이름\t유형");
			for (Insurance insurance : insuranceList) {
				System.out.println(insurance.getId() 
						+ "\t" + insurance.getName() 
						+ "\t" + insurance.getType().getName());
			}
			System.out.print("상품 아이디 : ");
			int id;
		    try {id = Integer.parseInt(userInput.readLine().trim());}
			catch (NumberFormatException e) {System.out.println("! 잘못된 입력입니다."); continue;}
		    if(id==0) return;
		    Insurance selectedInsurance = null;
		    for(Insurance insurance:insuranceList) {if(insurance.getId()==id)selectedInsurance = insurance;}
		    if(selectedInsurance==null){System.out.println("! 잘못된 입력입니다."); continue;}
		    printInsuranceDetail(selectedInsurance); return;
	    }
    }
	
	private void printInsuranceDetail(Insurance insurance) throws IOException {
	    while (true) {
	        System.out.println("******************** 상품 상세내용 *********************");
	        System.out.println("상품 상세내역을 확인하고 버튼을 선택하세요.");
	        System.out.println("\n[상품 상세내역]");
            System.out.println("아이디 : " + insurance.getId()
            		+ "\n이름 : " + insurance.getName()
            		+ "\n유형 : " + insurance.getType().getName()
            		+ "\n가입대상자 : " + insurance.getTarget()
					+ "\n보상조건 : " + insurance.getCompensateCondition()
					+ "\n비보상조건 : " + insurance.getNotCompensateCondition()
					+ "\n계산식 아이디 : " + insurance.getCalculationFormulaId());
	        System.out.println("\n[버튼]");
            System.out.println("1. 인가");
            System.out.println("2. 거절");
            System.out.println("3. 계산식 조회");
            System.out.println("0. 뒤로가기");
            String choice = userInput.readLine().trim();
            if (choice.equals("0")) {
            	return;
            } else if(choice.equals("1")){
				try {serviceContainer.getInsuranceService().examineAuthorization(insurance.getId(), InsuranceStatus.Authorize);}
				catch (NoDataException e) {System.out.println(e.getMessage()); return;}
				System.out.println("상품이 인가되었습니다."); return;
            } else if (choice.equals("2")){
				try {serviceContainer.getInsuranceService().examineAuthorization(insurance.getId(), InsuranceStatus.RefuseAuthorize);}
				catch (NoDataException e) {System.out.println(e.getMessage()); return;}
				System.out.println("인가가 거절되었습니다."); return;
            } else if (choice.equals("3")) {
            	printFormulaDetail(insurance.getCalculationFormulaId()); continue;
            } else {
            	System.out.println("! 잘못된 입력입니다."); continue;
			}
        }
    }
	private void printFormulaDetail(int id) throws IOException{
        System.out.println("******************** 계산식 상세내용 *********************");
		CalculationFormula formula = null;
		try {formula = serviceContainer.getCalculationFormulaService().getCalculationFormula(id);}
		catch (NoDataException e) {System.out.println(e.getMessage()); return;}
		if(formula instanceof HomeFormula) {printHomeFormulaDetail((HomeFormula) formula);}
		else {printHomeFormulaDetail((WorkplaceFormula) formula);}
	}
	private void printHomeFormulaDetail(HomeFormula formula) {
		DecimalFormat decFormat = new DecimalFormat("###,###");
		System.out.println("아이디 : " + formula.getId()
						+ "\n이름 : " + formula.getName());
		System.out.println("[거주 유형에 따른 위험도]");
        for (int i = ResidenceType.values().length-1; i>=0; i--) {
            System.out.println(ResidenceType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToResidenceType().get(ResidenceType.values()[i]).getLevel());
        }
		System.out.println("[주택 유형에 따른 위험도]");
		for (int i = HouseType.values().length-1; i>=0; i--) {
			System.out.println(HouseType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToHouseType().get(HouseType.values()[i]).getLevel());
		}
		System.out.println("[거주 평수에 따른 위험도]");
		for (int i = HomeSquareMeter.values().length-1; i>=0; i--) {
			System.out.println(HomeSquareMeter.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToSquareMeter().get(HomeSquareMeter.values()[i]).getLevel());
		}
		System.out.println("[기둥 유형에 따른 위험도]");
		for (int i = PillarType.values().length-1; i>=0; i--) {
			System.out.println(PillarType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToPillarType().get(PillarType.values()[i]).getLevel());
		}
		System.out.println("[지붕 유형에 따른 위험도]");
		for (int i = RoofType.values().length-1; i>=0; i--) {
			System.out.println(RoofType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToRoofType().get(RoofType.values()[i]).getLevel());
		}
		System.out.println("[외벽 유형에 따른 위험도]");
		for (int i = OutwallType.values().length-1; i>=0; i--) {
			System.out.println(OutwallType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToOutwallType().get(OutwallType.values()[i]).getLevel());
		}
		System.out.println("[보상금에 따른 위험도]");
		for (int i = HomeCompensation.values().length-1; i>=0; i--) {
			System.out.println(HomeCompensation.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToCompensation().get(HomeCompensation.values()[i]).getLevel());
		}
		System.out.println("보험료 산출 상수 : " + decFormat.format(formula.getMultiplierForPayment()));
		System.out.println("최소 보상금 산출 상수 : " + decFormat.format(formula.getMultiplierForMinCompensation()));
		System.out.println("최대 보상금 산출 상수 : " + decFormat.format(formula.getMultiplierForMaxCompensation()));
	}
	private void printHomeFormulaDetail(WorkplaceFormula formula) {
		DecimalFormat decFormat = new DecimalFormat("###,###");
		System.out.println("아이디 : " + formula.getId()
						+ "\n이름 : " + formula.getName());
		System.out.println("[건물용도에 따른 위험도]");
		for (Entry<WorkplaceUsage, RiskLevel> entrySet : formula.getRiskLevelAccordingToUsage().entrySet()) {
		System.out.println(entrySet.getKey().getName() + " : " + entrySet.getValue().getLevel());
		}
		System.out.println("[평수에 따른 위험도]");
		for (int i = WorkplaceSquareMeter.values().length-1; i>=0; i--) {
			System.out.println(WorkplaceSquareMeter.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToSquareMeter().get(WorkplaceSquareMeter.values()[i]).getLevel());
		}
		System.out.println("[층수에 따른 위험도]");
		for (int i = Floor.values().length-1; i>=0; i--) {
			System.out.println(Floor.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToFloor().get(Floor.values()[i]).getLevel());
		}
		System.out.println("[기둥 유형에 따른 위험도]");
		for (int i = PillarType.values().length-1; i>=0; i--) {
			System.out.println(PillarType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToPillarType().get(PillarType.values()[i]).getLevel());
		}
		System.out.println("[지붕 유형에 따른 위험도]");
		for (int i = RoofType.values().length-1; i>=0; i--) {
			System.out.println(RoofType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToRoofType().get(RoofType.values()[i]).getLevel());
		}
		System.out.println("[외벽 유형에 따른 위험도]");
		for (int i = OutwallType.values().length-1; i>=0; i--) {
			System.out.println(OutwallType.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToOutwallType().get(OutwallType.values()[i]).getLevel());
		}
		System.out.println("[보상금에 따른 위험도]");
		for (int i = WorkplaceCompensation.values().length-1; i>=0; i--) {
			System.out.println(WorkplaceCompensation.values()[i].getName()
					+ " : " + formula.getRiskLevelAccordingToCompensation().get(WorkplaceCompensation.values()[i]).getLevel());
		}
		System.out.println("보험료 산출 상수 : " + decFormat.format(formula.getMultiplierForPayment()));
		System.out.println("최소 보상금 산출 상수 : " + decFormat.format(formula.getMultiplierForMinCompensation()));
		System.out.println("최대 보상금 산출 상수 : " + decFormat.format(formula.getMultiplierForMaxCompensation()));
	}
}