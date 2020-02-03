<header class="fixed-top">
    <div class="bg-dark collapse" id="navbarHeader" style="">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-md-7 py-4">
                    <h4 class="text-white">О сервисе</h4>
                    <p class="text-muted">Все вопросы категории B;B1 ПДД Украины из сервисного центра</p>
                </div>
                <div class="col-sm-4 offset-md-1 py-4">
                    <h4 class="text-white">Навигация</h4>
                    <ul class="list-unstyled">
                        <li><a href="/sections" class="text-white">Вопросы по темам</a></li>
                        <li><a href="/random" class="text-white">20 случайных вопросов</a></li>
                        <#if authUser??>
                            <li>
                                <form action="/logout" method="post">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    <a href="" class="text-white" onclick="this.parentNode.submit(); return false;">Выйти</a>
                                </form>
                            </li>
                        <#else>
                            <li><a href="/reg" class="text-white">Регистрация</a></li>
                            <li><a href="/login" class="text-white">Войти</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="navbar navbar-dark bg-dark shadow-sm">
        <div class="container d-flex justify-content-between">
            <a href="/" class="navbar-brand d-flex align-items-center">
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="50" height="23" viewBox="0 0 70 20">
                    <image width="50" height="23" xlink:href="data:img/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAXCAQAAADjV9S/AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAALiMAAC4jAXilP3YAAAAHdElNRQfjCwoRIgYXXDirAAACR0lEQVRIx7WUXWjNcRjHP7PNJpbiYog2K9tQ2ITaxSTF2gXDygUuLERS7nYjLysliksiL6lZIa4srVyMaKUsi1pZXmZDLIeTd5uPC8fZ/xw7O/8jnu/V0/N7+j4v3+eHpESx7aayt5536Si5CciSFDaVB0yih7eMTYoMkUM5ecBzttNKekvJ36XuGKW+MptVvZy+k1SBY+qFtOlldqtvXPg3JGvVXseFmvhBVZsyJalQdWbYtbrEAbXHyvAkU/ykLg5N8Qu7VG1zemp1ZVNKEcXMYA415POSixSTjSGUA7n08poGpgHQymE6+Jqsrq1+CVzAoBE/mrl9NeJ7v8e8Pk+6fLiTRg7xgxY66ecZT3lHTqj6R7IffKaSjdQyC4ABVnIP8JtamOEG0mOM8zyn6nxB7fznFL+xTr31v0nwqlqVPP88aimll2tEM9pGESuZSBdtSYo8RR31iZ2sDuhsZwb1Ho9nvUk6ybFqX5CkQdUDLnCn0XRfRQBn1YfWWeEJVWcnRG/oMEmeUXVR/O6H1JIQFMvU23Fvg3olIX5fc4DJVPKJGgpo5m5slq9o5AjbOU3RqLt4zBZgT9xvppE11BBlAoNE2c08LmEkcLUHAhWsyODaywN5zUmxLgtyqGc+H4hQThO17IvXtBk4yk0KR+2kn02sp5rumJ/FKr7TQC65ZJHPE64xFJxeh7o3IIIXZofYyQxV58a8M+r+5DdBp8QBtdfzdqtaHVJd21S9Y4sRtf3PF4nu+Nh/o9edmsGdVPlohK3G8RN5k11/81uEBQAAAABJRU5ErkJggg=="/>
                </svg>
                <div class="logo-text"><strong>ПДД</strong></div>
            </a>
            <#if authUser??>
                <div class="dropdown">
                    <a class="nav-link navbar-text" data-toggle="dropdown" href="" role="button" aria-haspopup="true" aria-expanded="false">
                        <div class="user-icon-in-header"><i class="fa fa-user-circle"></i></div>
                        <div class="username-in-header">${authUser.username}</div></a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="/exam">Экзамен</a>
                        <a class="dropdown-item" href="#">Статистика</a>
                        <a class="dropdown-item" href="#">Профиль</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#">Продолжить</a>
                    </div>
                </div>
            </#if>
            <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarHeader" aria-controls="navbarHeader" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
    </div>
</header>