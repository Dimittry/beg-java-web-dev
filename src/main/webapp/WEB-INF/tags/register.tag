<%@ attribute name='color' required='true' rtexprvalue='false' %>
<%@ attribute name='legcolor' required='false' rtexprvalue='false' %>
<%@ attribute name='now' required='false' rtexprvalue='true' %>

<fieldset style="width: 320px;">
	<legend style="color:${legcolor}">Registration for login</legend>
	<form method="POST" action="checkRegistration">
		<div style="font-weight:bold;">Password: </div>
		<ul>
			<li>At least 7 characters</li>
			<li>At least 1 uppercase</li>
			<li>At least 1 decimal digit</li>
			<li>At least 1 special character</li>
			<li>At least 1 lowercase letter</li>
		</ul>
		<table border="1" cellpadding="4" cellspacing="0">
			<tr>
				<td>Login email:</td>
				<td><input type="text" name="email" required="required"></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" pattern="^((?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#!%]).{7,21})$" required="required"></td>
			</tr>
			<tr>
				<td>Confirm</td>
				<td><input type="password" name="confirm" pattern="^((?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#!%]).{7,21})$" required="required"></td>
			</tr>
		</table>
		<p><input type="submit" name="submit" value="Register"></p>
	</form>
</fieldset>