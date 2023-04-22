<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>User Home Page</title>
  <link href="https://fonts.googleapis.com/css?family=Overpass:400,700|Roboto:400,700" rel="stylesheet">
  <style>
    body {
      font-family: 'Roboto', sans-serif;
      background-color: #D0DCD0;
      color: #2B3B2B;
    }
    h1, h2 {
      font-family: 'Overpass', sans-serif;
      color: #111711;
    }
    .container {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
    }
    .column {
      flex-basis: 48%;
      margin-bottom: 20px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      background-color: #D1E9C3;
    }
    th, td {
      border: 1px solid #2B3B2B;
      padding: 8px;
      text-align: left;
    }
    th {
      background-color: #2B3B2B;
      color: #D1E9C3;
    }
    button {
      background-color: #2B3B2B;
      color: #D1E9C3;
      border: none;
      cursor: pointer;
      padding: 6px;
    }
  </style>
</head>
<body>
<h1><spring:message code="greeting" /></h1>

<div class="container">
  <div class="column">
    <h2><spring:message code="my_books" /></h2>
    <table>
      <thead>
      <tr>
        <th><spring:message code="image" /></th>
        <th><spring:message code="book_name" /></th>
        <th><spring:message code="author" /></th>
        <th><spring:message code="isbn" /></th>
        <th><spring:message code="language" /></th>
        <th><spring:message code="description" /></th>
        <th><spring:message code="status" /></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${myBooks}" var="book">
        <tr>
          <td><img src="${book.image}" alt="${book.name}" /></td>
          <td>${book.name}</td>
          <td>${book.author}</td>
          <td>${book.isbn}</td>
          <td>${book.language}</td>
          <td>${book.description}</td>
          <td>
            <form action="/changeStatus" method="post">
              <input type="hidden" name="bookId" value="${book.id}" />
              <button type="submit">${book.status ? 'Public' : 'Private'}</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <div class="column">
    <h2><spring:message code="borrowed_books" /></h2>
    <table>
      <thead>
      <tr>
        <th><spring:message code="image" /></th>
        <th><spring:message code="book_name" /></th>
        <th><spring:message code="author" /></th>
        <th><spring:message code="return_date" /></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${borrowedBooks}" var="book">
        <tr>
          <td><img src="${book.image}" alt="${book.name}" /></td>
          <td>${book.name}</td>
          <td>${book.author}</td>
          <td>${book.returnDate}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<div>
  <h2><spring:message code="lended_books" /></h2>
  <table>
    <thead>
    <tr>
      <th><spring:message code="image" /></th>
      <th><spring:message code="book_name" /></th>
      <th><spring:message code="author" /></th>
      <th><spring:message code="isbn" /></th>
      <th><spring:message code="language" /></th>
      <th><spring:message code="description" /></th>
      <th><spring:message code="expected_retrieval_date" /></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${lendedBooks}" var="book">
      <tr>
        <td><img src="${book.image}" alt="${book.name}" /></td>
        <td>${book.name}</td>
        <td>${book.author}</td>
        <td>${book.isbn}</td>
        <td>${book.language}</td>
        <td>${book.description}</td>
        <td>${book.expectedRetrievalDate}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>