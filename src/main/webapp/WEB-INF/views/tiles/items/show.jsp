	<input type='hidden' id='type' value='${ type }'/>
	<div class="container-fluid">
		<fieldset>
			<legend class="h4">${ type } Items</legend>
			<table id="dataTable" class="table table-striped table-bordered" width="100%">
				<col class="colWidth5">
				<col class="colWidth10">
				<col class="colWidth20">
				<col class="colWidth10">
				<col class="colWidth8">
				<col class="colWidth8">
				<col class="colWidth8">
				<col class="colWidth10">
				<col class="colWidth10">
				<col class="colWidth10">
				<thead>
					<tr>
						<th>UID</th>
						<th>Title</th>
						<th>Description</th>
						<th>Seller</th>
						<th>Start price</th>
						<th>Bid inc</th>
						<th>Best offer</th>
						<th>Bidder</th>
						<th>Stop date</th>
						<th>Bid</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>UID</th>
						<th>Title</th>
						<th>Description</th>
						<th>Seller</th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
				</tfoot>
			</table>
		</fieldset>
	</div>
	<div class="modal fade" id="bidListModal" data-keyboard="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Bidding History</h4>
				</div>
				<div class="modal-body">
					<table id="biddingTable" class="table table-striped table-bordered" width="100%">
						<thead>
							<tr>
								<th>&#8470;</th>
								<th>Bidder</th>
								<th>Amount</th>
								<th>Date Time</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="searchItemsModal" data-keyboard="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Items Search</h4>
				</div>
				<div class="modal-body">
					<form id="formSearchItems" action=""
							class="form-horizontal"
							data-bv-message="This value is not valid"
							data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
							data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
							data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
						<input type="hidden" name="search" value="true"/>
						<div class="form-group">
							<label for="uid" class="col-sm-3 control-label">Item UID</label>
								<div class="col-sm-6">
									<input type="text" id="uid" name="uid" class="form-control" placeholder="UID" autofocus="autofocus"/>
							</div>
						</div>
						<div class="form-group">
							<label for="title" class="col-sm-3 control-label">Title</label>
								<div class="col-sm-6">
									<input type="text" id="title" name="title" class="form-control" placeholder="Title"/>
							</div>
						</div>
						<div class="form-group">
							<label for="description" class="col-sm-3 control-label">Description</label>
								<div class="col-sm-6">
									<input type="text" id="description" name="description" class="form-control" placeholder="Description"/>
							</div>
						</div>
						<hr/>
						<div class="form-group">
							<label for="minPrice" class="col-sm-3 control-label">Min Price</label>
							<div class="row col-sm-6">
								<div class="col-sm-4">
									<input type="text" id="minPrice" name="minPrice" class="form-control" placeholder="Min"/>
								</div>
								<div>
									<label for="maxPrice" class="col-sm-4 control-label">Max Price</label>
									<div class="col-sm-4">
										<input type="text" id="maxPrice" name="maxPrice" class="form-control" placeholder="Max"/>
									</div>
								</div>
							</div>
						</div>
						<hr/>
						<div class="form-group">
							<label for="isBuyItNow" class="col-sm-3 control-label">Buy It Now</label>
							<div class="col-sm-6">
								<div class="checkbox">
									<label>
										<input type="checkbox" id="isBuyItNow" name="isBuyItNow" aria-label="..."/>&nbsp;Show Only					
									</label>
								</div>
							</div>			
						</div>
						<hr/>
						<div class="form-group">
							<label for="startDate" class="col-sm-3 control-label">Start date</label>
							<div class="row col-sm-6">
								<div class="col-sm-4">
									<input type="text" id="startDate" name="startDate" class="form-control" placeholder="Start"/>
								</div>
								<div>
									<label for="expireDate" class="col-sm-4 control-label">Expire date</label>
									<div class="col-sm-4">
										<input type="text" id="expireDate" name="expireDate" class="form-control" placeholder="Expire"/>
									</div>
								</div>
							</div>
						</div>
						<hr/>
						<div class="form-group">
							<label for="bidderCount" class="col-sm-3 control-label">Bidder count</label>
								<div class="col-sm-2">
									<input type="text" id="bidderCount" name="bidderCount" class="form-control" placeholder="Count"/>
							</div>
						</div>
						<div class="modal-footer">
							<button type="submit" class="btn btn-primary">Search</button>
							<button type="reset" class="btn btn-info" id="btnReset">Clear</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</form>
				</div>				
			</div>
		</div>
	</div>
	<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/dataTabler" src="<%=request.getContextPath()%>/js/require.js"></script>