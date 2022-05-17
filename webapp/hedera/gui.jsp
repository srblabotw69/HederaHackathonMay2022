<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>gui.jsp</title>
<link rel="stylesheet" href="./index.css"></link>
</head>
 <script src="https://code.jquery.com/jquery-1.11.1.js"></script>
<script src="./index.js"></script>
<body>

	<jsp:useBean id="guiID" class="HederaApp.GUI"/>
 
	<%-- <h3>${guiID.GUI}</h3> --%>
	
	    <div class="chat">
      <div class="side-panel">
        <h1 id="title">Hedera Chat Demo ⚡️</h1>
        <ul>
          <li><a class="panel-link" href="https://docs.hedera.com/"><img />Hedera JS Docs</a></li>
          <li><a class="panel-link"href="https://github.com/hashgraph/"><img />GitHub</a></li>
          <li><a class="panel-link" href="https://docs.hedera.com/"><img />Chat tutorial</a></li>
        </ul>
      </div>
      <div class="main-panel">
        <div class="content">
          <div class="header">
            <div class="chat-details">
              <h3 id="topic-id"> </h3>
              <h4 id="sequence-number"> </h4>
            </div>
          </div>
          <ul id="messages">
          </ul>
          <form id="form" action="">
<!--             <input id="m" autocomplete="off" placeholder="Type a message here"/><button alt="send"> </button>
 -->
             <input id="m"/><button> </button>
          
  </form>
        </div>
      </div>
    </div>
</body>
</html>

