<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<head>
    <title><spring:message code="addAssetView.titleView"/></title>
    <link rel="shortcut icon" href="<c:url value='/static/images/favicon-claro.ico'/>" type="image/x-icon">
    <script src="<c:url value="/static/javaScript/topbar.js"/>"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
            integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
            integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
            crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-aFq/bzH65dt+w6FI2ooMVUpc+21e0SRygnTpmBvdBgSdnuTN7QbdgL+OapgHtvPp" crossorigin="anonymous">
    <link href="<c:url value="/static/css/main.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/static/css/addAssetView.css"/>" rel="stylesheet"/>

    <script src="<c:url value="/static/javaScript/addAssetForm.js"/>" defer></script>
</head>

<body data-path="${path}" class="body-class">

<jsp:include page="../components/navBar.jsp"/>
<div class="main-class">
    <div class="container my-5">

        <jsp:include page="../components/snackbarComponent.jsp"/>

        <h1 class="text-center mb-5"><spring:message code="addAssetView.title"> </spring:message></h1>
        <div class="p-4 rounded">
            <div class="flex-container">

                <div class="image-wrapper">
                    <label for="uploadImage" class="image-container position-relative">
                        <img src="<c:url value="/static/images/no_image_placeholder.jpg"/>" alt="Book Cover"
                             class="img-fluid" id="bookImage" style="width: 400px; height: 600px; object-fit: cover">
                        <div class="img-hover-text">
                            <i class="bi bi-cloud-upload"></i>
                            <spring:message code="addAssetView.uploadImage"/>
                        </div>
                    </label>
                </div>

                <div class="form-wrapper">
                    <c:url var="addAssetUrl" value="/addAsset"/>
                    <form:form modelAttribute="addAssetForm" method="post"
                               action="${addAssetUrl}" enctype="multipart/form-data" id="form" accept-charset="utf-9">
                        <div class="stepper-wrapper">
                            <div class="stepper-item" data-step-count="1">
                                <div class="step-counter">1</div>
                                <div class="step-name">
                                    <spring:message code="addAssetView.steps.ISBN"/>
                                </div>
                            </div>
                            <div class="stepper-item" data-step-count="2">
                                <div class="step-counter">2</div>
                                <div class="step-name">
                                    <spring:message code="addAssetView.steps.INFO"/>
                                </div>
                            </div>
                            <div class="stepper-item" data-step-count="3">
                                <div class="step-counter">3</div>
                                <div class="step-name">
                                    <spring:message code="addAssetView.steps.LOCATION"/>
                                </div>
                            </div>
                        </div>
                        <fieldset class="info-container d-none" data-step="1" id="isbn-fs">
                            <spring:message code="addAssetView.isbnLabel" var="isbnLabel"/>
                            <h2><spring:message code="addAssetView.steps.ISBN.title"/></h2>
                            &#x1F6C8;
                            <text class="form-subtitle">
                                <spring:message code="addAssetView.steps.ISBN_NOTE"/>
                            </text>
                            <spring:message code="addAssetView.placeholders.isbn" var="isbnPH"/>
                            <form:input path="isbn" id="isbn"
                                        placeholder="${isbnPH}"
                                        class="form-control"/>
                            <small id="isbnError" class="text-danger small d-none">Please enter a valid ISBN</small>

                            <form:errors path="isbn" cssClass="text-danger small" element="small"/>
                            <div class="mt-3 form-button-container">
                                <input type="button" class="prev-button btn btn-secondary mx-1"
                                       value="<spring:message code="addAssetView.steps.prevButton"/>" disabled/>
                                <input type="button" class="next-button btn btn-primary mx-1"
                                       value="<spring:message code="addAssetView.steps.nextButton"/>"/>
                            </div>
                        </fieldset>
                        <fieldset class="info-container d-none" data-step="2">
                            <h2><spring:message code="addAssetView.steps.INFO.title"/></h2>
                            <div class="field-group">
                                <div class="field">
                                    <label for="title" class="form-label"> <spring:message
                                            code="addAssetView.titleLabel"/></label>
                                    <spring:message code="addAssetView.placeholders.title" var="titlePH"/>
                                    <form:input path="title" id="title" placeholder="${titlePH}" class="form-control"
                                                readonly="true"/>
                                    <form:errors path="title" cssClass="text-danger small" element="small"/>
                                </div>
                                <div class="field">
                                    <label for="physicalCondition" class="form-label"> <spring:message
                                            code="addAssetView.physicalConditionLabel"/></label>
                                    <form:select path="physicalCondition" id="physicalCondition" class="form-control"
                                                 accept-charset="utf-9">
                                        <form:option value="asnew"><spring:message
                                                code="addAssetForm.condition.asnew"/></form:option>
                                        <form:option value="fine"><spring:message
                                                code="addAssetForm.condition.fine"/></form:option>
                                        <form:option value="verygood"><spring:message
                                                code="addAssetForm.condition.verygood"/></form:option>
                                        <form:option value="good"><spring:message
                                                code="addAssetForm.condition.good"/></form:option>
                                        <form:option value="fair"><spring:message
                                                code="addAssetForm.condition.fair"/></form:option>
                                        <form:option value="poor"><spring:message
                                                code="addAssetForm.condition.poor"/></form:option>
                                        <form:option value="exlibrary"><spring:message
                                                code="addAssetForm.condition.exlibrary"/></form:option>
                                        <form:option value="bookclub"><spring:message
                                                code="addAssetForm.condition.bookclub"/></form:option>
                                        <form:option value="bindingcopy"><spring:message
                                                code="addAssetForm.condition.bindingcopy"/></form:option>

                                    </form:select>
                                </div>
                            </div>
                            <div class="field-group">
                                <div class="field">
                                    <spring:message code="addAssetView.authorLabel" var="authorLabel"/>
                                    <spring:message code="addAssetView.placeholders.author" var="authorPH"/>
                                    <label for="author" class="form-label">${authorLabel}</label>
                                    <form:input path="author" id="author" placeholder="${authorPH}"
                                                class="form-control"
                                                readonly="true"/>
                                    <form:errors path="author" cssClass="text-danger small" element="small"/>
                                </div>
                                <div class="field">
                                    <spring:message code="addAssetView.languageLabel" var="languageLabel"/>
                                    <spring:message code="addAssetView.placeholders.language" var="languagePH"/>
                                    <label for="language" class="form-label">${languageLabel}</label>
                                    <form:input path="language" id="language" placeholder="${languagePH}"
                                                class="form-control" readonly="true"/>
                                    <form:errors path="language" cssClass="text-danger small" element="small"/>
                                </div>
                            </div>
                            <div class="mt-3 form-button-container">
                                <input type="button" class="prev-button btn btn-primary mx-1" value="Atrás"/>
                                <input type="button" class="next-button btn btn-primary mx-1" value="Siguiente"/>
                            </div>
                        </fieldset>
                        <fieldset class="info-container d-none" data-step="3">
                            <h2><spring:message code="addAssetView.steps.LOCATION.title"/></h2>
                            <div class="field-group">
                                <div class="field">
                                    <spring:message code="addAssetView.localityLabel" var="localityLabel"/>
                                    <spring:message code="addAssetView.placeholders.city" var="localityPH"/>
                                    <label for="locality" class="form-label">${localityLabel}</label>
                                    <form:input path="locality" id="locality" placeholder="${localityPH}"
                                                class="form-control"/>
                                    <form:errors path="locality" cssClass="text-danger small" element="small"/>
                                </div>
                                <div class="field">
                                    <spring:message code="addAssetView.provinceLabel" var="provinceLabel"/>
                                    <spring:message code="addAssetView.placeholders.province" var="provincePH"/>
                                    <label for="province" class="form-label">${provinceLabel}</label>
                                    <form:input path="province" id="province" placeholder="${provincePH}"
                                                class="form-control"/>
                                    <form:errors path="province" cssClass="text-danger small" element="small"/>
                                </div>
                            </div>
                            <div class="field-group">
                                <div class="field">
                                    <spring:message code="addAssetView.countryLabel" var="countryLabel"/>
                                    <spring:message code="addAssetView.placeholders.country" var="countryPH"/>
                                    <label for="country" class="form-label">${countryLabel}</label>
                                    <form:input path="country" id="country" placeholder="${countryPH}"
                                                class="form-control"/>
                                    <form:errors path="country" cssClass="text-danger small" element="small"/>
                                </div>
                                <div class="field">
                                    <spring:message code="addAssetView.zipcodeLabel" var="zipcodeLabel"/>
                                    <spring:message code="addAssetView.placeholders.zipcode" var="zipcpdePH"/>
                                    <label for="zipcode" class="form-label">${zipcodeLabel}</label>
                                    <form:input path="zipcode" id="zipcode" placeholder="${zipcpdePH}"
                                                class="form-control"/>
                                    <form:errors path="zipcode" cssClass="text-danger small" element="small"/>
                                </div>
                            </div>
                            <div class="mt-3 form-button-container">
                                <input type="button" class="prev-button btn btn-primary mx-1" value="Atrás"/>
                                <spring:message code="addAssetView.addButton" var="addButton"/>
                                <input type="submit" class="btn btn-success mx-1"
                                       value="<c:out value="${addButton}"/>"/>
                            </div>
                        </fieldset>
                        <input type="file" accept="image/*" name="file" id="uploadImage" style="display:none;"
                               onchange="previewImage()"/>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<spring:message code="addAssetView.addBookModal.title" var="modalTitle"/>
<spring:message code="addAssetView.addBookModal.text" var="modalText"/>
<% request.setCharacterEncoding("utf-8"); %>
<jsp:include page="../components/modal.jsp">
    <jsp:param name="modalTitle" value="${modalTitle}"/>
    <jsp:param name="text" value="${modalText}"/>
</jsp:include>

<spring:message code="addAssetView.changeRole.title" var="title"/>
<spring:message code="addAssetView.changeRole.text" var="text"/>

<% request.setCharacterEncoding("utf-8"); %>
<jsp:include page="../components/addAssetModal.jsp">
    <jsp:param name="modalTitle" value="${title}"/>
    <jsp:param name="text" value="${text}"/>
</jsp:include>

</body>
<script>
    function previewImage() {
        const fileInput = document.getElementById('uploadImage');
        const file = fileInput.files[0];
        const img = document.getElementById('bookImage');
        const reader = new FileReader();

        reader.addEventListener('load', function () {
            img.src = reader.result;
        }, false);

        if (file) {
            reader.readAsDataURL(file);
        }
    }
</script>
<script>
    window.isbnUrl = `<c:url value="/book"><c:param name="isbn" value="${isbn}" /></c:url>`
</script>

<script>
    let bindingResult = `<%= request.getAttribute("org.springframework.validation.BindingResult.addAssetForm") %>`;
</script>
