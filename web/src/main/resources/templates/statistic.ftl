<#include "layouts/default.ftl">

<#macro title>Результат</#macro>

<#macro content>
    <section class="jumbotron text-center">
        <div class="container">
            <h1 class="jumbotron-heading">Статистика</h1>
            <p class="lead text-muted">Ваша личная полная статистика по результатам обучения</p>
        </div>
    </section>

<div class="py-5 bg-light">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Пройдено вопросов</div>
                        <div class="d-flex justify-content-center">
                            <div class="c100 p${statistic.completedPartRate}">
                                <span>${statistic.completedPartRate}%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Доля правильных ответов</div>
                        <div class="d-flex justify-content-center">
                            <div class="c100 p${statistic.completedPartSuccessRate}">
                                <span>${statistic.completedPartSuccessRate}%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Средний балл экзамена</div>
                        <div class="d-flex justify-content-center">
                            <div class="c100 p${(statistic.averageExamScore / 20) * 100}">
                                <span>${statistic.averageExamScore}</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Попыток сдачи экзамена</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.totalExamAttempts}
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Успешных попыток</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.successExamAttempts}
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Попыток без ошибок</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.examAttemptsWithoutErrors}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Общее время тестирования</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.totalTestingTime}
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Среднее время на каждый ответ</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.averageThinkingAboutQuestion}
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <div class="card-title d-flex justify-content-center">Лучшее время сдачи экзамена</div>
                        <div class="d-flex justify-content-center">
                            ${statistic.topExamTime}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="list-group shadow-sm" id="sections-accordion">
                    <#list statistic.coverage as section>
                        <div class="section-statistic">
                            <a href="" id="section-#{section.sectionId}" data-toggle="collapse" data-target="#section-#{section.sectionId}-info" aria-expanded="false" aria-controls="section-#{section.sectionId}-info" class="list-group-item list-group-item-action collapsed">
                                <span class="readiness_background" style="width: ${section.correctAnsweredPart}%;"></span>
                                <span class="section-name">${section.sectionName}</span>
                                <span class="readiness">${section.correctAnsweredPart}%</span>
                            </a>
                            <div id="section-#{section.sectionId}-info" class="collapse" aria-labelledby="section-#{section.sectionId}" data-parent="#sections-accordion">
                                <div class="card-body">
                                    <div>Всего вопросов: ${section.totalQuestions}</div>
                                    <div>Пройдено вопросов: ${section.correctAnswered + section.wrongAnswered}</div>
                                    <div>Правильных ответов: <span class="text-success">${section.correctAnswered}</span></div>
                                    <div>Неправильных ответов: <span class="text-danger">${section.wrongAnswered}</span></div>
                                    <div>Прогресс: ${section.correctAnsweredPart}%</div>
                                    <div><a href="/section/${section.sectionId}">Вернуться к теме</a></div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>

    </div>
</div>

</#macro>

<@display_page/>