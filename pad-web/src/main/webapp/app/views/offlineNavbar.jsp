<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/pad/login.htm?tp">
                <img src="app/img/ags_logo.png" alt="" style="height: 77%; float:left; margin-top: 2px;" /> &nbsp; Port Access Dakar 
                <c:if test="${isTestEnvironment == true}">
                    <span>(Sandbox)</span>
                </c:if>
                <div style="position:absolute;font-size: x-small;color: gray;padding-left: 58px;">
                    <%@ include file="../../WEB-INF/jsp/buildNumber.jsp"%>
                </div>
            </a>
        </div>
        <div id="navbar" class="navbar-collapse collapse" aria-expanded="false" style="height: 1px;">
            <ul class="nav navbar-nav navbar-right">
                <li class="navbar-select hidden-xs hidden-sm hidden-md">
                    <span class="glyphicon glyphicon-globe" style="font-size: 14pt; margin-top: 7px; margin-right: 0px;"></span>
                </li>
                <li class="navbar-select">
                    <select class="form-control select-language" style="width: inherit;" ng-change="changeLanguage(language);" ng-model="language">
                        <option value="FR">Français</option>
                        <option value="EN">English</option>
                    </select>
                </li>
            </ul>
        </div>
    </div>
</nav>