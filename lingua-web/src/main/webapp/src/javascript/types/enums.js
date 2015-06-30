define({

    HttpMethod: {
        POST: "POST",
        GET: "GET",
        PUT: "PUT",
        DELETE: "DELETE"
    },

    HttpHeaders: {
        BAD_REQUEST: 400,
        FORBIDDEN: 403,
        INTERNAL_SERVER_ERROR: 500
    },

    MessageSeverity: {
        INFO: "INFO",
        ERROR: "ERROR"
    },

    Dialogs: {
        ADD_NOTE: "ADD_NOTE"
    },

    Views: {
        MAIN: "",
        MODAL_DIALOG: "MODAL_DIALOG",
        LOGIN_DIALOG: "LOGIN_DIALOG",
        KNOWLEDGE_TEST_HINT: "KNOWLEDGE_TEST_HINT"
    },

    AppStates: {
        READER: "READER",
        NOTEBOOK: "NOTEBOOK",
        ADD_NOTE: "ADD_NOTE",
        EDIT_NOTE: "EDIT_NOTE",
        TRANSLATE: "TRANSLATE",
        LOGIN: "LOGIN",
        SETTINGS: "SETTINGS",
        ADD_NOTEBOOK: "ADD_NOTEBOOK",
        ADD_PAGE: "ADD_PAGE",
        KNOWLEDGE_TEST: "KNOWLEDGE_TEST",
        KNOWLEDGE_TEST_HINT: "KNOWLEDGE_TEST_HINT"
    },

    Components: {
        ANY: "*",
        READER: "READER",
        TRANSLATE: "TRANSLATE",
        ADD_NOTE: "ADD_NOTE",
        LOGIN: "LOGIN",
        PAGE: "PAGE",
        JSON_WEB_SERVICE: "JSON_WEB_SERVICE",
        NOTEBOOK: "NOTEBOOK",
        BINDER: "BINDER",
        ADD_PAGE: "ADD_PAGE",
        ADD_NOTEBOOK: "ADD_NOTEBOOK",
        EDIT_NOTE: "EDIT_NOTE"
    },

    TranslationSources: {
        GOOGLE: "Google",
        COLLINS: "Collins",
        COLLINS_GOOGLE: "CollinsGoogle",
        MANUAL: "Manual"
    },

    Caches: {
        SUPPORTED_LANGUAGES: "SUPPORTED_LANGUAGES",
        LANGUAGE_NAMES: "LANGUAGE_NAMES",
        SOURCE_CONTEXTS: "SOURCE_CONTEXTS"
    },

    Subjects: {
        TRANSLATION_REQUEST: "translation-request",
        NOTE: "note"
    },

    Signals: {
        LOGIN_SUCCESS: "login-success",
        SETTINGS_SAVE_SUCCESS: "settings-save-success",
        CURRENT_NOTEBOOK_CHANGED: "current-notebook-changed",
        CSRF_RETRIEVED: "csrf-retrieved",
        NOTE_SAVED_SUCCESS: "note-added-success",
        NOTE_UPDATED_SUCCESS: "note-updated-success",
        CURRENT_PAGE_CHANGED: "current-page-changed",
        PAGE_SAVED_SUCCESS: "page-saved-success",
        NOTEBOOK_SAVED_SUCCESS: "notebook-saved-success",
        DONE_SETUP: "done-setup"
    }
});
