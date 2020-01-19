<#include "layouts/default.ftl">

<#macro title>Тест</#macro>

<#macro content>
<section class="jumbotron text-center">
    <h3 class="jumbotron-heading pb-2">${progress.progressName}</h3>
    <div class="btn-group questions-progress" role="group">
        <#list progress.list as questionProgressUnit>
            <#rt><a href="/section/#{progress.sectionId}/question/#{questionProgressUnit.questionNumber}"
                <#t>class="btn <#if !questionProgressUnit.isAnswered()>btn-light<#elseif questionProgressUnit.isWrongAnswered()>btn-danger<#else>btn-success</#if> <#if questionProgressUnit.questionNumber == question.number>active</#if>"
                <#t>role="button">#{questionProgressUnit.questionNumber}
            <#lt></a>
        </#list>
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
                <#assign currentQuestionProgressUnit = progress.getByNumber(question.number)>
                <#list question.answers as answer>
                    <#if currentQuestionProgressUnit.isAnswered()>
                        <button class="list-group-item list-group-item-action answer-button<#if answer.number == question.answer> list-group-item-success</#if><#if currentQuestionProgressUnit.isWrongAnswered() && answer.number == currentQuestionProgressUnit.answeredNumber> list-group-item-danger</#if>" id="${answer.number}">${answer.text}</button>
                    <#else>
                        <button class="list-group-item list-group-item-action answer-button" id="${answer.number}">${answer.text}</button>
                    </#if>
                </#list>
            </div>
            <form action="/section/#{progress.sectionId}/question/#{question.number}" method="post" id="answer-form">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <input name="answer" id="answer" type="hidden" <#if currentQuestionProgressUnit.isAnswered()>disabled</#if>/>
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