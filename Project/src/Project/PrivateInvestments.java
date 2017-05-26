package Project;


public class PrivateInvestments extends Assets{//subclass of assets(inheritance)

	private double divedend;
	private double baseReturnRate;
	private double omegaMeasure;
	private double totalValue;
	private double value;

	public PrivateInvestments(String code, String type, String label, double divedend, double baseReturnRate,
			double omegaMeasure, double totalValue) {//constructor
		super(code, type, label);//calls Assets constructor
		this.divedend = divedend;
		this.baseReturnRate = baseReturnRate/100;
		this.omegaMeasure = omegaMeasure;
		this.totalValue = totalValue;
		this.value = 0.0;
	}

	public PrivateInvestments(PrivateInvestments p, double value) {//constructor
		super(p.code, p.type, p.label);//calls Assets constructor
		this.divedend = p.divedend;
		this.baseReturnRate = p.baseReturnRate;
		this.omegaMeasure = p.omegaMeasure;
		this.totalValue = p.totalValue;
		this.value = value;
	}

	public double getDivedend() {
		return this.divedend;
	}

	public double getRate() {
		return this.baseReturnRate;
	}

	public double getReturn(){//returns the return value of the asset based on percentage owned
		double owned = this.getValue();
		double rate = (this.divedend* this.value) * 4 + owned * this.baseReturnRate;
		return rate;	
	}

	public double getTotalValue() {
		return totalValue;
	}
	
	public double getVal(){
		return this.value;
	}

	public double getValue(){//returns the total value owned of this asset
		double total = this.value * this.totalValue;
		return total;
	}

	public double getRisk() {//calculates the omega risk measure of the asset
		double a = (-100000.0/this.totalValue);
		double risk = this.omegaMeasure + Math.exp(a);
		return risk;
	}
}
