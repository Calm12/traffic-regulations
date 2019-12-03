<#include "layouts/default.ftl">

<#macro title>Темы</#macro>

<#macro content>
    <section class="jumbotron text-center">
        <div class="container">
            <h1 class="jumbotron-heading">Темы</h1>
            <p class="lead text-muted">Список всех разделов правил с вопросами</p>
        </div>
    </section>

    <div class="py-5 bg-light">
        <div class="container">
            <div class="list-group">
                <#list sections as section>
                    <a href="/section/#{section.id}" class="list-group-item list-group-item-action">${section.number}. ${section.name}</a>
                </#list>
            </div>
        </div>
    </div>
</#macro>

<@display_page/>