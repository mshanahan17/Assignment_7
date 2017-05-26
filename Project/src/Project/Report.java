package Project;

import java.util.*;

public class Report {

	private MyList<Portfolio> list;


	public Report(MyList<Portfolio> portfolios) {// report constructor
		this.list = portfolios;
	}


	public void printReport(){
		String summary = "Portfolio Summary Report";

		/*Anonymous class that uses Javas collection sort and a comparator to sort the array of portfolios
		 * alphabetically by last names. If there are people with the same last name it orders them by first 
		 * names as well.
		 */
		/*Collections.sort(this.portfolios, new Comparator<Portfolio>() {
			public int compare(Portfolio a, Portfolio b)
			{
				int sort = a.getOwner().getLastName().compareTo(b.getOwner().getLastName());
				if(sort !=0){
					return sort;
				}
				return a.getOwner().getFirstName().compareTo(b.getOwner().getFirstName());
			}
		});*/

		System.out.println(summary);//heading
		for(int i =0; i< 150; i++){
			System.out.print("=");
		}
		System.out.printf("\n %-10s %-25s %-30s %15s %15s %15s %15s %15s\n", 
				"Portfolio", "Owner", "Manager", "Fees", "Commissions",
				"Risk Measure", "Return", "Total Value");//heading

		double grandValue = 0.0;
		double grandTotal = 0.0;
		double grandCommisions = 0.0;
		double grandFees = 0.0;

		//for(int i =0; i < portfolios.size(); i++){//loops through portfolios and assets to return values such as return,risk,fees,etc.
		for(Portfolio p: list){

			String code = p.getCode();
			String owner = p.printName(p.getOwner());
			double returnValue = 0.0;
			double total = 0.0;
			double fees = 0.0;
			double commisions = 0.0;
			double risk = 0.0;
			String manager = "";


			if(p.getManager() != null){// makes sure a manager was given
				manager = p.printName(p.getManager());
			}
			else{
				manager = "none";
			}

			total = p.getValue();
			returnValue = p.getReturn();
			if(total == 0){
				risk = 0.0;
			}
			else{
				risk = p.getRisk()/total;
			}
			grandTotal += total;
			grandValue += returnValue;
			fees = p.getFees();
			commisions = p.getFees(returnValue);
			grandFees += fees;
			grandCommisions += commisions;


			System.out.printf("\n %-10s %-25s %-30s  $%13.2f  $%13.2f  %14.4f  $%13.2f  $%13.2f",
					code, owner, manager, fees, commisions, risk,//prints out information for each portfolio
					returnValue, total);
		}
		System.out.println();
		System.out.printf("%69s", " ");
		for(int i =0; i< 80; i++){
			System.out.print("-");
		}
		System.out.printf("\n %68s $%13.2f  $%13.2f  %14s  $%13.2f  $%13.2f\n\n", "Totals", grandFees
				, grandCommisions, "",grandValue ,grandTotal);//prints out totals for all portfolios combined
	}


	public void printSummary(){
		String details = "Portfolio Details";
		System.out.println(details);
		for(int i =0; i< 150; i++){
			System.out.print("=");
		}
		//for(int i =0; i < portfolios.size(); i++){//cycles through to print a detailed report of each portfolio
		for(Portfolio p: list){
			ArrayList<Assets> a = p.getAssets();

			p.printCode();
			double totalRisk = 0.0;
			double totalValue = 0.0;
			double totalReturn = 0.0;

			for(int j = 0; j < 40; j++){
				System.out.print("*");
			}
			//prints the names of owner,manager,beneficiary if applicable
			p.printName(p.getOwner(), "Owner");
			p.printName(p.getManager(), "Manager");
			p.printName(p.getBenificiary(), "Benificiary");

			System.out.printf("\n\nAssets:\n%-15s %-35s %15s %15s %15s %15s", "Code", "Asset",
					"Return Rate", "Risk", "Annual Return", "Value");
			if(a.size() == 0){
				System.out.println();
				System.out.printf("%58s", " ");
				for(int l =0; l< 60; l++){
					System.out.print("-");
				}
				System.out.printf("\n%56s %9.2f%% %15.4f  $%13.2f  $%13.2f", "Totals", 0.0, 0.0000,
						0.0, 0.0);
			}
			for(int j= 0; j < a.size(); j++ ){//gets info for each individual asset in the portfolio


				String code = a.get(j).getCode();
				String asset = a.get(j).getLabel();
				double risk = a.get(j).getRisk();
				double annRet = a.get(j).getReturn();
				double val= a.get(j).getValue();
				double retRate = (annRet/val) * 100;

				System.out.printf("\n%-15s %-34s %15.2f%% %15.4f  $%13.2f  $%13.2f", code,
						asset, retRate, risk, annRet, val);
				totalRisk += a.get(j).getRisk()* a.get(j).getValue();
				totalValue += val;
				totalReturn += annRet;
				double totalRetRate = 0.0;
				if(j == a.size()-1){
					if(totalValue != 0){
						totalRisk = totalRisk/totalValue;
						totalRetRate = (totalReturn/totalValue)*100;
					}else{
						totalRisk = 0.0;
					}
					System.out.println();
					System.out.printf("%58s", " ");
					for(int l =0; l< 60; l++){
						System.out.print("-");
					}
					System.out.printf("\n%56s %9.2f%% %15.4f  $%13.2f  $%13.2f", "Totals", totalRetRate, totalRisk,
							totalReturn, totalValue);
				}
			}
		}
		System.out.println("\n\n");
	}
}
