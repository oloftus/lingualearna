(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        Signals = {
            loginSuccessSignal : "lingua-login-success",
            noteSubmittedSuccessSignal : "note-submitted-success",
            currentNotebookChanged : "current-notebook-changed"
        };
    });
})();
