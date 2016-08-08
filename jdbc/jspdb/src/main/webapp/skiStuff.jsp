<%@ page errorPage="error.jsp"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix="sql" %>   

<!DOCTYPE html>					  
<html>
  <head>
    <title>Kelly's Ski Equipment</title>
    <link rel = "stylesheet" href = "style.css" type = "text/css"></link>
  </head>
  <body>
  <h1>Inside 2</h1>
  
    <sql:setDataSource                    
      var = "myDS"                                           
      driver = "org.postgresql.Driver"
      url = "jdbc:postgresql://localhost:5432/skistuff"
      user = "olyver" 
      password = "SAMBADA1902"
    />
    <sql:query var = "listStuff" dataSource = "${myDS}"> 
      SELECT * FROM skisEtc ORDER BY id;
    </sql:query>


    <p>
       <center><h3>Kelly's Ski Equipment</h3></center>
    </p>
    <div align="center">
      <table class = "products" border = "1" cellpadding = "5">
        <tr>
          <th>Id</th>
          <th>Product</th>
          <th>Category</th>
	  <th>Price</th>
        </tr>
        <c:forEach var = "item" items = "${listStuff.rows}"> 
          <tr>
            <td class = "rght"><c:out value="${item.id}" /></td>
            <td class = "cent"><c:out value="${item.product}" /></td>
            <td class = "cent"><c:out value="${item.category}" /></td>
	    <td class = "rght"><c:out value="\$${item.price}" /></td>
          </tr>
        </c:forEach>
      </table>
    </div>

  </body>
</html>
