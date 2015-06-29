<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

	<fieldset class="container">
		<legend>Edit Item</legend>
		<form:form id="formEditItem" action="${contextPath}/rest/item/${actionUrl}" commandName="item"
			class="form-horizontal"
			data-bv-message="This value is not valid"
			data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
			data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
			data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
			<input type="hidden" id="_method" value="${httpMethod}"/>
			<form:input type="hidden" path="id"/>			
			<div class="form-group">
				<form:label path="title" class="col-sm-4 control-label">Title of Item</form:label>
				<div class="col-sm-4">
					<form:input type="text" path="title" class="form-control"
						placeholder="Title of Item" required="required" autofocus="autofocus"/>
				</div>
			</div>
			<div class="form-group">
				<form:label path="description" class="col-sm-4 control-label">Description</form:label>
				<div class="col-sm-4">
					<form:textarea rows="3" cols="" class="form-control" path="description"
						placeholder="Description" required="required"/>
				</div>
			</div>			
			<div class="form-group">
				<form:label path="startPrice" class="col-sm-4 control-label">Start price</form:label>
				<div class="col-xs-2">
					<div class="input-group">
						<div class="input-group-addon">$</div>
						<form:input type="text" class="form-control" path="startPrice"
							placeholder="0.00" required="required" min="0"/>
					</div>
				</div>
			</div>
			<div class="form-group">
				<form:label path="timeLeft" class="col-sm-4 control-label">Time Left</form:label>
				<div class="col-xs-2">
					<form:input type="number" class="form-control" path="timeLeft"
						min="1" max="1000" step="1" placeholder="Time in hour" required="required"/>
				</div>
			</div>
			<div class="form-group">
				<label for="buyItNow1" class="col-sm-4 control-label">Buy It Now</label>
				<div class="col-sm-4">
					<div class="checkbox">
						<label>
							<form:checkbox path="buyItNow" aria-label="..."/>
						</label>
					</div>
				</div>
			</div>
			<div class="form-group" style="${ item.isBuyItNow() ? 'display: none' : ''}">
				<form:label path="bidIncrement" class="col-sm-4 control-label">Bid Increment</form:label>
				<div class="col-xs-2">
					<div class="input-group">
						<div class="input-group-addon">$</div>
						<form:input type="text" class="form-control" path="bidIncrement"
							placeholder="0.00" required="required" min="0" disabled="${ item.isBuyItNow() }"/>
					</div>
				</div>
			</div>
			

			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-4">
					<button type="submit" class="btn btn-primary btn-sm">Save</button>
					<button type="reset" class="btn btn-info btn-sm" id="btnReset">Reset</button>
				</div>
			</div>
		</form:form>
	</fieldset>
	<script type="text/javascript" data-main="${contextPath}/js/formValidator" src="${contextPath}/js/require.js"></script>