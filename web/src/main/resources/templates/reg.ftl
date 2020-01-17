<#import "/spring.ftl" as spring/>

<#include "layouts/default.ftl">

<#macro title>Регистрация</#macro>

<#macro content>
<section class="jumbotron text-center">
    <div class="container">
        <h1 class="jumbotron-heading">Регистрация</h1>
    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">
        <div class="mx-auto reg-form">
            <form method="post" autocomplete="off">
                <@spring.bind "user.*"/>
                <#list spring.status.errorMessages as error>
                    <div class="alert alert-danger" role="alert">
                        <a class="close" data-dismiss="alert"><i class="fa fa-xs fa-times"></i></a>${error}
                    </div>
                </#list>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-user"></i></span></div>
                    <@spring.formInput "user.username" "class='form-control' placeholder='Логин'"/>
                    <#-- <#if spring.status.error>has error here</#if> -->
                    <#-- <@spring.showErrors "<br>"/> -->
                </div>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-envelope"></i></span></div>
                    <@spring.formInput "user.email" "class='form-control' placeholder='Email'" "email"/>
                </div>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-lock"></i></span></div>
                    <@spring.formPasswordInput "user.password" "class='form-control' placeholder='Пароль'"/>
                </div>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-lock"></i></span></div>
                    <@spring.formPasswordInput "user.matchingPassword" "class='form-control' placeholder='Повторите пароль'"/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">Создать аккаунт</button>
                </div>
                <p class="text-center">Уже есть аккаунт? <a href="<@spring.url '/login'/>">Войти</a></p>
            </form>
        </div>
    </div>
</div>
</#macro>

<@display_page/>