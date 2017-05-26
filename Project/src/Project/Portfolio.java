package Project;

import java.util.ArrayList;

public class Portfolio {

	private String code;
	private Persons owner;
	private Persons manager;
	private Persons benificiary;
	private ArrayList<Assets> assets;

	public Portfolio(String code, Persons owner, Persons manager, Persons benificiary, ArrayList<Assets> assets) {
		this.code = code;
		this.owner = owner;
		this.manager = manager;
		this.benificiary = benificiary;
		this.assets = assets;
	}

	public String getCode() {
		return code;
	}

	public Persons getOwner() {
		return owner;
	}

	public Persons getManager() {
		return manager;
	}

	public Persons getBenificiary() {
		return benificiary;
	}

	public ArrayList<Assets> getAssets() {
		return assets;
	}

	public String printName(Persons a){//overloading method returning a string of a formatted version of a name
		if(a != null){
			return a.printName();
		}
		else{
			return "none";
		}
	}

	public void printName(Persons a, String b){//overloading method that prints a formatted name and title to standard out
		if(a != null){
			System.out.printf("\n%-15s %-15s", b + ": ", a.printName());
		}
		else{
			System.out.printf("\n%-15s %-15s", b + ": ", "none");
		}
	}

	public double getFees(){//polymorphism overloading method that calculates base fees with no parameter
		double fees = 0.0;
		if(this.manager != null && this.assets != null && this.getManager().getType() != null){//gets fees if portfolio has a manager
			if(this.getManager().getType().equalsIgnoreCase("E")&& this.assets.size() != 0){
				fees = this.manager.getFees("E", this.assets.size());
			}
			else if(this.manager.getType().equalsIgnoreCase("J")&& this.assets.size() != 0){
				fees = this.manager.getFees("J", this.assets.size());
			}
			else{
				return 0.0;
			}
		}
		return fees;
	}

	public double getFees(double val){//polymorphism overloading method that calculates commision fees with a value given
		double commisions = 0.0;
		if(this.manager != null && this.assets != null && this.getManager().getType() != null){//gets commisions if portfolio has a manager
			if(this.getManager().getType().equalsIgnoreCase("E")&& this.assets.size() > 0){
				commisions = this.manager.getCommision("E", val);
			}
			else if(this.manager.getType().equalsIgnoreCase("J")&& this.assets.size() > 0){
				commisions = this.manager.getCommision("J", val);;
			}
			else{
				return commisions;
			}
		}
		return commisions;
	}

	public void printCode(){
		System.out.println("\n\nPortfolio: " + this.code);
	}

	public double getValue(){
		double value = 0.0;
		for(int i=0; i<this.assets.size(); i++){
			value += this.assets.get(i).getValue();
		}
		return value;
	}
	
	public double getReturn(){
		double retValue = 0.0;
		for(int i=0; i<this.assets.size(); i++){
			retValue += this.assets.get(i).getReturn();
		}
		return retValue;
	}
	
	public double getRisk(){
		double risk = 0.0;
		for(int i=0; i<this.assets.size(); i++){
			risk += this.assets.get(i).getRisk() * this.assets.get(i).getValue();
		}
		return risk;
	}

	public  String toString() {
			return  (this.owner.printName());
	}
}
