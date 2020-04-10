<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    <title>Top 20 Movie List</title>
</head>
<body>
<div class='row'>
<c:forEach var="temp" items="${movies}">
    <div class="card border-light col-sm-6" style="max-width: 18rem;">
        <div class="card-header">
            <c:url var="titleLink" value="/movie/moviedetail">
            <c:param name="movieId" value="${temp.id}"/>
        </c:url><a href="${titleLink}">${temp.title}</a> </div>
        <div class="card-body text-dark">
            <p class="card-text">Year: ${temp.year}</p>
            <p class="card-text">Director: ${temp.director}</p>
            <p class="card-text">First three genres:
                <c:forEach var="tempGe" items="${temp.genres}">
            <li>${tempGe.name}</li>
                </c:forEach>
            </p>
            <p class="card-text">First three stars:

                <c:forEach var="tempSt" items="${temp.stars}">
                    <c:url var="starLink" value="/movie/stardetail">
                        <c:param name="starId" value="${tempSt.id}"/>
                    </c:url>
            <li>
                    <a href="${starLink}">${tempSt.name}</a>
        </li>
                </c:forEach>

            </p>

            <p class="card-text">Rating:  ${temp.rating}</p>

        </div>
    </div>



</c:forEach>

</div>



<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
</body>
</html>