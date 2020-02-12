<#include "layouts/default.ftl">

<#macro title>Главная</#macro>

<#macro content>
<section class="jumbotron text-center">
    <div class="container">
        <h1 class="jumbotron-heading">Подготовка к экзамену в СЦ</h1>
        <p class="lead text-muted">Наша база содержит все вопросы с теоретического экзамена по правилам дорожного движения в сервисном центре</p>
        <p class="lead text-muted">Вам доступны три режима тестирования: вопросы по теме, случайные вопросы и экзамен</p>
        <p class="lead text-muted">Контролировать свое обучение очень легко благодаря полной и удобной личной статистике</p>
        <#if authUser??>
            <p>
                <a href="/sections" class="btn btn-primary my-2">Вопросы по темам</a>
                <a href="/random" class="btn btn-secondary my-2">Случайные вопросы</a>
            </p>
        <#else>
            <p>
                <a href="/reg" class="btn btn-primary my-2">Регистрация</a>
                <a href="/login" class="btn btn-secondary my-2">Войти</a>
            </p>
        </#if>

    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">

        <div class="row">
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <img src="/images/1.jpg" alt="" class="img-thumbnail">
                    <div class="card-body">
                        <p class="card-text">Правила дорожного движения на автомобиле</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <img src="/images/2.jpg" alt="" class="img-thumbnail">
                    <div class="card-body">
                        <p class="card-text">Все дорожные знаки, разметка, первая помощь</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <img src="/images/3.jpg" alt="" class="img-thumbnail">
                    <div class="card-body">
                        <p class="card-text">Правила движения пешеходов и велосипедистов</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</#macro>

<@display_page/>