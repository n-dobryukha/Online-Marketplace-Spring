/**
 * 
 */
require.config({
	paths: {
        'jquery': 'https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min',
        'bootstrap': 'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min'
    },
    shim: {
    	'bootstrapValidator.min': ['jquery'],
    	'bootstrap': ['jquery']
    }
});

require(
	['jquery', 'bootstrap', 'bootstrapValidator.min'],
	function( $, bootstrap, bootstrapValidator ){
		$.fn.serializeObject = function()
		{
		    var o = {};
		    var a = this.serializeArray();
		    $.each(a, function() {
		        if (o[this.name] !== undefined) {
		            if (!o[this.name].push) {
		                o[this.name] = [o[this.name]];
		            }
		            o[this.name].push(this.value || '');
		        } else {
		            o[this.name] = this.value || '';
		        }
		    });
		    return o;
		};
		
		$(document).ready(function() {
    		$('#formLogin').bootstrapValidator({
    			fields: {
    				login: {
    					validators: {
    						blank: {}
    					}    						
    				},
    				password: {
    					validators: {
    						blank: {}
    					}    						
    				}
    			}
    		})
    		.on('success.form.bv', function(e) {
    			e.preventDefault();
	            var $form = $(e.target),
	            	bv = $form.data('bootstrapValidator'),
	            	header = $("meta[name='_csrf_header']").attr("content"),
	            	token = $("meta[name='_csrf']").attr("content");
	            
	            $.ajax({
	            	beforeSend: function(xhr) {
	                    xhr.setRequestHeader(header, token);
	                },
                    url: this.action,
                    type: this.method,
                    data: $form.serialize(),
                    cache: false,
                         
                    success: function (data, textStatus, jqXHR){
                        switch (data.status) {
                        case "FAIL":
                        	bv.updateStatus(data.field,"INVALID","blank")
                    		bv.updateMessage(data.field,"blank",data.errorMsg)
                        	break;
                        case "EXCEPTION":
                        	alert("error: " + data.errorMsg);
                        	break;
                        default:
                        	window.location.replace("./items/show/all");
                        }
                    },
                         
                    error: function (jqXHR, textStatus, errorThrown){
                        alert("error - HTTP STATUS: "+jqXHR.status);
                    }                    
                });
    		});

    		$('#formRegistration').bootstrapValidator({
    			fields : {
    				login : {
    					validators: {
    						blank: {},
    	                    stringLength: {
    	                        min: 6
    	                    },
    	                    regexp: {
    	                        regexp: /^[a-zA-Z0-9_\.]+$/,
    	                        message: 'The login can only consist of alphabetical, number, dot and underscore'
    	                    },	                    
    	                    different: {
    	                        field: 'password,confirmPassword',
    	                        message: 'The login and password cannot be the same as each other'
    	                    }
    	                }
    				},
    				password : {
    					validators: {
    						stringLength: {
    	                        min: 6
    	                    },
    	                    identical: {
    	                        field: 'confirmPassword',
    	                        message: 'The password and its confirm are not the same'
    	                    },
    	                    different: {
    	                        field: 'login',
    	                        message: 'The password cannot be the same as login'
    	                    }
    	                }
    				},
    				confirmPassword: {
    	                validators: {
    	                	stringLength: {
    	                        min: 6
    	                    },
    	                    identical: {
    	                        field: 'password',
    	                        message: 'The password and its confirm are not the same'
    	                    },
    	                    different: {
    	                        field: 'login',
    	                        message: 'The password cannot be the same as login'
    	                    }
    	                }
    	            }
    			}
    		})
    		.on('success.form.bv', function(e) {
    			e.preventDefault();
	            var $form = $(e.target),
	            	bv = $form.data('bootstrapValidator'),
	            	formData = $form.serializeObject(),
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
	            	url: this.action,
                    type: this.method,
                    data: JSON.stringify(formData),
                    cache: false,
                    datatype: 'json',
                    contentType: "application/json;charset=UTF-8",
                         
                    success: function (data, textStatus, jqXHR){
                    	switch (jqXHR.status) {
						case 201:
							$.ajax({
								url: './login',
								type: 'POST',
								data: this.data,
								cache: false,
			                    datatype: 'json',
			                    contentType: "application/json;charset=UTF-8",
			                         
			                    success: function (data, textStatus, jqXHR){
			                    	window.location.replace("./items/show/all");
			                    }
							})
							break;
						default:
							switch (data.status) {
	                        case "WRONGPARAM":
	                        	var fieldErrors = data.fieldErrors;
	                        	for (var fieldName in data.fieldErrors) {
	                        		bv.updateStatus(fieldName,"INVALID","blank");
	                        		bv.updateMessage(fieldName,"blank",fieldErrors[fieldName]);
	                        	}
	                        default:
	                        	$form.find('button[type="submit"]').prop('disabled', false);
	                        	break;
	                        }
							break;
						}
                    },
                         
                    error: function (jqXHR){
                    	switch (jqXHR.status) {
						case 409:
							bv.updateStatus("login","INVALID","blank");
                    		bv.updateMessage("login","blank","Login already exists.");
                    		$form.find('button[type="submit"]').prop('disabled', false);
                    		break;
						default:
							alert("ERROR\r\nHTTP STATUS: "+jqXHR.status+" - "+jqXHR.statusText);
							$form.find('button[type="submit"]').prop('disabled', false);
                        	break;
                        }                    	
                    }                    
                });
    		});
    		
    		$('#formEditItem')
			.bootstrapValidator({
				fields : {
					title: {
						validators: {
							blank: {}
						}
					},
					description: {
						validators: {
							blank: {}
						}
					},
					startPrice : {
						validators: {
							blank: {},
							numeric: {},
							greaterThan: {
		                        inclusive: false
		                    }
		                }
					},
					timeLeft: {
						validators: {
							blank: {}
						}
					},
					bidIncrement : {
						validators: {
							blank: {},
							numeric: {},
							greaterThan: {
		                        inclusive: false
		                    }
		                }
					}
				}
			})
			.on('change', 'input[type="checkbox"][name="buyItNow"]', function() {
				var isBuyItNow = $(this).is(':checked'),
					bootstrapValidator = $('#formEditItem').data('bootstrapValidator');
				bootstrapValidator.enableFieldValidators('bidIncrement', !isBuyItNow);
				if (isBuyItNow) {
					$('#bidIncrement')
						.val('')
						.prop('disabled',true)
						.parents('.form-group').hide();
				} else {
					$('#bidIncrement')
						.prop('disabled',false)
						.parents('.form-group').show();
				}
			})
			.on('success.form.bv', function(e) {
    			e.preventDefault();
    			
	            var $form = $(e.target),
	            	bv = $form.data('bootstrapValidator'),
	            	data = $form.serializeObject(),
	            	header = $("meta[name='_csrf_header']").attr("content"),
	            	token = $("meta[name='_csrf']").attr("content"),
	            	method = $("#_method").val();
		        
	            if (!confirm('Are you sure?')) {
	            	$form.find('button[type="submit"]').prop('disabled', false);
	            	return;
	            }
	            
	            $.ajax({
	            	beforeSend: function(xhr) {
	            		xhr.setRequestHeader(header, token);
	            	},
	            	url: this.action,
                    type: method,
                    data: JSON.stringify(data),
                    cache: false,
                    datatype: 'json',
                    contentType: "application/json; charset=utf-8",
                         
                    success: function (data, textStatus, jqXHR){
                        switch (data.status) {
                        case "SUCCESS" :
                            if ($('#id').val() === '') window.location.replace("./show/my");
                            else window.location.replace("../show/my");
                        	break;
                        case "WRONGPARAM":
                        	var fieldErrors = data.fieldErrors;
                        	for (var fieldName in data.fieldErrors) {
                        		bv.updateStatus(fieldName,"INVALID","blank");
                        		bv.updateMessage(fieldName,"blank",fieldErrors[fieldName]);
                        	}
                        	break;
                        case "EXCEPTION":
                        	alert("error: " + data.errorMsg);
                        	break;
                        default:
                        	break;
                        }
                    },
                         
                    error: function (jqXHR, textStatus, errorThrown){
                        alert("error - HTTP STATUS: "+jqXHR.status);
                    },
                         
                    complete: function(jqXHR, textStatus){
                        //alert("complete");
                    }                    
                });
    		});
			
			$('#btnReset').click(function() {
    	        $(this).parents('form').data('bootstrapValidator').resetForm(true);
    	        $('#buyItNow').prop('checked', false)
    	        $('#bidIncrement')
    	        	.prop('disabled',false)
					.parents('.form-group').show();
    	    });
    	})
    }
);