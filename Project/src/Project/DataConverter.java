package Project;

import java.sql.*;
import java.util.ArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.status.StatusLogger;

import com.sdb.PortfolioData;


public class DataConverter {
	private Connection con;
	private ArrayList<Persons> people = new ArrayList<Persons>();//list to hold the different persons objects
	private ArrayList<Assets> asset = new ArrayList<Assets>(); //list to hold the different assets objects
	private ArrayList<Portfolio> portfolioList = new ArrayList<Portfolio>();//list to hold portfolio objects
	private MyList<Portfolio> list = new MyList<Portfolio>(new NameComparator());
	private MyList<Portfolio> list2 = new MyList<Portfolio>(new ManagerComparator());
	private MyList<Portfolio> list3 = new MyList<Portfolio>(new AssetComparator());
	private static Logger log;
	static PortfolioData pd = new PortfolioData();
	
	/**
	 * Configures logger for log4j2
	 */
	static {
		StatusLogger.getLogger().setLevel(Level.OFF);
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		builder.setStatusLevel(Level.ERROR);
		builder.setConfigurationName("BuilderTest");
		builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
				.addAttribute("level", Level.DEBUG));
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
				ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout")
				.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
		appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL)
				.addAttribute("marker", "FLOW"));
		builder.add(appenderBuilder);
		builder.add(builder.newLogger("org.apache.logging.log4j", Level.DEBUG)
				.add(builder.newAppenderRef("Stdout")).addAttribute("additivity", false));
		builder.add(builder.newRootLogger(Level.ERROR).add(builder.newAppenderRef("Stdout")));
		log = LogManager.getLogger();
	}

	/**
	 * main method creates DataConverter object and calls the necessary methods
	 * to create objects and act on those objects
	 */
	public static void main(String[] args) {
		DataConverter dc = new DataConverter();
		dc.createPersons();
		dc.createAssets();
		dc.createPortfolios();
		dc.printReport();

	}	

	/**
	 * method to create objects from the assets from database
	 */
	public void createAssets(){
		try {
			String q = "select * from Asset";
			con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");//makes connection to database
			PreparedStatement st = con.prepareStatement(q);//prepared statement ot get asset info from DB
			ResultSet rs = st.executeQuery();//executes prepared statement and creates result set

			while(rs.next()){//reads the resultSet
				switch(rs.getString(3)){// using the 3rd column of resultset as type of asset for switch statement

				case "S"://stock account
					Stocks stock = new Stocks(rs.getString(2), "S" , rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), 
							rs.getString(10),  rs.getDouble(11));
					this.asset.add(stock);//add to array
					break;	
				case "D"://deposit account
					DepositAccount deposit = new DepositAccount(rs.getString(2), "D" , rs.getString(4), rs.getDouble(9));
					this.asset.add(deposit);//add to array
					break;
				case "P"://private investment account
					PrivateInvestments investment = new PrivateInvestments(rs.getString(2), "P" , rs.getString(4), 
							rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8));
					this.asset.add(investment);//add to array
					break;
				}
			}
			if ( rs != null && ! rs.isClosed())//closes resultset
				rs.close();
			if ( st != null && ! st.isClosed())//closes statement
				st.close();
			if ( con != null && ! con.isClosed())//closes connection
				con.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
	}
	
	/**
	 *method to create person objects
	 */
	public void createPersons(){
		try {
			con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
			String sqlQuerry = "select personId, personCode, firstName, lastName, brokerType, secId from Person";
			PreparedStatement st = con.prepareStatement(sqlQuerry);
			ResultSet rs = st.executeQuery();

			while(rs.next()){//reads resultset from person table
				int personId = rs.getInt("personId");
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String brokerType = rs.getString("brokerType");
				String secCode = rs.getString("secId");
				ArrayList<String> email = new ArrayList<String>();
				DataConverter d = new DataConverter();

				if(d.getEmail(personId) != null){//calls email method to create email arraylist
					email = d.getEmail(personId);
				}
				else{
					email = null;
				}
				Address address = d.getAddress(personId);//makes address object by calling get address method

				if(brokerType != null){//if person is a broker creates broker object else person object
					Broker bro = new Broker(personCode, brokerType, secCode, lastName, firstName, address, email);
					this.people.add(bro);
				}
				else{
					Persons person = new Persons(personCode, lastName, firstName, address, email);
					this.people.add(person);
				}
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (st != null && ! st.isClosed())
				st.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		}catch (SQLException e) {
			log.error("SQLException: ", e);
		}
	}	

	/**
	 * method to create portfolio objects
	 */
	public void createPortfolios(){
		try {
			con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
			String q = "select portfolioId, portfolioCode from Portfolio";
			PreparedStatement st = con.prepareStatement(q);
			ResultSet rs = st.executeQuery();

			while(rs.next()){ //reads result set till no more portfolios remain
				Persons owner = null;
				Persons manager = null;
				Persons beneficiary = null;
				String portCode = rs.getString("portfolioCode");
				String sqlQuery = "select r.role, p.personCode from Role r join Person p on r.personId = p.personId where portfolioId = ?";
				PreparedStatement st2 = con.prepareStatement(sqlQuery);
				st2.setInt(1,rs.getInt("portfolioId"));
				ResultSet rs2 = st2.executeQuery();

				while(rs2.next()){//reads result set to attach persons to portfolio and assign roles
					for(int i = 0; i < this.people.size(); i++){
						if(this.people.get(i).getCode().equals(rs2.getString("p.personCode"))){
							switch(rs2.getString("r.role")){
							case "Manager":
								manager = people.get(i);
								break;
							case "Owner":
								owner = people.get(i);
								break;
							case "Beneficiary":
								beneficiary = people.get(i);
								break;
							}
						}
					}
				}

				String q2 = ("select a.assetCode, ag.assetValue From Portfolio p "
						+ "join AssetGroup ag on p.portfolioId = ag.portfolioId "
						+ "join Asset a on ag.assetId = a.assetId "
						+ "where p.portfolioId = ?");
				st2 = con.prepareStatement(q2);
				st2.setInt(1, rs.getInt("portfolioId"));
				rs2 = st2.executeQuery();

				ArrayList<Assets> assets = new ArrayList<Assets>();

				while(rs2.next()){//reads result set of assets to add asset list to portfolio
					for(int i = 0; i < this.asset.size(); i++){
						if(this.asset.get(i).getCode().equals(rs2.getString("a.assetCode"))){
							if(this.asset.get(i) instanceof Stocks){
								Stocks stock = new Stocks((Stocks) this.asset.get(i), rs2.getDouble("ag.assetValue"));
								assets.add(stock);
							}
							else if(this.asset.get(i) instanceof PrivateInvestments){
								PrivateInvestments pi = new PrivateInvestments((PrivateInvestments) this.asset.get(i), 
										rs2.getDouble("ag.assetValue")/100);
								assets.add(pi);
							}
							else if(this.asset.get(i) instanceof DepositAccount){
								DepositAccount da = new DepositAccount((DepositAccount) this.asset.get(i), 
										rs2.getDouble("ag.assetValue"));
								assets.add(da);
							}
							else{
							}
						}
					}
				}
				if(rs2 != null && ! rs2.isClosed())
					rs2.close();
				if(st2 != null && ! st.isClosed())
					st2.close();
				Portfolio portfolios = new Portfolio(portCode, owner, manager, beneficiary, assets);
				this.portfolioList.add(portfolios);//adds portfolio objects to a list
				this.list.add(portfolios);
				this.list2.add(portfolios);
				this.list3.add(portfolios);
			}
			if(rs != null && ! rs.isClosed())
				rs.close();
			if(st != null && ! st.isClosed())
				st.close();
			if(con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
	}
	
	/**
	 * Method where all the calls to Report occur and lable the sorting
	 */
	public void printReport(){
		Report report = new Report(this.list);//sorted by owner name
		Report report2 = new Report(this.list2);//sorted by manager type then name
		Report report3 = new Report(this.list3);//sorted by total asset value of portfolio
		// report object from portfolios and assets
		System.out.println("SORTED BY OWNER NAME:");
		report.printReport();//calls report method to print out summary and detailed report
		System.out.println("\nSORTED BY MANAGER TYPE THEN NAME NO MANAGER GOES TO FRONT OF LIST:");
		report2.printReport();
		System.out.println("\nSORTED BY TOTAL ASSET VALUE OF PORTFOLIO HIGH TO LOW:");
		report3.printReport();
		//report.printSummary();
	}

	/**
	 * method to create email array of a certain person from email table
	 * @param personId
	 * @return
	 */
	public ArrayList<String> getEmail(int a){
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String sqlQuerry = "select email from Email where personId = ?";
		ArrayList<String> email = new ArrayList<String>();
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(sqlQuerry);
			st.setInt(1,a);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				email.add(rs.getString("email"));
			}
			if(rs != null && ! rs.isClosed())
				rs.close();
			if(st != null && ! st.isClosed())
				st.close();
			if(con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
		return email;
	}

	/**
	 * creates address object of a certain person
	 * @param personId
	 * @return
	 */
	public Address getAddress(int a){
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String sqlQuerry = "select a.street, a.city, a.zip, s.state, s.country from Address a "
				+ "join State s on a.stateId = s.stateId where personId = ?";
		Address address = null;
		try {
			PreparedStatement st = con.prepareStatement(sqlQuerry);
			st.setInt(1, a);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				if(rs.getString("a.zip") != null){
					address = new Address(rs.getString("a.street"), rs.getString("a.city"), rs.getString("s.state"), 
							rs.getString("a.zip"),rs.getString("s.country"));
				}
				else{
					address = new Address(rs.getString("a.street"), rs.getString("a.city"), rs.getString("s.state"), 
							"" ,rs.getString("s.country"));
				}
			}
			if(rs != null && ! rs.isClosed())
				rs.close();
			if(st != null && ! st.isClosed())
				st.close();
			if(con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
		return address;
	}
}



