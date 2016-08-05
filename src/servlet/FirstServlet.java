package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet(description = "Il primo esempio di servlet", urlPatterns = { "/FirstServlet" })
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FirstServlet() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			response.getWriter().println("<p><b>" + paramName + "</b>:" + request.getParameter(paramName) + "</p>");
		}
		// Get current time

		Calendar calendar = new GregorianCalendar();
		String am_pm;
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		if(calendar.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";
		String CT = hour+":"+ minute +":"+ second +" "+ am_pm;
		response.getWriter().println("</br><p align='right'>" + CT + "</p>");
	
		String ipAddress = request.getRemoteAddr();
		response.getWriter().println("<p><b>Ip del Client:</b> " + ipAddress + "</p>");
		
		
		// Create a session object if it is already not created.
		HttpSession session = request.getSession(true);
		// Get session creation time.
		Date createTime = new Date(session.getCreationTime());
		// Get last access time of this web page.
		Date lastAccessTime = new Date(session.getLastAccessedTime());
		String title = "Welcome Back to my website";
		Integer visitCount = new Integer(0);
		String visitCountKey = new String("visitCount");
		String userIDKey = new String("userID");
		String userID = new String("ABCD");
		// Check if this is new comer on your web page.
		if (session.isNew()){
			title = "Welcome to my website";
			session.setAttribute(userIDKey, userID);
		} else {
			visitCount = (Integer)session.getAttribute(visitCountKey);
			visitCount = visitCount + 1;
			userID = (String)session.getAttribute(userIDKey);
		}
		session.setAttribute(visitCountKey, visitCount);
		// Set response content type
		response.setContentType("text/html");
		response.getWriter().println("<h2 align='center'>" + title+ "</h2></br>");
		response.getWriter().println("<p><b>Session ID:</b> " + session.getId()+ "</p>");
		response.getWriter().println("<p><b>Created:</b> " + createTime + "</p>");
		response.getWriter().println("<p><b>Last Access:</b> " + lastAccessTime+ "</p>");
		response.getWriter().println("<p><b>User ID:</b> " + userID+ "</p>");
		response.getWriter().println("<p><b>Visit Count:</b> " + visitCount+ "</p>");
		
		///////////////////////////////////////////
		String QUERY = "";
		
		String KindOfQuery    = request.getParameter("KindOfQuery");
		String book_title = request.getParameter("book_title");
		String author = request.getParameter("author");
		String amazon_url = request.getParameter("amazon_url");
		
		if(KindOfQuery!=null){
			if(KindOfQuery.equals("SELECT")||KindOfQuery.equals("select") &&(book_title!=null || author!=null || amazon_url!=null)){
				//QUERY = "SELECT book_title, author, amazon_url FROM books";
				QUERY = "SELECT ";
				if(book_title != null)
					QUERY = QUERY + "book_title, ";
				if(author != null)
					QUERY = QUERY + "author, ";
				if(amazon_url != null)
					QUERY = QUERY + "amazon_url ";
				else
					QUERY = QUERY.substring(0, QUERY.length() - 2) + " "; //rimuovo la virgola
				QUERY = QUERY + "from books";	
				SelectQuery(QUERY, response);	
				
			}else if(KindOfQuery.equals("INSERT")|| KindOfQuery.equals("insert")){
				//QUERY = "INSERT INTO books (book_title, author, amazon_url) VALUES ('Mas Que Nada dei poveri', 'Ruth Groviller', 'www.amazon.com?id=7675')";
				if(book_title!=null && author!=null && amazon_url!=null){
					QUERY = "INSERT INTO books (book_title, author, amazon_url) VALUES ('" + book_title + "', '" + author + "', '" + amazon_url + "')";
					InsertQuery(QUERY, response);
				}
			}
		
			
		}
		
		
		
		//String QUERY = "SELECT book_title, author, amazon_url FROM books";
		//String QUERY = "INSERT INTO books (book_title, author, amazon_url) VALUES ('Mas Que Nada dei poveri', 'Ruth Groviller', 'www.amazon.com?id=7675')";
		
		//SelectAllFromDB(QUERY,response);
		//InsertIntoDB(request,response);
		
		
		/*
		PrintWriter out = response.getWriter();
		String docType =
				"<!doctype html public \"-//w3c//dtd html 4.0 " +
						"transitional//en\">\n";
		out.println(docType + "<html>\n" +	"<head><title>" + title + "</title></head>\n" +
				"<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">" + title + "</h1>\n" +
				"<h2 align=\"center\">Session Infomation</h2>\n" +
				"<table border=\"1\" align=\"center\">\n" +
				"<tr bgcolor=\"#949494\">\n" +
				" <th>Session info</th><th>value</th></tr>\n" +	"<tr>\n" +
				" <td>id</td>\n" +
				" <td>" + session.getId() + "</td></tr>\n" +
				"<tr>\n" + " <td>Creation Time</td>\n" +
				" <td>" + createTime +
				" </td></tr>\n" + "<tr>\n" +
				" <td>Time of Last Access</td>\n" +
				" <td>" + lastAccessTime + " </td></tr>\n" + "<tr>\n" +
				" <td>User ID</td>\n" +	" <td>" + userID +	" </td></tr>\n" +
				"<tr>\n" + " <td>Number of visits</td>\n" +
				" <td>" + visitCount + "</td></tr>\n" +
				"</table>\n" +	"</body></html>");*/
	}	
	/*
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getWriter().println("<html><head>RESULT</head><body>Questa pagina è automaticamente generata.</body></html>");
		
		String name    = request.getParameter("name");
		String surname = request.getParameter("surname");
		
		String italiano   = request.getParameter("ita");
		String matematica = request.getParameter("mat");
		String storia     = request.getParameter("sto");
		
		
		response.setContentType("text/html");
		response.getWriter().println("<html><head><h2>Pagina generata dalla Servlet</h2></head>");
		response.getWriter().println("<body>");
		if(name!=null && surname!=null){
			response.getWriter().println("<p><b>Nome:</b> " + name + "</p><p><b>Cognome:</b> " + surname + "</p>");
		}
		
		if(italiano!=null || matematica!=null || storia!=null){
			response.getWriter().println("</br></br><h4>Materie</h4>");
		
			if(italiano!=null){
				response.getWriter().println("<p><b>Italiano:</b> " + italiano + "</p>");
			}
			if(matematica!=null){
				response.getWriter().println("<p><b>Matematica:</b> " + matematica + "</p>");
			}
			if(storia!=null){
				response.getWriter().println("<p><b>Storia:</b> " + storia + "</p>");
			}
		}
		
		
		response.getWriter().print("</body></html>");
	
	}*/

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	/*private void SelectAllFromDB(String QUERY, HttpServletResponse response){
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";
		final String DB_URL="jdbc:mysql://localhost:3306/slimtestdb";
		// Database credentials
		final String USER = "root";
		final String PASS = "";
		Connection conn = null;
		Statement stmt = null;
		//QUERY = "SELECT book_title, author, amazon_url FROM books";
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			// Execute SQL query
			stmt = conn.createStatement();
			
			if(QUERY.contains("INSERT")){
				//stmt.executeUpdate("INSERT INTO Customers " + "VALUES ('Mas Que Nada dei poveri', 'Ruth Groviller', 'www.amazon.com?id=7675')");
				stmt.executeUpdate(QUERY);
				
				// Clean-up environment
				//rs.close();
				stmt.close();
				conn.close();

			}else if(QUERY.contains("SELECT")){
				ResultSet rs = stmt.executeQuery(QUERY);
				// Extract data from result set
				while(rs.next()){
					//Retrieve by column name
					String title = rs.getString("book_title");
					String author = rs.getString("author");
					String amazon_url = rs.getString("amazon_url");
					//Display values
					response.getWriter().println("<p><b>Book title :</b> " + title + "</p>");
					response.getWriter().println("<p><b>Book author:</b> " + author + "</p>");
					response.getWriter().println("<p><b>Book link  :</b> " + amazon_url + "</p>");
					response.getWriter().println("</br>");
				}
				// Clean-up environment
				rs.close();
				stmt.close();
				conn.close();

			}
			
							
		}catch(SQLException e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}catch(Exception e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
			}
		}
	}
	private void InsertIntoDB(HttpServletRequest request, HttpServletResponse response){
		String book_title   = request.getParameter("book_title");
		String author = request.getParameter("author");
		String amazon_url     = request.getParameter("amazon_url");
		
		if(book_title!=null && author!=null && amazon_url!=null){
			String QUERY = "INSERT into books VALUES ('" + book_title + "','" + author + "','" + amazon_url + "')";
			SelectAllFromDB(QUERY, response);
		}
		
	}*/
	
	
	
	private void SelectQuery(String QUERY, HttpServletResponse response){
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";
		final String DB_URL="jdbc:mysql://localhost:3306/slimtestdb";
		// Database credentials
		final String USER = "root";
		final String PASS = "";
		Connection conn = null;
		Statement stmt = null;
		//QUERY = "SELECT book_title, author, amazon_url FROM books";
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			// Execute SQL query
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(QUERY);
			// Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				try{
					String title = rs.getString("book_title");
					response.getWriter().println("<p><b>Book title :</b> " + title + "</p>");
				}catch(Exception e){}
				
				try{
					String author = rs.getString("author");
					response.getWriter().println("<p><b>Book author:</b> " + author + "</p>");
				}catch(Exception e){}
				
				try{
					String amazon_url = rs.getString("amazon_url");
					response.getWriter().println("<p><b>Book link  :</b> " + amazon_url + "</p>");
				}catch(Exception e){}
				
				response.getWriter().println("</br>");
			}
			// Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
			
		}catch(SQLException e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}catch(Exception e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
	}
	
	
	
	private void InsertQuery(String QUERY, HttpServletResponse response){
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";
		final String DB_URL="jdbc:mysql://localhost:3306/slimtestdb";
		// Database credentials
		final String USER = "root";
		final String PASS = "";
		Connection conn = null;
		Statement stmt = null;
		//QUERY = "SELECT book_title, author, amazon_url FROM books";
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			// Execute SQL query
			stmt = conn.createStatement();
			//stmt.executeUpdate("INSERT INTO Customers " + "VALUES ('Mas Que Nada dei poveri', 'Ruth Groviller', 'www.amazon.com?id=7675')");
			
			//specifica per l'inserimento o la modifica
			stmt.executeUpdate(QUERY);
			
			// Clean-up environment
			//rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}catch(Exception e){
			try {
				response.getWriter().println("<h1>" + e.toString() + "</h1>");
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	
	
	
}





