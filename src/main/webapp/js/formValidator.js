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
	            	bv = $form.data('bootstrapValidator');
	            
	            $.ajax({
                    url: this.action,
                    type: this.method,
                    data: $form.serialize(),
                    cache: false,
                    datatype: 'json',
                         
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
	            	data = $form.serializeObject();
	            
	            $.ajax({
	            	url: this.action,
                    type: this.method,
                    data: JSON.stringify(data),
                    cache: false,
                    datatype: 'json',
                    contentType: "application/json",
                         
                    success: function (data, textStatus, jqXHR){
                        //alert("success");
                        switch (data.status) {
                        case "SUCCESS" :
                            window.location.replace("./items/show/all");
                        	break;
                        case "WRONGPARAM":
                        	var fieldErrors = data.fieldErrors;
                        	for (var fieldName in data.fieldErrors) {
                        		bv.updateStatus(fieldName,"INVALID",fieldErrors[fieldName]);
                        	}
                        	break;
                        case "EXISTSLOGIN":
                        	bv.updateStatus("login","INVALID","blank");
                        	bv.updateMessage("login","blank",data.errorMsg);
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
    		
    		$('#formEditItem')
			.bootstrapValidator({
				fields : {
					startPrice : {
						validators: {
							numeric: {},
							greaterThan: {
		                        inclusive: false
		                    }
		                }
					},
					bidIncrement : {
						validators: {
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
	            	data = $form.serializeObject();
	            
	            if (!confirm('Are you sure?')) {
	            	$form.find('button[type="submit"]').prop('disabled', false);
	            	return;
	            }
	            
	            $.ajax({
                    url: this.action,
                    type: this.method,
                    data: JSON.stringify(data),
                    cache: false,
                    datatype: 'json',
                    contentType: "application/json",
                         
                    success: function (data, textStatus, jqXHR){
                        switch (data.status) {
                        case "SUCCESS" :
                            if ($('#itemId').val() === '') window.location.replace("./show/my");
                            else window.location.replace("../show/my");
                        	break;
                        case "WRONGPARAM":
                        	var fieldErrors = data.fieldErrors;
                        	for (var fieldName in data.fieldErrors) {
                        		bv.updateStatus(fieldName,"INVALID",fieldErrors[fieldName]);
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