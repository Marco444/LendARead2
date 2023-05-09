<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .filter-button {
        background-color: #2B3B2B;
        margin-right: 10px;
        border-radius: 20px;
    }
    .filter-button-selected {
        opacity: 1;
    }

    .filter-button:not(.filter-button-selected) {
        opacity: 0.6;
    }

    .table-row-clickable {
        cursor: pointer;
        margin-bottom: 10px;
        opacity: 0.8;
    }
    .table-row-clickable:hover {
        opacity: 1;
    }

</style>
<c:choose>
    <c:when test="${isLender}">
        <div class="table-title">
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <h2><spring:message code="lended_books"/></h2>
                <div>
                    <div class="d-inline-flex">
                        <c:url var="filterUrl" value="/applyFilter"/>
                        <form action="${filterUrl}" method="get">
                            <input type="hidden" name="table" value="lended_books">
                            <input type="hidden" name="filter" value="all">
                            <button type="submit" data-bs-toggle="tooltip" data-bs-placement="top" title="Show all lended books" class="btn btn-primary filter-button ${filter == 'all' && table == "lended_books" || filter == null ? 'filter-button-selected' : ''}">
                                <spring:message code="userHomeView.all" />
                            </button>
                        </form>
                    </div>
                    <div class="d-inline-flex">
                        <c:url var="filterUrl" value="/applyFilter"/>
                        <form action="${filterUrl}" method="get">
                            <input type="hidden" name="table" value="lended_books">
                            <input type="hidden" name="filter" value="pending">
                            <button type="submit" data-bs-toggle="tooltip" data-bs-placement="top" title="Show pending lended books" class="btn btn-primary filter-button ${filter == 'pending' && table == "lended_books" ? 'filter-button-selected' : ''}">
                                <spring:message code="userHomeView.pending" />
                            </button>
                        </form>
                    </div>
                    <div class="d-inline-flex">
                        <c:url var="filterUrl" value="/applyFilter"/>
                        <form action="${filterUrl}" method="get">
                            <input type="hidden" name="table" value="lended_books">
                            <input type="hidden" name="filter" value="confirmed">
                            <button type="submit" data-bs-toggle="tooltip" data-bs-placement="top" title="Show in-progress lended books" class="btn btn-primary filter-button ${filter == 'confirmed' && table == "lended_books" ? 'filter-button-selected' : ''}">
                                <spring:message code="userHomeView.inProgress" />
                            </button>
                        </form>
                    </div>
                    <div class="d-inline-flex">
                        <c:url var="filterUrl" value="/applyFilter"/>
                        <form action="${filterUrl}" method="get">
                            <input type="hidden" name="table" value="lended_books">
                            <input type="hidden" name="filter" value="delayed">
                            <button type="submit" data-bs-toggle="tooltip" data-bs-placement="top" title="Show delayed lended books" class="btn btn-primary filter-button ${filter == 'delayed' && table == "lended_books" ? 'filter-button-selected' : ''}">
                                <spring:message code="userHomeView.delayed" />
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="table-container">
                <table class="table">
                    <thead>
                    <tr>
                        <th><spring:message code="image"/></th>
                        <th><spring:message code="book_name"/></th>
                        <th><spring:message code="expected_retrieval_date"/></th>
                        <th><spring:message code="borrower_name"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userAssets.lendedBooks}" var="asset">
                        <spring:message var="lendedBooksName" code='lendedBooks' />
                        <c:url var="userUrl" value="/userBookDetails/${asset.id}?table=${lendedBooksName}"/>
                        <tr class="table-row-clickable" data-href="${userUrl}">
                            <td>
                                <img class="responsive-image" src="<c:url value='/getImage/${asset.imageId}'/>" alt="<c:out value='${asset.book.name}'/>"/>
                            </td>
                            <td><c:out value="${asset.book.name}"/></td>
                            <td><c:out value="${asset.dueDate}"/></td>
                            <td><c:out value="${asset.borrower}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </c:when>
    <c:otherwise>
        <div class="promo-box">
            <h2><spring:message code="become_lender.title"/></h2>
            <p><spring:message code="become_lender.subtitle"/></p>
            <form action="<c:url value="/changeRole"/>" method="post">
                <button type="submit" class="button-status"><spring:message
                        code="become_lender.button"/></button>
            </form>
        </div>
    </c:otherwise>
</c:choose>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        })
    })
    document.addEventListener('DOMContentLoaded', function () {
        var clickableRows = document.querySelectorAll('.table-row-clickable');
        clickableRows.forEach(function (row) {
            row.addEventListener('click', function () {
                window.location.href = row.getAttribute('data-href');
            });
        });
    });

</script>
