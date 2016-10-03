package ru.begjavaweb.checker;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.begjavaweb.settings.Settings;

public class RegistrationChecker extends HttpServlet {
	private static final String PwordPattern 
		= "^((?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#!%]).{7,21})$";

	private static final Pattern pattern = Pattern.compile(PwordPattern);

	private static final int saltSize = 128;

	private static final int base = 32;

	private static final SecureRandom randGenerator = new SecureRandom();

	private static MessageDigest messageDigest = null;

	private static final String dbUri = "jdbc:postgresql://localhost/skistuff";
	
	private static final String dbDriver = "org.postgresql.Driver";

	private static final String goodNews = "goodResult.jsp";
	
	private static final String badNews = "badResult.jsp";


	public RegistrationChecker() {}

	@Override
	public void init() {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			System.exit(0);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		verifyRegistration(req, res);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		sendResponse(req, res, "POST request only, please.", badNews);
	}	

	private boolean missingEmail(HttpServletRequest req) {
		String email = req.getParameter("email").trim();
		return (email == null || email.length() < 1);
	}

	private boolean pwdAndConfirmNotTheSame(HttpServletRequest req) {
		String passwd = req.getParameter("password").trim();
		String confirm = req.getParameter("confirm").trim();
		return (passwd == null || confirm == null || !passwd.equals(confirm));
	}

	private boolean pwdNotStrongEnough(HttpServletRequest req) {
		String passwd = req.getParameter("password").trim();
		Matcher matcher = pattern.matcher(passwd);
		return !matcher.matches();
	}

	private boolean emailAlreadyInUse(HttpServletRequest req) {
		String email = req.getParameter("email").trim();
		Connection conn = getConnection();
		boolean flag = false;
		try {
		    String sql = "select email from users where email = ?";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    stmt.setString(1, email);
		    ResultSet rs = stmt.executeQuery();
		    if (rs.isBeforeFirst()) flag = true;  // if there's a first record, the email is already in DB
		    rs.close();
		    stmt.close();
		    conn.close();
		}
		catch(Exception e) { e.printStackTrace(); }
		return flag;
    }

    private void saveSaltedPasswdHashToDB(HttpServletRequest req) {
		String email = req.getParameter("email").trim();
		String passwd = req.getParameter("password").trim();
		String salt = getSalt();
		byte[ ] digest = getHashedPassword(passwd, salt);
		Connection conn = getConnection();

		try {
		    String sql = "Insert into users (email, salt, digest) values (?,?,?)";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    stmt.setString(1, email);
		    stmt.setString(2, salt);
		    stmt.setBytes(3, digest);
		    stmt.executeUpdate();
		    stmt.close();
		    conn.close();
		}
		catch(Exception e) { System.err.println(e); }
    }

    private void verifyRegistration(HttpServletRequest req, HttpServletResponse res) {
		// Check for missing email, different inputs for 'password' and 'confirm', and
		// a sufficiently strong password. On failure, send an error response.
		if (missingEmail(req)) {
		    sendResponse(req, res, "Email input is missing.", badNews);
		    return;
		}

		if (pwdAndConfirmNotTheSame(req)) {
		    sendResponse(req, res, "Password and confirmed password differ.", badNews);
		    return;
		}

		if (pwdNotStrongEnough(req)) {
		    sendResponse(req, res, "The password is not sufficiently strong.", badNews);
		    return;
		}

		// Is the email already in use? Is so, send error response.
		if (emailAlreadyInUse(req)) 
		    sendResponse(req, res, "This email is already in use.", badNews);
		else { 
		    saveSaltedPasswdHashToDB(req);
		    sendResponse(req, res, "You are registered under the given email and password.", goodNews);
		}
    }

    private void sendResponse(HttpServletRequest req,
			      HttpServletResponse res, 
			      String msg, 
			      String page) {
		try {
		    HttpSession session = req.getSession();
		    session.setAttribute("result", msg);
		    res.sendRedirect(page);
		}
		catch(IOException e) { }
    }

    private Connection getConnection() {
		Properties props = setLoginForDB();
		Connection conn = null;
		try {
		    Class.forName(dbDriver); 
		    conn = DriverManager.getConnection(dbUri, props);
		}
		catch(Exception e) { e.printStackTrace(); }
		return conn;
    }

    private Properties setLoginForDB() {
		Properties props = new Properties();
		props.setProperty("user", Settings.getUserName());
		props.setProperty("password", Settings.getPassword());
		return props;
	    }

    private String getSalt() {
		return new BigInteger(saltSize, randGenerator).toString(base); 
    }

    private byte[ ] getHashedPassword(String pword, String salt) {
		byte[ ] digest = null;
		try {
		    String saltedPword = pword + salt;
		    messageDigest.update(saltedPword.getBytes());
		    digest = messageDigest.digest();
		}
		catch(Exception e) { }
		return digest;
    }
}




