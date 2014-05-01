(function() {
    
    var appRoot = "http://localhost:8080/LinguaWeb";
    var cssRoot = appRoot + "/resources/css";
    var jsRoot = appRoot + "/javascript";
    var libRoot = jsRoot + "/lib";
    var moduleRoot = jsRoot + "/modules";
    var viewRoot = appRoot + "/ngViews";
    var apiRoot = appRoot + "/app/api";
    
    var csrfTokenApiUrl = apiRoot + "/security/csrfToken";

    var cssFiles = [ "/common.css", "/reader.css" ];
    var scriptFiles = [ libRoot + "/require-2.1.11.js", moduleRoot + "/config/requireConfig.js" ];
    var ngIncludeFile = "/reader.html";
    var readerJsFile = moduleRoot + "/miniApps/reader.js";
    
    var ngIncludeClass = "lingua-ng-include";
    var ngIncludeId = "lingua-reader";
    
    Properties = {};
    
    var addCss = function() {

        for (var i = 0; i < cssFiles.length; i++) {
            var cssInclude = document.createElement("link");
            cssInclude.setAttribute("rel", "stylesheet");
            cssInclude.setAttribute("type", "text/css");
            cssInclude.setAttribute("href", cssRoot + cssFiles[i]);
            document.head.appendChild(cssInclude);
        }
    };

    var addNgInclude = function() {

        var ngInclude = document.createElement("ng-include");
        ngInclude.setAttribute("class", ngIncludeClass);
        ngInclude.setAttribute("id", ngIncludeId);
        ngInclude.setAttribute("src", "'" + viewRoot + ngIncludeFile + "'");

        document.body.insertBefore(ngInclude, document.body.childNodes[0]);
    };

    var addCsrfToken = function(readyCallback) {

        function reqListener() {
            Properties.csrfToken = this.responseText;
            readyCallback();
        }

        var csrfTokenRequest = new XMLHttpRequest();
        csrfTokenRequest.onload = reqListener;
        csrfTokenRequest.withCredentials = true;
        csrfTokenRequest.open("GET", csrfTokenApiUrl, true);
        csrfTokenRequest.send();
    };

    var addScripts = function() {

        for (var i = 0; i < scriptFiles.length; i++) {
            var scriptFile = document.createElement("script");
            scriptFile.setAttribute("src", scriptFiles[i]);
            document.body.appendChild(scriptFile);
        }
    };
    
    var boot = function() {
        
        require([ readerJsFile ]);
    };

    addCss();
    addNgInclude();
    addCsrfToken(function() {
        addScripts();
        boot();
    });
})();
