<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<p class="btn-group-xs">
	<a href="<%=request.getContextPath()%>/items/search" type="button" class="btn btn-primary" role="button">Search</a>
	<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#searchItemsModal">Search</button>
<sec:authorize access="isAuthenticated()">
	<a href="<%=request.getContextPath()%>/items/show/all" type="button" class="btn btn-primary" role="button">Show All Items</a>
	<a href="<%=request.getContextPath()%>/items/show/my" type="button" class="btn btn-primary" role="button">Show My Items</a>
	<a href="<%=request.getContextPath()%>/items/new" type="button" class="btn btn-primary" role="button">Sell</a>
</sec:authorize>
</p>