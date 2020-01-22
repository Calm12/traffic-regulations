<#include "layouts/default.ftl">

<#macro title>Тест</#macro>

<#macro content>
<#if currentProgressUnit??>
<#else>
    <#assign currentProgressUnit = progress.getByNumber(question.number)>
</#if>
<section class="jumbotron text-center">
    <h3 class="jumbotron-heading pb-2">${progress.progressName}</h3>
    <div class="btn-group questions-progress" role="group">
        <#list progress.list as progressUnit>
            <#rt><a href="<#if progress.randomSet>/random<#else>/section/#{progress.sectionId}</#if>/question/#{progressUnit.questionNumber}"
                <#t>class="btn <#if !progressUnit.isAnswered()>btn-light<#elseif progressUnit.isWrongAnswered()>btn-danger<#else>btn-success</#if> <#if progressUnit.questionNumber == currentProgressUnit.questionNumber>active</#if>"
                <#t>role="button">#{progressUnit.questionNumber}
            <#lt></a>
        </#list>
        <#if progress.completed>
            <a href="/questions/${progress.id}/complete" class="btn btn-info" role="button">Результат</a>
        </#if>
    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">

    <div class="row">
        <div class="col-md-12 h4 pb-4">${question.text}</div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <div class="list-group">
                <#list question.answers as answer>
                    <#if currentProgressUnit.isAnswered()>
                        <button class="list-group-item list-group-item-action answer-button<#if answer.number == question.answer> list-group-item-success</#if><#if currentProgressUnit.isWrongAnswered() && answer.number == currentProgressUnit.answeredNumber> list-group-item-danger</#if>" id="${answer.number}">${answer.text}</button>
                    <#else>
                        <button class="list-group-item list-group-item-action answer-button" id="${answer.number}">${answer.text}</button>
                    </#if>
                </#list>
            </div>
            <form action="<#if progress.randomSet>/random<#else>/section/#{progress.sectionId}</#if>/question/#{currentProgressUnit.questionNumber}" method="post" id="answer-form">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <input name="answer" id="answer" type="hidden" <#if currentProgressUnit.isAnswered()>disabled</#if>/>
            </form>
        </div>
        <div class="col-md-4">
            <img class="question-image" src="/images/#{question.id}.jpg" alt="image"/>
        </div>
    </div>

    </div>
</div>

</#macro>

<@display_page/>