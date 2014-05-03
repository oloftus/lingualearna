(function() {

    var appRoot = "http://localhost:8080/LinguaWeb";
    var cssRoot = appRoot + "/resources/css";
    var jsRoot = appRoot + "/javascript";
    var libRoot = jsRoot + "/lib";
    var moduleRoot = jsRoot + "/modules";
    var viewRoot = appRoot + "/ngViews";

    var cssFiles = [ "/common.css", "/reader.css" ];
    var scriptFiles = [ libRoot + "/require-2.1.11.js", moduleRoot + "/config/requireConfig.js" ];
    var ngIncludeFile = viewRoot + "/reader.html";
    var readerJsFile = moduleRoot + "/miniApps/reader.js";

    var ngIncludeClass = "lingua-ng-include";
    var ngIncludeId = "lingua-reader";

    Properties = {};

    var scriptsLoadedCounter = 0;
    
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
        ngInclude.setAttribute("src", "'" + ngIncludeFile + "'");

        document.body.insertBefore(ngInclude, document.body.childNodes[0]);
    };

    var addScripts = function(loadedCallback) {

        for (var i = 0; i < scriptFiles.length; i++) {
            var scriptFile = document.createElement("script");
            scriptFile.setAttribute("src", scriptFiles[i]);
            scriptFile.onload = function() {
                scriptsLoadedCounter++;
                if (scriptsLoadedCounter === scriptFiles.length) {
                    loadedCallback();
                }
            };
            document.body.appendChild(scriptFile);
        }
    };

    var boot = function() {

        setTimeout(function() {
            require([ readerJsFile ]);
        }, 100);
    };

    addCss();
    addNgInclude();
    addScripts(function() {
        boot();
    });
})();
