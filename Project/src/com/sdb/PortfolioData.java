package com.sdb; //DO NOT CHANGE THIS

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Project.ConnectionManager;



/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class PortfolioData {
	private static Connection con;


	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");

		String byeRole = "TRUNCATE TABLE Role";
		try {

			PreparedStatement ps = con.prepareStatement(byeRole);
			ps.executeUpdate();

			String byeEmail = "TRUNCATE TABLE Email";
			ps = con.prepareStatement(byeEmail);
			ps.executeUpdate();

			String byeAddress = "TRUNCATE TABLE Address";
			ps = con.prepareStatement(byeAddress);
			ps.executeUpdate();


			String byePerson = "DELETE FROM Person";
			ps = con.prepareStatement(byePerson);
			ps.executeUpdate();


			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");

		String checkPerson = "SELECT * FROM Person WHERE personCode = ?";
		try {
			PreparedStatement ps = con.prepareStatement(checkPerson);
			ps.setString(1, personCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int personId = rs.getInt("personId");
				String byeRole = "DELETE FROM Role WHERE personId = ?";
				ps = con.prepareStatement(byeRole);
				ps.setInt(1, personId);
				ps.executeUpdate();

				String byeEmail = "DELETE FROM Email WHERE personId = ?";
				ps = con.prepareStatement(byeEmail);
				ps.setInt(1, personId);
				ps.executeUpdate();

				String byeAddress = "DELETE FROM Address WHERE personId = ?";
				ps = con.prepareStatement(byeAddress);
				ps.setInt(1, personId);
				ps.executeUpdate();

				String byePerson = "DELETE FROM Person WHERE personId = ?";
				ps = con.prepareStatement(byePerson);
				ps.setInt(1, personId);
				ps.executeUpdate();

				if (rs != null && ! rs.isClosed())
					rs . close () ;
				if (ps != null && ! ps.isClosed())
					ps.close() ;
				if (con != null && ! con.isClosed())
					con.close();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to add a person record to the database with the provided data. The
	 * <code>brokerType</code> will either be "E" or "J" (Expert or Junior) or 
	 * <code>null</code> if the person is not a broker.
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @param brokerType
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country, String brokerType, String secBrokerId) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String newState = "INSERT INTO State(state, country) VALUES(?, ?)";
		String checkState = "SELECT stateId FROM State WHERE state = ?";
		int stateId = 0;
		int personId = 0;

		try {
			PreparedStatement ps = con.prepareStatement(checkState);
			ps.setString(1, state);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				stateId = rs.getInt("stateId");
			}
			else{
				ps = con.prepareStatement(newState);
				ps.setString(1, state);
				ps.setString(2, country);
				ps.executeUpdate();
				ps = con.prepareStatement("SELECT LAST_INSERT_ID()");
				rs = ps.executeQuery();
				rs.next();
				stateId = rs.getInt("LAST_INSERT_ID()");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String newPerson = "INSERT INTO Person (personId, personCode, firstName, lastName, brokerType, secId) "
				+ "VALUES (default, ?, ?, ?, ?, ?)";
		String checkPerson = "SELECT personId FROM Person WHERE personCode = ?";		

		try{
			PreparedStatement ps = con.prepareStatement(checkPerson);
			ps.setString(1, personCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				personId = rs.getInt("personId");
			}
			else{
				ps = con.prepareStatement(newPerson);
				ps.setString(1, personCode);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, brokerType);
				ps.setString(5, secBrokerId);
				ps.executeUpdate();
				ps = con.prepareStatement("SELECT LAST_INSERT_ID()");
				rs = ps.executeQuery();
				rs.next();
				personId = rs.getInt("LAST_INSERT_ID()");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		String newAddress = "INSERT INTO Address(street, city, zip, stateId, personId) "
				+ "VALUES(?, ?, ?, (SELECT stateId FROM State WHERE state = ?),"
				+ " (SELECT personId FROM Person WHERE personCode = ?))";
		String checkAddress = "SELECT addressId FROM Address WHERE personID = ? AND stateId = ?";
		try{
			PreparedStatement ps = con.prepareStatement(checkAddress);
			ps.setInt(1, personId);
			ps.setInt(2, stateId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				int addressId = rs.getInt("addressId");
			}
			else{
				ps = con.prepareStatement(newAddress);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setString(4, state);
				ps.setString(5, personCode);
				ps.executeUpdate();
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");

		/* -------------------------------CHECK FOR DUPLICATES ------------------------------*/
		String getPersonId = "SELECT personId FROM Person WHERE personCode = ?";
		int personId;
		try {
			PreparedStatement ps = con.prepareStatement(getPersonId);
			ps.setString(1, personCode);
			ResultSet rs = ps.executeQuery();
			rs.next();
			personId = rs.getInt("personId");

			String newEmail = "INSERT INTO Email (emailId, email, personId) values (default, ?, ?)";
			String checkEmail = "SELECT emailId, personId FROM Email WHERE email = ?";
			String addPerson = "UPDATE Email SET personId = ? WHERE emailId = ?";

			ps = con.prepareStatement(checkEmail);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(rs.next()){
				int emailId = rs.getInt("emailId");
				ps = con.prepareStatement(addPerson);
				ps.setInt(1, personId);
				ps.setInt(2, emailId);
				ps.executeUpdate();
			}
			else{
				ps = con.prepareStatement(newEmail);
				ps.setString(1, email);
				ps.setInt(2, personId);
				ps.executeUpdate();
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Removes all asset records from the database
	 */
	public static void removeAllAssets() {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String byeAssetGroup = "DELETE FROM AssetGroup";
		try {

			PreparedStatement ps = con.prepareStatement(byeAssetGroup);
			ps.executeUpdate();

			String byeAsset = "DELETE FROM Asset";
			ps = con.prepareStatement(byeAsset);
			ps.executeUpdate();


			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the asset record from the database corresponding to the
	 * provided <code>assetCode</code>
	 * @param assetCode
	 */
	public static void removeAsset(String assetCode) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String checkAsset = "SELECT * FROM Asset WHERE assetCode = ?";
		try {
			PreparedStatement ps = con.prepareStatement(checkAsset);
			ps.setString(1, assetCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int assetId = rs.getInt("assetId");

				String byeAssetGroup = "DELETE FROM AssetGroup WHERE assetId = ?";
				ps = con.prepareStatement(byeAssetGroup);
				ps.setInt(1, assetId);
				ps.executeUpdate();

				String byeAsset = "DELETE FROM Asset WHERE assetId = ?";
				ps = con.prepareStatement(byeAsset);
				ps.setInt(1, assetId);
				ps.executeUpdate();
				if (rs != null && ! rs.isClosed())
					rs . close () ;
				if (ps != null && ! ps.isClosed())
					ps.close() ;
				if (con != null && ! con.isClosed())
					con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds a deposit account asset record to the database with the
	 * provided data. 
	 * @param assetCode
	 * @param label
	 * @param apr
	 */
	public static void addDepositAccount(String assetCode, String label, double apr) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String newDeposit =	  "INSERT INTO Asset (assetId, assetCode, assetType, label, apr) "
				+ "VALUES (default, ?, 'D', ?, ?)";

		try {
			PreparedStatement ps = con.prepareStatement(newDeposit);
			ps.setString(1, assetCode);
			ps.setString(2, label);
			ps.setDouble(3, apr);
			ps.executeUpdate();

			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds a private investment asset record to the database with the
	 * provided data.  The <code>baseRateOfReturn</code> is assumed to be on the
	 * scale [0, 1].
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param baseOmega
	 * @param totalValue
	 */
	public static void addPrivateInvestment(String assetCode, String label, Double quarterlyDividend, 
			Double baseRateOfReturn, Double baseOmega, Double totalValue) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String newPrivateInvestment = "INSERT INTO Asset (assetId, assetCode, assetType, label, dividend, baseRate, risk, totalValue)  "
				+ "VALUES (default, ?, 'P', ?, ?, ?, ?, ?)";

		try {
			PreparedStatement ps = con.prepareStatement(newPrivateInvestment);
			ps.setString(1, assetCode);
			ps.setString(2, label);
			ps.setDouble(3, quarterlyDividend);
			ps.setDouble(4, baseRateOfReturn);
			ps.setDouble(5, baseOmega);
			ps.setDouble(6, totalValue);
			ps.executeUpdate();

			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a stock asset record to the database with the
	 * provided data.  The <code>baseRateOfReturn</code> is assumed to be on the 
	 * scale [0, 1].
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param beta
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, String label, Double quarterlyDividend, 
			Double baseRateOfReturn, Double beta, String stockSymbol, Double sharePrice) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String newStock = "INSERT INTO Asset (assetId, assetCode, assetType, label, dividend, baseRate, risk, stockSymbol, sharePrice) "
				+ "VALUES (default, ?, 'S', ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(newStock);
			ps.setString(1, assetCode);
			ps.setString(2, label);
			ps.setDouble(3, quarterlyDividend);
			ps.setDouble(4, baseRateOfReturn);
			ps.setDouble(5, beta);
			ps.setString(6, stockSymbol);
			ps.setDouble(7, sharePrice);
			ps.executeUpdate();

			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all portfolio records from the database
	 */
	public static void removeAllPortfolios() {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String byeRole = "DELETE FROM Role";
		try {
			PreparedStatement ps = con.prepareStatement(byeRole);
			ps.executeUpdate();

			String byeAssetGroup = "DELETE FROM AssetGroup";
			ps = con.prepareStatement(byeAssetGroup);
			ps.executeUpdate();

			String byePortfolio = "DELETE FROM Portfolio";
			ps = con.prepareStatement(byePortfolio);
			ps.executeUpdate();
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the portfolio record from the database corresponding to the
	 * provided <code>portfolioCode</code>
	 * @param portfolioCode
	 */
	public static void removePortfolio(String portfolioCode) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String checkPortfolio = "SELECT * FROM Portfolio WHERE portfolioCode = ?";
		try {
			PreparedStatement ps = con.prepareStatement(checkPortfolio);
			ps.setString(1, portfolioCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int portfolioId = rs.getInt("portfolioId");
				String byeRole = "DELETE FROM Role WHERE portfolioId = ?";
				ps = con.prepareStatement(byeRole);
				ps.setInt(1, portfolioId);
				ps.executeUpdate();

				String byeAssetGroup = "DELETE FROM AssetGroup WHERE portfolioId = ?";
				ps = con.prepareStatement(byeAssetGroup);
				ps.setInt(1, portfolioId);
				ps.executeUpdate();

				String byePortfolio = "DELETE FROM Portfolio WHERE portfolioId = ?";
				ps = con.prepareStatement(byePortfolio);
				ps.setInt(1, portfolioId);
				ps.executeUpdate();
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a portfolio records to the database with the given data.  If the portfolio has no
	 * beneficiary, the <code>beneficiaryCode</code> will be <code>null</code>
	 * @param portfolioCode
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addPortfolio(String portfolioCode, String ownerCode, String managerCode, String beneficiaryCode) {
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
		String q = "SELECT portfolioId FROM Portfolio WHERE portfolioCode = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, portfolioCode);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				int portfolioId = rs.getInt("portfolioId");
			}
			else{
				q = "INSERT INTO Portfolio(portfolioCode) VALUES(?)";
				ps = con.prepareStatement(q);
				ps.setString(1, portfolioCode);
				ps.executeUpdate();
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");

		try{
			q = 	"INSERT INTO Role(role, personId, portfolioId) "
					+ "VALUES(?, (SELECT personId FROM Person WHERE personCode = ?), "
					+ "(SELECT portfolioId FROM Portfolio WHERE portfolioCode = ?))";

			PreparedStatement ps = con.prepareStatement(q);

			if(managerCode != null && beneficiaryCode != null){

				ps.setString(1, "Owner");
				ps.setString(2, ownerCode);
				ps.setString(3, portfolioCode);
				ps.addBatch();

				ps.setString(1, "Manager");
				ps.setString(2, managerCode);
				ps.setString(3, portfolioCode);
				ps.addBatch();

				ps.setString(1, "Beneficiary");;
				ps.setString(2, beneficiaryCode);
				ps.setString(3, portfolioCode);
				ps.addBatch();

				ps.executeBatch();
			}
			else if(managerCode != null){

				ps.setString(1, "Owner");
				ps.setString(2, ownerCode);
				ps.setString(3, portfolioCode);
				ps.addBatch();

				ps.setString(1, "Manager");
				ps.setString(2, managerCode);
				ps.setString(3, portfolioCode);
				ps.addBatch();
				ps.executeBatch();
			}
			else{

				ps.setString(1, "Owner");
				ps.setString(2, ownerCode);
				ps.setString(3, portfolioCode);
				ps.executeUpdate();

				ps.setString(1, "Beneficiary");
				ps.setString(2, beneficiaryCode);
				ps.setString(3, portfolioCode);
				ps.executeUpdate();
			}
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Associates the asset record corresponding to <code>assetCode</code> with the 
	 * portfolio corresponding to the provided <code>portfolioCode</code>.  The third 
	 * parameter, <code>value</code> is interpreted as a <i>balance</i>, <i>number of shares</i>
	 * or <i>stake percentage</i> (on the scale [0, 1]) depending on the type of asset the <code>assetCode</code> is
	 * associated with.  
	 * @param portfolioCode
	 * @param assetCode
	 * @param value
	 */
	public static void addAsset(String portfolioCode, String assetCode, double value) {
		try {
			String q = "SELECT a.assetId, a.assetCode, ag.assetValue, ag.assetGroupId FROM Portfolio p "
					+ "JOIN AssetGroup ag on p.portfolioId = ag.portfolioId "
					+ "Join Asset a ON ag.assetId = a.assetId "
					+ "WHERE p.portfolioId = (SELECT p.portfolioID FROM Portfolio p WHERE p.portfolioCode = ?)"
					+ "HAVING a.assetCode = ?";

			con = ConnectionManager.getConnection("mshanahan", "Wfy3n_");
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1,portfolioCode);
			ps.setString(2, assetCode);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				int agId = rs.getInt("assetGroupId");
				q = "UPDATE AssetGroup SET assetValue = assetValue + ? "
						+ "WHERE assetGroupId = ?";
				ps = con.prepareStatement(q);
				ps.setDouble(1, value);
				ps.setInt(2, agId);
				ps.executeUpdate();
			}
			else{
				q = 	"INSERT INTO AssetGroup(assetValue, assetId, portfolioId)"
						+ "VALUES(?, (SELECT assetId FROM Asset WHERE assetCode = ?),"
						+ "(SELECT portfolioId FROM Portfolio WHERE portfolioCode = ?))";
				ps = con.prepareStatement(q);
				ps.setDouble(1, value);
				ps.setString(2, assetCode);
				ps.setString(3, portfolioCode);
				ps.executeUpdate();
			}
			if (rs != null && ! rs.isClosed())
				rs . close () ;
			if (ps != null && ! ps.isClosed())
				ps.close() ;
			if (con != null && ! con.isClosed())
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
