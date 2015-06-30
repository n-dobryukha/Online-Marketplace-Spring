<fieldset class="container">
	<legend>Search Item</legend>
	<form id="formEditItem" action="${contextPath}/rest/item/${actionUrl}"
			class="form-horizontal"
			data-bv-message="This value is not valid"
			data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
			data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
			data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
		<div class="form-group">
			<label for="uid" class="col-sm-4 control-label">Item UID</label>
				<div class="col-sm-4">
					<input type="text" id="uid" name="uid" class="form-control"
					placeholder="UID"/>
			</div>
		</div>
		<div class="form-group">
			<label for="title" class="col-sm-4 control-label">Title</label>
				<div class="col-sm-4">
					<input type="text" id="title" name="title" class="form-control"
					placeholder="Title"/>
			</div>
		</div>
		<div class="form-group">
			<label for="description" class="col-sm-4 control-label">Description</label>
				<div class="col-sm-4">
					<input type="text" id="description" name="description" class="form-control"
					placeholder="Description"/>
			</div>
		</div>
		<hr/>
		<div class="form-group">
			<label for="minPrice" class="col-sm-4 control-label">Min Price</label>
			<div class="row col-sm-4">
				<div class="col-sm-4">
					<input type="text" id="minPrice" name="minPrice" class="form-control"
					placeholder="min price"/>
				</div>
				<div>
					<label for="maxPrice" class="col-sm-4 control-label">Max Price</label>
					<div class="col-sm-4">
						<input type="text" id="maxPrice" name="maxPrice" class="form-control"
						placeholder="max price"/>
					</div>
				</div>
			</div>
		</div>
		<hr/>
		<div class="form-group">
			<label for="isBuyItNow" class="col-sm-4 control-label">Buy It Now</label>
			<div class="col-sm-4">
				<div class="checkbox">
					<label>
						<input type="checkbox" id="isBuyItNow" name="isBuyItNow" aria-label="..."/>&nbsp;Show Only					
					</label>
				</div>
			</div>			
		</div>
		<hr/>
		<div class="form-group">
			<label for="startDate" class="col-sm-4 control-label">Start date</label>
				<div class="col-sm-2">
					<input type="text" id="startDate" name="startDate" class="form-control"
					placeholder="Start date"/>
			</div>
		</div>
		<div class="form-group">
			<label for="expireDate" class="col-sm-4 control-label">Expire date</label>
				<div class="col-sm-2">
					<input type="text" id="expireDate" name="expireDate" class="form-control"
					placeholder="Expire date"/>
			</div>
		</div>
		<hr/>
		<div class="form-group">
			<label for="bidderCount" class="col-sm-4 control-label">Bidder count</label>
				<div class="col-sm-2">
					<input type="text" id="bidderCount" name="bidderCount" class="form-control"
					placeholder="Count"/>
			</div>
		</div>
		<hr/>
		<div class="form-group">
			<div class="col-sm-offset-4 col-sm-4">
				<button type="submit" class="btn btn-primary btn-sm">Search</button>
				<button type="reset" class="btn btn-info btn-sm" id="btnReset">Clear</button>
			</div>
		</div>
	</form>
</fieldset>