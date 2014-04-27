(function() {
    
    var appRoot = "http://localhost:8080/LinguaWeb/";
    var cssRoot = appRoot + "resources/css/";
    var jsRoot = appRoot + "javascript/";
    var ngViewsRoot = appRoot + "ngViews/";
    var apiRoot = appRoot + "app/api/";

    var addCss = function() {

        var cssInclude = [];
        cssInclude[0] = document.createElement("link");
        cssInclude[1] = document.createElement("link");

        for (var i = 0; i < cssInclude.length; i++) {
            cssInclude[i].setAttribute("rel", "stylesheet");
            cssInclude[i].setAttribute("type", "text/css");
        }

        cssInclude[0].setAttribute("href", cssRoot + "common.css");
        cssInclude[1].setAttribute("href", cssRoot + "reader.css");

        for (var i = 0; i < cssInclude.length; i++) {
            document.head.appendChild(cssInclude[i]);
        }
    };

    var addNgInclude = function() {

        var ngInclude = document.createElement("ng-include");
        ngInclude.setAttribute("class", "lingua-ng-include");
        ngInclude.setAttribute("id", "lingua-reader");
        ngInclude.setAttribute("src", "'" + ngViewsRoot + "reader.html" + "'");

        document.body.insertBefore(ngInclude, document.body.childNodes[0]);
    };

    var addCsrfToken = function(readyCallback) {

        function reqListener() {
            
            pageParam = {};
            pageParam.csrfToken = this.responseText;
            readyCallback();
        }

        var csrfTokenRequest = new XMLHttpRequest();
        csrfTokenRequest.onload = reqListener;
        csrfTokenRequest.withCredentials = true;
        csrfTokenRequest.open("GET", apiRoot + "security/csrfToken", true);
        csrfTokenRequest.send();
    };

    var boot = function() {

        var requireConfig = document.createElement("script");
        requireConfig.setAttribute("src", jsRoot + "modules/config/requireConfig.js");
        var requireApp = document.createElement("script");
        requireApp.setAttribute("src", jsRoot + "lib/require-2.1.11.js");

        document.body.appendChild(requireConfig);
        document.body.appendChild(requireApp);

        // TODO: Temporary
        setTimeout(function() {
            require([ jsRoot + "modules/miniApps/reader.js" ]);
        }, 100);
    };

    addCss();
    addNgInclude();
    addCsrfToken(function() {
        boot();
    });
})();
