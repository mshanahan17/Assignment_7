package Project;



public class Stocks extends Assets{//subclass of assets(inheritance)

	private double divedend;
	private double baseReturnRate;
	private double betaMeasure;
	private String stockSymbol;
	private double sharePrice;
	private double value;

	public Stocks(String code, String type, String label, double divedend, double baseReturnRate, double betaMeasure,
			String symbol, double sharePrice) {//constructor
		super(code, type, label);//calls Assets constructor
		this.divedend = divedend;
		this.baseReturnRate = baseReturnRate;
		this.betaMeasure = betaMeasure;
		this.stockSymbol = symbol;
		this.sharePrice = sharePrice;
		this.value = 0.0;
	}

	public Stocks(Stocks s, double value) {//copy constructor
		super(s.code, s.type, s.label);
		this.divedend = s.divedend;
		this.baseReturnRate = s.baseReturnRate/100;
		this.betaMeasure = s.betaMeasure;
		this.stockSymbol = s.stockSymbol;
		this.sharePrice = s.sharePrice;
		this.value = value;
	}

	public double getDivedend() {
		return divedend;
	}

	public double getRate() {//polymorphism with getRate(double) below
		return baseReturnRate;
	}

	public double getReturn(){// calculates return rate of the stock
		double owned = this.getValue();
		double rate = (4 * (divedend*this.value)) + owned * this.baseReturnRate;
		return rate;	
	}

	public double getRisk() {
		return betaMeasure;
	}

	public double getRisk(double value){ 
		return this.betaMeasure;
	}

	public String getSymbol() {
		return stockSymbol;
	}

	public double getSharePrice() {
		return sharePrice;
	}

	public double getVal(){
		return this.value;
	}
	public double getValue() {//calculates total value of asset based on number of stocks owned
		double total = value * this.sharePrice;
		return total;
	}
}
