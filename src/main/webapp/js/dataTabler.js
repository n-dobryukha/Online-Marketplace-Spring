/**
 * 
 */
require.config({
	paths: {
        'jquery': 'https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min',
        'datatables': 'https://cdn.datatables.net/1.10.7/js/jquery.dataTables',
        'integration': 'https://cdn.datatables.net/plug-ins/1.10.7/integration/bootstrap/3/dataTables.bootstrap',
        'bootstrap': 'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min'        
    },
    shim: {
    	'datatables': ['jquery'],
    	'bootstrap': ['jquery'],
    	'bootstrapValidator.min': ['jquery'],
    	'common': ['jquery']
    }
});

require(
		['jquery', 'datatables', 'integration', 'bootstrap', 'bootstrapValidator.min', 'common'],
		function( $, datatables, integration ) {
		    $(document).ready(function() {
		    	$('#dataTable tfoot th').each(
						function() {
							var title = $('#dataTable tfoot th').eq(
									$(this).index()).text();
							if (title !== "")
								$(this).html(
										'<input type="text" class="input-xs col-xs-12" placeholder="'
												+ title + '" />');
						});

				var table = $('#dataTable').DataTable({
					'dom': 'lt<"row"<"col-sm-5"i><"col-sm-7 input-group-sm"p>>',
					'searching' : true,
					'ajax' : {
						'url': '../../rest/item/?scope=' + $('#type').val().toLowerCase() + '&' + document.location.search.split('?')[1],
						'type': 'GET'
					},
					'columnDefs': [
						{'targets':0, 'data' : 'uid'},
						{'targets':1, 'data' : 'title'},
						{'targets':2, 'data' : 'description'},
						{'targets':3, 'data' : 'seller.name' },
						{'targets':4, 'data' : 'startPrice'},
						{'targets':5, 'data' : 'bidInc'},
						{
							'targets':6,
							'data' : 'bestOffer',
							'render': function(data, type, row) {
								switch (data) {
								case "":
									return "";
									break;
								default:
									return data + "&nbsp<span class='glyphicon glyphicon-list-alt' aria-hidden='true' data-toggle='modal' data-target='#bidListModal' data-whatever='" + row.uid + "'></span>";
								}
								
							}
						},
						{'targets':7, 'data' : 'bidder.name'},
						{'targets':8, 'data' : { _: 'stopDate.display', 'sort': 'stopDate.timestamp' } },
						{
							'targets': 9,
							'data': 'action',
							'render': function(data, type, row) {
								switch (data) {
								case 'bid':
									var bidInc = parseFloat(row.bidInc).toFixed(2),
										minValue = ((row.bestOffer === "") ? row.startPrice : (parseFloat(row.bestOffer) + parseFloat(bidInc)));
									return "<form method='post' data-item-id='" + row.uid + "'><div class='form-group'><div class='input-group input-group-xs'><div class='input-group-addon'>$</div><input type='number' name='amount' class='form-control' placeholder='" + minValue + "' min='" + minValue + "' step='" + bidInc + "' required='required'><span class='input-group-btn'><button class='btn btn-default' type='submit'>Bid</button></span></div></div></form>";
									break;
								case 'buy':
									return "<form method='post' data-item-id='" + row.uid + "'><input type='hidden' name='amount' value='" + row.startPrice + "'><div class='btn-group btn-group-justified'><div class='btn-group' role='group'><button class='btn btn-default btn-xs' type='submit'>Buy</button></div></div></form>"
								case 'edit':
									return "<div class='btn-group btn-group-justified' role='group' aria-label='...'>" +
												"<div class='btn-group btn-group-xs' role='group'>" +
													"<button type='edit' class='btn btn-default' value='" + row.uid +"'>Edit&nbsp;<span class='glyphicon glyphicon-edit' aria-hidden='true'></span></button>" +
												"</div>" +
												"<div class='btn-group btn-group-xs' role='group'>" +
													"<button type='delete' class='btn btn-default' value='" + row.uid +"'>Delete&nbsp;<span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button>" +
												"</div>"
  											"</div>";
									break;
								default:
									return "<div class='text-center'>" + data + "</div>";
								}								
							}
						}]
				});

				table.columns().every(function() {
					var that = this;

					$('input', this.footer()).on('keyup change', function() {
						that.search(this.value).draw();
					});
				});
				
				table.on( 'draw', function () {
					$('#dataTable').find('form').each(function() {
						var $form = $(this);
						$form.bootstrapValidator({
							message: 'This value is not valid',						
						})
						.on('success.form.bv', function(e) {
			    			e.preventDefault();
				            var $form = $(e.target),
				            	itemId = $form.data('item-id'),
				            	data = $form.serialize(),
				            	bv = $form.data('bootstrapValidator'),
				            	header = $("meta[name='_csrf_header']").attr("content"),
				            	token = $("meta[name='_csrf']").attr("content");
				            
				            if (!confirm('Are you sure?')) {
				            	$form.find('button[type="submit"]').prop('disabled', false);
				            	return;
				            }
				            	
				            $.ajax({
				            	beforeSend: function(xhr) {
				                    xhr.setRequestHeader(header, token);
				                },			                    
			                    url: "../../rest/item/" + itemId + "/bid/",
			                    type: "POST",
			                    data: data,
			                    cache: false,
			                         
			                    success: function (data, textStatus, jqXHR){
			                    	table.row( $form.closest('tr') ).data( data ).draw();
			                    },
			                         
			                    error: function (jqXHR, textStatus, errorThrown){
			                        alert("error - HTTP STATUS: "+jqXHR.status);
			                    },
			                         
			                    complete: function(jqXHR, textStatus){			                        
			                    }                    
			                });
			    		});
					})
					$('#dataTable').find('button[type="edit"]').each(function() {
						$(this).on('click', function() {
							document.location.href = '../edit/' + this.value;
						});
					})
					$('#dataTable').find('button[type="delete"]').each(function() {
						$(this).on('click', function() {
							var itemId = this.value,
								row = $(this).closest('tr'),
				            	header = $("meta[name='_csrf_header']").attr("content"),
				            	token = $("meta[name='_csrf']").attr("content");

							if (!confirm('Are you sure?')) {
								return;
							}
							
							$.ajax({
								beforeSend: function(xhr) {
				                    xhr.setRequestHeader(header, token);
				                },
			                    url: "../../rest/item/" + itemId,
			                    type: "DELETE",
			                    cache: false,

			                    success: function (data, textStatus, jqXHR){
			                    	switch (data.status) {
			                        case "EXCEPTION":
			                        	alert("error: " + data.errorMsg);
			                        	break;
			                        default:
			                        	table.row( row ).remove().draw(false);
			                        	break;
			                    	}
			                    },
			                         
			                    error: function (jqXHR, textStatus, errorThrown){
			                        alert("error - HTTP STATUS: "+jqXHR.status);
			                    },
			                         
			                    complete: function(jqXHR, textStatus){			                        
			                    }                    
			                });
						});
					})
				});
				
				$('#bidListModal').on('show.bs.modal', function (event) {
					var itemId = $(event.relatedTarget).data('whatever');
					var table;
					if ( $.fn.dataTable.isDataTable( '#biddingTable' ) ) {
					    table = $('#biddingTable').DataTable();
					    table.ajax.url( '../../rest/item/' + itemId + '/bid/' ).load();
					}
					else {
						table = $('#biddingTable').DataTable({
							'retrieve': true,
							'ajax' : {
								'url': '../../rest/item/' + itemId + '/bid/',
								'type': 'GET'
							},
							'columns': [
								{ data : 'count'},
								{ data : 'bidder.name'},
								{ data : 'amount'},
								{ data : { _: 'ts.display', 'sort': 'ts.timestamp' } },
							]
						});
					}
				})
				
				$('#searchItemsModal').on('show.bs.modal', function (event) {
					var $form = $('#formSearchItems'),
						bv = $form.data('bootstrapValidator'),
						fields = $form.serializeObject(),
						isLocalStorage = isLocalStorageAvailable(),
						isSubmitDisabled = true;
					
					for (field in fields) {
						var $field = $('#'+field);
						if (isLocalStorage) {
							$field.val(localStorage.getItem('onlinemarketplace.itemsearch.'+field));
						}
						if ($field.val() !== "") {
							isSubmitDisabled = false;
						}
					}
					if (isLocalStorage) {
						$('#isBuyItNow').prop('checked', (localStorage.getItem('onlinemarketplace.itemsearch.isBuyItNow') === 'true'));
					}
					if ($('#isBuyItNow').prop('checked')) {
						isSubmitDisabled = false;
					}					
					$('#btnSearchSubmit').prop('disabled', isSubmitDisabled);
				});
				
				$('#formSearchItems').bootstrapValidator({
					fields : {
						uid: {
							validators: {
								digits: {}
							}
						},
						title: {
							validators: {
								stringLength: {
									min: 3
								}
							}
						},
						description: {
							validators: {
								stringLength: {
									min: 3
								}
							}
						},
						minPrice: {
							validators: {
								numeric: {}
							}
						},
						maxPrice: {
							validators: {
								numeric: {}
							}
						},
						startDate: {
							validators: {
								date: {
									format: 'YYYY-MM-DD H:M'
								}
							}
						},
						expireDate: {
							validators: {
								date: {
									format: 'YYYY-MM-DD H:M'
								}
							}
						},
						bidderCount: {
							validators: {
								digits: {}
							}
						}
					}
				})
				.on('success.field.bv', function(e, data) {
					if (data.bv.$invalidFields.length === 0) {
						var fields = $(this).serializeObject(),
							isSubmitDisabled = true;
						for (field in fields) {
							if (fields[field] !== "") {
								isSubmitDisabled = false;
								break;
							}
						}
						data.bv.disableSubmitButtons(isSubmitDisabled);
					}
				})
				.on('status.field.bv', function(e, data) {
					if (data.element.val() === "") data.element.parents('.form-group').removeClass('has-success');
		        })
				.on('success.form.bv', function(e) {
					e.preventDefault();
					var $form = $(this),
						fields = $form.serializeObject();
					if (isLocalStorageAvailable()) {
						for (field in fields) {
							localStorage.setItem('onlinemarketplace.itemsearch.'+field, fields[field]);
						}
						localStorage.setItem('onlinemarketplace.itemsearch.isBuyItNow', $('#isBuyItNow').prop('checked'));
					}
					$('#searchItemsModal').modal('hide');
					var table = $('#dataTable').DataTable();
				    table.ajax.url( '../../rest/item/?scope=' + $('#type').val().toLowerCase() + '&search=true&' + $form.serialize() ).load();
				})
				
				$('#isBuyItNow').on('click', function() {
					var $this = $(this),
						$form = $this.closest('form'),
						fields = $form.serializeObject(),
						isSubmitDisabled = true;
					if (!$this.prop('checked')) {
						if ($form.data('bootstrapValidator').$invalidFields.length === 0) {
							for (field in fields) {
								if (fields[field] !== "") {
									isSubmitDisabled = false;
									break;
								}
							}
						}
					} else {
						isSubmitDisabled = false;
					}
					$('#btnSearchSubmit').prop('disabled', isSubmitDisabled);
				});
				
				$('#btnReset').click(function() {
					var $form = $(this).closest('form'),
						fields = $form.serializeObject(),
						table = $('#dataTable').DataTable();
					if (isLocalStorageAvailable()) {
						for (field in fields) {
							localStorage.removeItem('onlinemarketplace.itemsearch.'+field);
						}
						localStorage.removeItem('onlinemarketplace.itemsearch.isBuyItNow');
					}
					$form.data('bootstrapValidator').resetForm(true);
					$('#searchItemsModal').modal('hide');
					table.ajax.url( '../../rest/item/?scope=' + $('#type').val().toLowerCase() + '&search=false' ).load();
				})
			})
		}
);