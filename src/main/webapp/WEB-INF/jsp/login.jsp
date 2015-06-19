<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");%>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="css/default.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrapValidator.min.css">
</head>
<body>
	<fieldset class="container-main">
		<legend>Login</legend>
		<form id="formLogin" class="form-horizontal" method="POST" action="./login"
			data-bv-message="This value is not valid"
			data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
			data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
			data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
			<div class="form-group">
				<label for="login" class="col-sm-4 control-label">Login</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="login" name="login"
						placeholder="Login" autofocus="autofocus" required="required">					
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-sm-4 control-label">Password</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="password" name="password"
						placeholder="Password" required="required">
				</div>
			</div>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-8">
					<button type="submit" class="btn btn-primary btn-sm">Sign in</button>
					<a href="./auth/guest" class="btn btn-success btn-sm" role="button">As guest</a>
					<a href="registration.jsp" class="btn btn-info btn-sm" role="button">Registration</a>
				</div>
			</div>
		</form>
	</fieldset>
<script type="text/javascript" data-main="js/formValidator" src="js/require.js"></script>
</body>
</html>