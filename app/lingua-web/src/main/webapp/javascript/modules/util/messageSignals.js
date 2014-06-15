(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        Signals = {
            LoginSuccess : "lingua-login-success",
            NoteSubmittedSuccessSignal : "note-submitted-success",
            CurrentNotebookChanged : "current-notebook-changed",
            CsrfRetrieved : "csrf-retrieved"
        };
    });
})();
