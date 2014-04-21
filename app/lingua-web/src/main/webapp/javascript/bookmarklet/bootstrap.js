(function() {
    
    var appRoot = "http://localhost:8080/LinguaWeb/"
    var cssRoot = appRoot + "resources/css/"
    var jsRoot = appRoot + "javascript/"
    var ngViewsRoot = appRoot + "ngViews/"
    
    var addCss = function() {
        
        var cssInclude = document.createElement("link");
        cssInclude.setAttribute("rel", "stylesheet");
        cssInclude.setAttribute("type", "text/css");
        cssInclude.setAttribute("href", cssRoot + "reader.css");
        
        document.head.appendChild(cssInclude);
    };
    
    var addNgController = function() {
        
        document.body.setAttribute("ng-controller", "readerController");
    };
    
    var addNgInclude = function() {

        var ngInclude = document.createElement("ng-include");
        ngInclude.setAttribute("class", "lingua-ng-include");
        ngInclude.setAttribute("src", "'" + ngViewsRoot + "reader.html" + "'");
        
        document.body.appendChild(ngInclude);
    };
    
    var boot = function() {
        
        var requireConfig = document.createElement("script");
        requireConfig.setAttribute("src", jsRoot + "modules/config/requireConfig.js");
        var requireApp = document.createElement("script");
        requireApp.setAttribute("src", jsRoot + "lib/require-2.1.11.js");
        requireApp.setAttribute("data-main", jsRoot + "modules/reader");
        
        document.body.appendChild(requireConfig);
        document.body.appendChild(requireApp);
    };
    
    addCss();
    addNgController();
    addNgInclude();
    boot();
    
    require([jsRoot + "modules/reader.js"]);
})();
