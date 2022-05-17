<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.json.JSONObject"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">


<link href="/scss/customBootstrap.css" rel="stylesheet" type="text/css">
<link href="/scss/_variables.scss" rel="stylesheet" type="text/scss">

<%-- <%@ import "scss/_variables.scss" %>
 --%>



<title>Asset Token Tree</title>
</head>
<body>

	<!-- Optional JavaScript; choose one of the two! -->

	<!-- Option 1: Bootstrap Bundle with Popper -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
		crossorigin="anonymous"></script>

	<!-- Option 2: Separate Popper and Bootstrap JS -->
	<!--
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
    -->

	<%
	String accountServiceFormAction = "http://localhost:8080/lib/AccountServiceServlet";
	String contractServiceCreateFormAction = "http://localhost:8080/lib/SmartContractServiceERC1400Servlet_createContract";
	String contractServiceCreateCallContractFormAction = "http://localhost:8080/lib/SmartContractServiceERC1400Servlet_callContract";
	String tokenServiceMintFormAction = "http://localhost:8080/lib/MintTokenUsingHTSServlet";
	String tokenServiceDeleteFormAction = "http://localhost:8080/lib/MintTokenUsingHTSServlet_deleteToken";
	String fileHandlingeFormAction = "http://localhost:8080/lib/FileUploadServlet";
	%>

	<br>
	<div class="container">
		<div class="shadow p-1 mb-3 bg-body rounded ">
			<div class="p-1 mb-0 bg-warning text-dark $yellow-100 bg-opacity-50 ">
				<nav class="navbar navbar-expand-lg">
				<div class="container-fluid">
					<a class="navbar-brand text-dark" href="/lib/hedera/HederaMain.jsp">
						<h1 class="display-6">
							<img src="/lib/hedera/assets/tokentree.png" alt="TokenTree"
								width="40" height="35" class="d-inline-block align-text-top">
							Asset Token Tree
						</h1>
					</a>
					<div class="col-6">

						<a class="nav-link" href="https://hedera.com"><h6>
								Powered by <img src="/lib/hedera/assets/hedera.png"
									alt="TokenTree" width="40" height="40"
									class="d-inline-block align-text-middle">
							</h6></a>
					</div>
					<button class="navbar-toggler" type="button"
						data-bs-toggle="collapse" data-bs-target="#navbarNav"
						aria-controls="navbarNav" aria-expanded="false"
						aria-label="Toggle navigation">
						<span class="navbar-toggler-icon"></span>
					</button>
					<!-- 					<div class="collapse navbar-collapse" id="navbarNav">
						<ul class="nav nav-tabs">
							<li class="nav-item"><a class="nav-link" href="/lib/hedera/HederaMain.jsp">Home</a></li>
							<li class="nav-item"><a class="nav-link active"
								href="/lib/hedera/HederaMain.jsp">Dashboard</a></li>
						</ul>
						<ul class="navbar-nav">
							<li class="nav-item "><a class="nav-link active"
								aria-current="page" href="/lib/hedera/HederaMain.jsp"> <h3>Dashboard</h3></a>
							</li>
						</ul> 
					</div> -->

					<ul class="nav nav-pills">
						<li class="nav-item border border-primary rounded"><a
							class="nav-link disabled" href="/lib/hedera/HederaMain.jsp">Dashboard</a></li>
						<li class="nav-item"><a class="nav-link"
							href="/lib/hedera/HederaMain.jsp">Logout</a></li>
					</ul>
					<!-- <a href="/hedera/HederaMain.asp"><h6>Home</h6></a>
					<h1 class="display-6">Token Beet</h1>  -->
				</div>
			</div>
		</div>
		<div class="container">
			<div class="shadow p-3 mb-3 bg-body rounded">
				<div class="row g-2">
					<div class="col-6">
						<div class="p-1 border bg-light">
							<div class="shadow-sm p-1 mb-3 bg-body rounded">
								<div class="bg-success p-2 text-dark bg-opacity-10">
									<h5>1. Account Service</h5>
								</div>
							</div>
							<form action=<%=accountServiceFormAction%> method="post">
								&nbsp;Login to see your token(s): &nbsp; <input type="submit"
									name="myButton" value="Login" class="btn btn-primary btn-sm" />
							</form>
							&nbsp;<b>Treasury Id:</b>&nbsp;&nbsp;${treasuryId}<br>
								&nbsp; <b> Account Id:</b>&nbsp;&nbsp;${accId}
						</div>
					</div>
					<div class="col-6">
						<div class="p-1 border bg-light">
							<div class="shadow-sm p-1 mb-3 bg-body rounded">
								<div class="bg-success p-2 text-dark bg-opacity-10">
									<h5>2. Contract Service</h5>
								</div>
							</div>
							<!-- <form action="http://localhost:8080/lib/HederaMainServlet"
								method="post">
								&nbsp;Create a contract: <input type="submit" name="myButton"
									value="Create" class="btn btn-warning btn-sm" />
							</form> -->
							<form action=<%=contractServiceCreateFormAction%> method="post">
								&nbsp;&nbsp;I. Create a contract:&nbsp; <input type="submit" name="myButton"
									value="Create" class="btn btn-warning btn-sm" />
							</form>
							<br>
							<form action=<%=contractServiceCreateCallContractFormAction%>
								method="post">
								&nbsp;&nbsp;II. Run a contract with Id:&nbsp; <input type="text" name="contractIdInput" value="${contractId}" size="12"></input> &nbsp; <input type="submit" name="myButton"
									value="Run" class="btn btn-info btn-sm" />


								<!-- 					<button type="button" class="beta">Click Me</button>
 -->
							</form>
						</div>
					</div>
					<div class="col-6">
						<div class="p-1 border bg-light">
							<div class="shadow-sm p-1 mb-3 bg-body rounded">
								<div class="bg-success p-2 text-dark bg-opacity-10">
									<h5>3. Token Service</h5>
								</div>
							</div>
							<form action=<%=tokenServiceMintFormAction%> method="post">
								&nbsp;I. Mint a token using HTS:&nbsp;&nbsp;<input type="submit"
									name="myButton" value="Mint" class="btn btn-success btn-sm" />
							</form>
							<br>
							<form action=<%=tokenServiceDeleteFormAction%> method="post">
								&nbsp;II. Delete a token using HTS: &nbsp;
								<!------------------------------------------------->
								<!--  <input type="text" name="tokenId"></input> 
 -->
								<!------------------------------------------------->

								<select name="category" style="width: 125px">
									<option value=""></option>
									<c:forEach items="${products}" var="prod">
										<option value="${prod.id}">${prod.id}</option>
									</c:forEach>
								</select>

								<!------------------------------------------------->
								<%-- 								<div class="dropdown">
									<button class="btn btn-secondary dropdown-toggle" type="button"
										id="category" data-bs-toggle="dropdown" aria-expanded="false">
										Dropdown button</button>
									<ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
										<c:forEach items="${products}" var="prod">
											<li><a class="${prod.id}" href="${prod.id}">${prod.id}</a></li>
										</c:forEach>
									</ul>
								</div> --%>
								<!------------------------------------------------->
								&nbsp;<input type="submit" name="myButton" value="Delete"
									class="btn btn-danger btn-sm" />
							</form>
						</div>
					</div>
					<div class="col-6">
						<div class="p-1 border bg-light">
							<div class="shadow-sm p-1 mb-3 bg-body rounded">
								<div class="bg-success p-2 text-dark bg-opacity-10">
									<h5>4. File Handling</h5>
								</div>
							</div>
							<div class="container">
								<div class="row">
									<div class="col">
										<%-- <form action=<%=fileHandlingeFormAction%> method="post" enctype="application/x-www-form-urlencoded"> --%>
										<form action=<%=fileHandlingeFormAction%> method="post"
											enctype="multipart/form-data">
											<div class="d-grid gap-1 col-1 ">
												<input type="file" name="file" size="200" /> <input
													type="submit" name="upload" value="Upload"
													class="btn btn-dark btn-sm col-3" />
											</div>
										</form>
									</div>
									<div class="col">
										<a class="nav-link" href="https://cloud.google.com"> <img
											src="/lib/hedera/assets/googleCloudPlatform.png"
											alt="TokenTree" width="40" height="35"
											class="d-inline-block align-text-middle">&nbsp;Google
											Cloud
										</a>
									</div>
								</div>
								<br>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="container">
			<div class="shadow p-3 mb-3 bg-body rounded">
				<div class="row g-2">
					<div class="col-12">
						<div class="p-3 border bg-light">
							<!------------------------------------------------------->
							<div class="row">
								<!------------------------------------------------------->


								<div class="col-4">
									<div class="p-3 border bg-light">

<h6>
											<b>Tokens:</b> 
										</h6>
										
										<%-- 		<c:forEach var="tokens" items="${strObjList}">
											<p>${tokens}</p>
										</c:forEach> --%>

										<c:if test="${not empty tokenDetails}">
											<table class="table">
												<thead>
													<tr>
														<th scope="col">Token Id</th>
														<th scope="col">Token Symbol</th>
														<th scope="col">Docs</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach var="tokenDetails" items="${tokenDetails}">
														<tr>
															<td>${tokenDetails.id}</td>
															<td>${tokenDetails.symbol}</td>
															<td>${tokenDetails.numOfDocs} 
															
															
																<c:choose>
																	<c:when test="${tokenDetails.numOfDocs==0}">
																		<a href="/lib/hedera/samplefiles/zerofile.txt"> <img
																			src="/lib/hedera/assets/document_red.png"
																			alt="document" width="20" height="20"
																			class="d-inline-block align-text-middle">
																		</a>
																	</c:when>
																	<c:when test="${tokenDetails.numOfDocs==1}">
																		<a href="/lib/hedera/samplefiles/samplefile.txt">
																			<img src="/lib/hedera/assets/document_white.png"
																			alt="document" width="20" height="20"
																			class="d-inline-block align-text-middle">
																		</a>
																	</c:when>
																	<c:when test="${tokenDetails.numOfDocs%2==0}">
																		<a href="/lib/hedera/samplefiles/samplefile.txt">
																			<img src="/lib/hedera/assets/document_color.png"
																			alt="document" width="20" height="20"
																			class="d-inline-block align-text-middle">
																		</a>
																	</c:when>
																	<c:otherwise>
																		<a href="/lib/hedera/samplefiles/samplefile.txt">
																			<img src="/lib/hedera/assets/document_color.png"
																			alt="document" width="20" height="20"
																			class="d-inline-block align-text-middle">
																		</a>
																	</c:otherwise>
																</c:choose>



															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:if>


									</div>
								</div>


								<!------------------------------------------------------->

								<div class="col-8">

									<div class="p-3 border bg-light">

										<h6>
											<b>Console:</b>&nbsp;&nbsp; <br> <br>
											<p class="text-primary">
												<i>${data}</i>
											</p>
										</h6>
										<%-- <p>Account Info: ${accountInfo}</p> --%>
										<p>${accountInfo}</p>
										<c:forEach var="accInfo" items="${accInfo}">
											<p>${accInfo}</p>

										</c:forEach>
									</div>
								</div>
								<!------------------------------------------------------->

							</div>
							<!------------------------------------------------------->
						</div>
					</div>
				</div>
			</div>
		</div>
</body>
</html>