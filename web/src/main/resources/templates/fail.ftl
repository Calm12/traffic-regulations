<#include "layouts/default.ftl">

<#macro title>Результат</#macro>

<#macro content>
<section class="jumbotron text-center">
    <h3 class="jumbotron-heading pb-2">${progress.progressName}</h3>
    <div class="col align-self-center pt-5">
        <div class="progress result-progress-bar">
            <div class="progress-bar bg-success progress-bar-striped progress-bar-animated" role="progressbar" style="width: ${progress.result.correctRate}%" aria-valuenow="${progress.result.correctRate}" aria-valuemin="0" aria-valuemax="100">${progress.result.correctRate}%</div>
            <div class="progress-bar bg-danger progress-bar-striped" role="progressbar" style="width: ${progress.result.wrongRate}%" aria-valuenow="${progress.result.wrongRate}" aria-valuemin="0" aria-valuemax="100">${progress.result.wrongRate}%</div>
        </div>
    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">
        <div class="d-flex justify-content-center h4 pb-4">Вы не сдали экзамен :(</div>
        <div class="justify-content-center">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">Результат</li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Всего вопросов: <span class="badge badge-secondary badge-pill">${progress.result.total}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Правильных ответов: <span class="badge badge-success badge-pill">${progress.result.correct}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Количество ошибок: <span class="badge badge-danger badge-pill">${progress.result.wrong}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Успешность: <span class="badge badge-secondary badge-pill">${progress.result.correctRate}%</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Потраченное время: <span class="badge badge-secondary badge-pill">${progress.result.formattedDuration}</span>
                </li>
            </ul>
            <a role="button" href="<#if progress.randomSet>/random<#elseif progress.exam>/exam<#else>/section/#{progress.sectionId}</#if>/question/${progress.first.questionNumber}" class="btn btn-secondary btn-lg btn-block">Вернуться к вопросам</a>
        </div>
    </div>
</div>

</#macro>

<@display_page/>