<%@page import="java.util.List,pers.model.Message"%>
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Mini Blog</title>
<link rel='stylesheet' href='../css/member.css' type='text/css'>
</head>
<body>

    <div class='leftPanel'>
        <img src='../images/caterpillar.jpg' alt='Mini Blog' /><br>
        <br>${requestScope.username} 的微網誌
    </div>
    <table border='0' cellpadding='2' cellspacing='2'>
        <thead>
            <tr>
                <th><hr></th>
            </tr>
        </thead>
        <tbody>

            <%
                List<Message> messages = (List<Message>) request.getAttribute("messages");
                for(Message message : messages) {                    
            %>
            
            <tr>
                <td style='vertical-align: top;'><%= message.getUsername() %><br>
                    <%= message.getBlabla() %><br> <%= message.getLocalDateTime() %>
                    <hr>
                </td>
            </tr>
            
            <%
                }
            %>

        </tbody>
    </table>
    <hr>
</body>
</html>