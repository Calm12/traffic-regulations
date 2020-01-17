<#import "/spring.ftl" as spring/>

<#include "layouts/default.ftl">

<#macro title>Авторизация</#macro>

<#macro content>
<section class="jumbotron text-center">
    <div class="container">
        <h1 class="jumbotron-heading">Авторизация</h1>
    </div>
</section>

<div class="py-5 bg-light">
    <div class="container">
        <div class="mx-auto reg-form">
            <form action="/login" method="post" autocomplete="off">
                <#if SPRING_SECURITY_LAST_EXCEPTION?? && SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
                    <div class="alert alert-danger" role="alert">
                        <a class="close" data-dismiss="alert"><i class="fa fa-xs fa-times"></i></a>${SPRING_SECURITY_LAST_EXCEPTION.message}
                    </div>
                </#if>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-user"></i></span></div>
                    <input type="text" name="username" class='form-control' placeholder='Логин'/>
                </div>
                <div class="form-group input-group">
                    <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-lock"></i></span></div>
                    <input type="password" name="password" class='form-control' placeholder='Пароль'/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">Войти</button>
                </div>
                <p class="text-center">Еще нет аккаунта? <a href="<@spring.url '/reg'/>">Регистрация</a></p>
            </form>
        </div>
    </div>
</div>
</#macro>

<@display_page/>