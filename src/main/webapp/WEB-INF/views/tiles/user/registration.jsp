<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<fieldset class="container-main">
		<legend>Registration</legend>
		<form:form id="formRegistration" method="POST" action="${contextPath}/rest/user/"
			class="form-horizontal"
			data-bv-message="This value is not valid"
			data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
			data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
			data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
			<div class="form-group">
				<form:label path="fullName" class="col-sm-4 control-label">Full Name</form:label>
				<div class="col-sm-4">
					<form:input path="fullName" class="form-control"
						placeholder="Full Name" required="required" autofocus="autofocus"/>
				</div>
			</div>
			<div class="form-group">
				<form:label path="billingAddress" class="col-sm-4 control-label">Billing Address</form:label>
				<div class="col-sm-4">
					<form:input path="billingAddress" class="form-control"
						placeholder="Billing Address" required="required"/>
				</div>
			</div>			
			<div class="form-group">
				<form:label path="email" class="col-sm-4 control-label">Email</form:label>
				<div class="col-sm-4">
					<div class="input-group">
						<div class="input-group-addon">@</div>
						<form:input type="email" path="email" class="form-control"
							placeholder="mail@example.com" required="required"/>
					</div>
				</div>
			</div>
			<br/>
			<div class="form-group">
				<form:label path="login" class="col-sm-4 control-label">Login</form:label>
				<div class="col-sm-4">
					<form:input path="login" class="form-control"
						placeholder="Login" required="required" maxlength="30"/>
				</div>
			</div>			
			<div class="form-group">
				<form:label path="password" class="col-sm-4 control-label">Password</form:label>
				<div class="col-sm-4">
					<form:password path="password" class="form-control"
						placeholder="Password" required="required"/>
				</div>
			</div>
			<div class="form-group">
				<label for="confirmPassword" class="col-sm-4 control-label">Confirm Password</label>
				<div class="col-sm-4">
					<input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
						placeholder="Password" required="required"/>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-4">
					<button type="submit" class="btn btn-primary btn-sm">Registration</button>
					<button type="reset" class="btn btn-info btn-sm" id="btnReset">Reset</button>
				</div>
			</div>
		</form:form>
	</fieldset>
	<script type="text/javascript" data-main="${contextPath}/js/formValidator" src="${contextPath}/js/require.js"></script>