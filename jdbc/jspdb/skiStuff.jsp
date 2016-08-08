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


  </body>
</html>
