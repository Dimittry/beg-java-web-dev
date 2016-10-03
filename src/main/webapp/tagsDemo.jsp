<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<h3>Above the customized tag</h3>
		<tags:register color="#ccccff" legcolor='#0000b3' now = '<%= new java.util.Date().toString()%>'>
		</tags:register>
		<h3>Below the customized tag</h3>
	</body>
</html>