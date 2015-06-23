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
								<th>№</th>
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
	<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/dataTabler" src="<%=request.getContextPath()%>/js/require.js"></script>