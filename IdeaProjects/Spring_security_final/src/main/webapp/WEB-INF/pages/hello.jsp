<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<body>

	<%@include file="menu.jsp"%>


	<h1>Title : ${title}</h1>

	<c:forEach items="${users}" var="data">
		${data.username} ||| ${data.password} <br/>
	</c:forEach>


	<sec:authorize access="hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')">
		<!-- For login user -->
		<c:url value="/j_spring_security_logout" var="logoutUrl" />
		<form action="${logoutUrl}" method="post" id="logoutForm">
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
				You are : ${pageContext.request.userPrincipal.name} ==> <a
					href="javascript:formSubmit()"> Logout</a>
			</h2>
		</c:if>


	</sec:authorize>
</body>
</html>