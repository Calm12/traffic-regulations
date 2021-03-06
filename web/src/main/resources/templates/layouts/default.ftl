<#macro content>
    Page body stub
</#macro>

<#macro title>
    Главная
</#macro>

<#macro display_page>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title><@title/> | Тесты ПДД Украины</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/webjars/font-awesome/5.12.0/css/all.min.css" rel="stylesheet"/>
    <link href="/webjars/alertifyjs/1.4.1/build/css/alertify.min.css" rel="stylesheet"/>
    <link href="/webjars/alertifyjs/1.4.1/build/css/themes/default.min.css" rel="stylesheet"/>
    <link href="/css/circle.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet"/>
</head>
<body>

<#include "header.ftl">

<main role="main" class="main">
    <@content/>
</main>

<#include "footer.ftl">

<script src="/webjars/jquery/3.0.0/jquery.min.js"></script>
<script src="/webjars/bootstrap/4.1.3/js/bootstrap.bundle.min.js"></script>
<script src="/webjars/alertifyjs/1.4.1/build/alertify.min.js"></script>
<script src="/js/main.js"></script>
<#if flash??><script>alertify.error('${flash}');</script></#if>
</body>
</html>
</#macro>