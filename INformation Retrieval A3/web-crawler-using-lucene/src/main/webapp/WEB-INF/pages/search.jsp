<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Search</title>
 <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link href="<c:url value="/resources/css/bootstrap/bootstrap.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/jquery.dynatable.css"/>"  rel="stylesheet">
    <link href="<c:url value="/resources/css/search.css"/>"  rel="stylesheet">
     <script src="<c:url value="/resources/js/jquery-3.1.1.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.dynatable.js" />"></script>
    
    <script src="<c:url value="/resources/js/search.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.blockUI.js" />"></script>
   </head>
<body>
	<%-- <h1>Title : ${title}</h1>	
	<h1>Message : ${message}</h1> --%>
	<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container"> 
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span> 
            </button>
            <a target="_blank" href="#" class="navbar-brand">Web Crawler</a>
        </div>
        
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-user"></span> 
                        <strong><c:if test="${pageContext.request.userPrincipal.name != null}">
	 ${pageContext.request.userPrincipal.name}
	</c:if></strong>
                        <span class="glyphicon glyphicon-chevron-down"></span>
                    </a>
                    
                    <ul class="dropdown-menu">
                        <li>
                            <div class="navbar-login">
                                <div class="row">
                                    <div class="col-lg-4">
                                        <p class="text-center">
                                            <span class="glyphicon glyphicon-user icon-size"></span>
                                        </p>
                                    </div>
                                    <div class="col-lg-8">
                                        <p class="text-left" id="userName"><strong>
	<c:if test="${pageContext.request.userPrincipal.name != null}">
	 ${pageContext.request.userPrincipal.name}
	</c:if>
	</strong></p>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="navbar-login navbar-login-session">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <p>
                                        <form action="<c:url value='/j_spring_security_logout' />" method="post" id="logoutForm">
                                        <button type="submit" class="btn btn-danger btn-block">Logout</button>
											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
										</form>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        </div>
        <div class="search-box"> 

    <div class="search">
        <div class="container">
          <div class="row">
            <div class="col-md-10 col-md-offset-1">
              <div class="form-section">
                <div class="row">
                    <form role="form" id="queryForm" action="/web-crawler-using-lucene/queryForm">
                      <div class="col-md-4">
                        <div class="form-group">
                          <label class="sr-only" for="urlInput">URL</label>
                          <input type="text" class="form-control" id="urlInput" placeholder="Enter URL ">
                        </div>
                      </div>
                     <div class="col-md-4">
                        <div class="form-group">
                          <label class="sr-only" for="keyword">Keyword</label>
                          <input type="text" class="form-control" id="keywordInput" placeholder="Enter Search Query ">
                        </div>
                      </div>
                      <div class="col-md-2">
                        <div class="form-group">
                          <label class="sr-only" for="depth">CrawlDepth</label>
                          <select id="crawlDepth" name="crawlDepth" class="form-control">
                              <option value="1">1</option>
                              <option value="2">2</option>
                              <option value="3">3</option>
                              <option value="4">4</option>
                              <option value="5">5</option>
                          </select>
                        </div>
                      </div>
                      	<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
                      <div class="col-md-2">
                        <button type="submit" class="btn btn-default btn-primary">Search</button>
                      </div>
                    </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
</div>
     </div>
     <div class="container" style="padding-top:95px;">
     <ul id="ul-example" class="row-fluid">
 
  <!-- ... //-->
</ul>
	</div>
	</body>
</html><%-- <body>
	<h1>Title : ${title}</h1>
	<h1>Message : ${message}</h1>

	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<form action="/j_spring_security_logout" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

	<c:if test="${pageContext.request.userPrincipal.name != null}">
		<h2>
			Welcome : ${pageContext.request.userPrincipal.name} | <a
				href="javascript:formSubmit()"> Logout</a>
		</h2>
	</c:if>

</body>
 --%>