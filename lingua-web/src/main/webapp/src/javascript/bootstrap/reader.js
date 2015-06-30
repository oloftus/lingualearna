if (typeof IN_PRODUCTION === 'undefined') IN_PRODUCTION = false;

lingua.AppRoot = "@app_root@";

(function () {

    var cssRoot = lingua.AppRoot + "/resources/css";
    var jsRoot = lingua.AppRoot + "/javascript";
    var minJsRoot = lingua.AppRoot + "/js";
    var libRoot = lingua.AppRoot + "/dependencies";
    var viewRoot = lingua.AppRoot + "/templates";

    var cssFile = "/reader.css";
    var ngIncludeFile = "/module/reader.html";
    var requireJsFile = "/requirejs/require.js";
    var requireConfigFile = "/config/require.js";
    var minifiedApp = "/reader.min.js";
    var mainModule = "/reader.js";

    function addCss() {

        var cssInclude = document.createElement("link");
        cssInclude.setAttribute("rel", "stylesheet");
        cssInclude.setAttribute("type", "text/css");
        cssInclude.setAttribute("href", cssRoot + cssFile);
        document.head.appendChild(cssInclude);
    }

    function addNgInclude() {

        var ngInclude = document.createElement("ng-include");
        ngInclude.setAttribute("src", "'" + viewRoot + ngIncludeFile + "'");
        document.body.appendChild(ngInclude);
    }

    function addScripts() {

        var scriptFile;

        if (IN_PRODUCTION) {
            scriptFile = document.createElement("script");
            scriptFile.setAttribute("type", "text/javascript");
            scriptFile.async = false;
            scriptFile.setAttribute("src", minJsRoot + minifiedApp);
            document.head.appendChild(scriptFile);
        }

        if (!IN_PRODUCTION) {
            scriptFile = document.createElement("script");
            scriptFile.setAttribute("type", "text/javascript");
            scriptFile.async = false;
            scriptFile.setAttribute("src", jsRoot + requireConfigFile);
            document.head.appendChild(scriptFile);

            scriptFile = document.createElement("script");
            scriptFile.setAttribute("type", "text/javascript");
            scriptFile.async = false;
            scriptFile.setAttribute("src", libRoot + requireJsFile);
            scriptFile.setAttribute("data-main", jsRoot + mainModule);
            document.head.appendChild(scriptFile);
        }
    }

    function storeAndDeleteJquery() {

        if (typeof($) != "undefined" || typeof(jQuery) != "undefined") {
            lingua.OldJquery = $ || jQuery;
            delete $;
            delete jQuery;
        }
    }

    addCss();
    addNgInclude();
    storeAndDeleteJquery();
    addScripts();
})();
