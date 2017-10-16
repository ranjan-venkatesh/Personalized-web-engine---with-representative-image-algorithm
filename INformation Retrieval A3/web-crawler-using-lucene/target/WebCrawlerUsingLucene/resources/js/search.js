/**
 * 
 */
// Function that renders the list items from our records
var exrecords; 
$( document ).ready(function() {
	$( "#queryForm" ).submit(function( event ) {
		
		  // Stop form from submitting normally
		  event.preventDefault();
		  // Get some values from elements on the page:
		  var $form = $( this ),
		    urlTerm = $('#urlInput').val(),
		    url = $form.attr( "action" ),
		  	queryTerm = $("#keywordInput").val(),
		  	depthTerm = $("#crawlDepth").val(),
		  	userName = $("#userName").text();
		  
		  var csrfToken = $("input[name='_csrf']").val();
			  var headers = {};
			  headers['X-CSRF-TOKEN'] = csrfToken;
		  // Send the data using post
		  var posting = $.ajax( {url: url,
			  beforeSend: function()
			    {
				  $.blockUI({ message: '<h2><img src="resources/images/busy.gif" /> Crawling in Progress..Please Wait</h2>' }); 
			    },
			    type: 'post',
			    data: {
			    	url: urlTerm ,query:queryTerm,depth :depthTerm,user : userName
			    },
			    headers: headers
			       });
		 
		  // Put the results in a div
		  posting.done(function( data ) {
	        setTimeout($.unblockUI, 20);
	        console.log(data);
	        exrecords = data;
		    dynaTableDraw();
		  });
		});
	
	
	
});

function dynaTableDraw() {
	//console.log(exrecords);
	 var dynatable=$('#ul-example').dynatable({
		  table: {
		    bodyRowSelector: 'li'
		  },
		  writers: {
		    _rowWriter: ulWriter
		  },
		  readers: {
		    _rowReader: ulReader
		  },
		  dataset:{
			  records : JSON.parse(exrecords)
		  }
		}).data("dynatable");
	 
	 dynatable.settings.dataset.originalRecords =  JSON.parse(exrecords);
     dynatable.process(); 
	}

function ulWriter(rowIndex, record, columns, cellWriter) {
	  var cssClass = "span4", li;
	  if (rowIndex % 3 === 0) { cssClass += ' first'; }
	  li = '<li class="' + cssClass + '">'+'<div class="thumbnail row"><div class="col-md-3 pull-md-9" style="vertical-align: middle;"><img src="'+record.imageUrl+'" height="100" width="150"></div><div class="caption col-md-9 push-md-3"><h3 class="title">'+record.title+'</h3><p class="summary">'+record.summary+'</p><p class="url">'+record.url+'</p><p><a href="'+record.url+'" class="btn btn-primary">Goto</a></p></div></div></li>';
	  return li;
	}


	// Function that creates our records from the DOM when the page is loaded
	function ulReader(index, li, record) {
	  var $li = $(li);
	  record.imageUrl = $li.find('img').attr("src");
	  record.url = $li.find('.url').text();;
	  record.title = $li.find('.title').text();
	  record.summary = $li.find('.summary').text();
	}