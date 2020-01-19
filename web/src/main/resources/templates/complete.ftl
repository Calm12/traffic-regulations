<#include "layouts/default.ftl">

<#macro title>Тест</#macro>

<#macro content>
<section class="jumbotron text-center">
    <h3 class="jumbotron-heading pb-2">${progress.progressName}</h3>
    <div class="col align-self-center pt-5">
        <div class="progress result-progress-bar">
            <div class="progress-bar bg-success progress-bar-striped progress-bar-animated" role="progressbar" style="width: ${result.correctRate}%" aria-valuenow="${result.correctRate}" aria-valuemin="0" aria-valuemax="100">${result.correctRate}%</div>
            <div class="progress-bar bg-danger progress-bar-striped" role="progressbar" style="width: ${result.wrongRate}%" aria-valuenow="${result.wrongRate}" aria-valuemin="0" aria-valuemax="100">${result.wrongRate}%</div>
        </div>
    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">
        <div class="d-flex justify-content-center h4 pb-4">Вы ответили на все вопросы!</div>
        <div class="justify-content-center">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">Результат</li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Всего вопросов: <span class="badge badge-secondary badge-pill">${result.total}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Правильных ответов: <span class="badge badge-success badge-pill">${result.correct}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Количество ошибок: <span class="badge badge-danger badge-pill">${result.wrong}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Успешность: <span class="badge badge-secondary badge-pill">${result.correctRate}%</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    Потраченное время: <span class="badge badge-secondary badge-pill">${result.formattedDuration}</span>
                </li>
            </ul>
        </div>
    </div>
</div>

</#macro>

<@display_page/>