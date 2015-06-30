define(
    [
        "types/enums",
        "config/properties"
    ],
    function (enums, properties) {

        return {
            routingTable: [
                {
                    stateName: enums.AppStates.READER
                },
                {
                    stateName: enums.AppStates.NOTEBOOK,
                    views: [
                        {
                            viewUrl: "/panel/binder"
                        }
                    ]
                },
                {
                    stateName: enums.AppStates.KNOWLEDGE_TEST,
                    views: [
                        {
                            viewUrl: "/panel/knowledgeTest"
                        }
                    ]
                },
                {
                    stateName: enums.AppStates.ADD_NOTE,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/addNote"
                        }
                    ],
                    parentStates: [ enums.AppStates.READER, enums.AppStates.NOTEBOOK ]
                },
                {
                    stateName: enums.AppStates.EDIT_NOTE,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/editNote"
                        }
                    ],
                    parentStates: [ enums.AppStates.NOTEBOOK ]
                },
                {
                    stateName: enums.AppStates.TRANSLATE,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/translate"
                        }
                    ],
                    parentStates: [ enums.AppStates.READER, enums.AppStates.NOTEBOOK ]
                },
                {
                    stateName: enums.AppStates.LOGIN,
                    views: [
                        {
                            viewName: enums.Views.LOGIN_DIALOG,
                            viewUrl: "/dialog/login"
                        },
                        {
                            viewName: enums.Views.MAIN
                        }
                    ],
                    parentStates: [ enums.AppStates.READER, enums.AppStates.NOTEBOOK ]
                },
                {
                    stateName: enums.AppStates.SETTINGS,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/settings"
                        }
                    ],
                    parentStates: [ enums.AppStates.NOTEBOOK, enums.AppStates.KNOWLEDGE_TEST ]
                },
                {
                    stateName: enums.AppStates.ADD_NOTEBOOK,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/addNotebook"
                        }
                    ],
                    parentStates: [ enums.AppStates.NOTEBOOK, enums.AppStates.KNOWLEDGE_TEST ]
                },
                {
                    stateName: enums.AppStates.ADD_PAGE,
                    views: [
                        {
                            viewName: enums.Views.MODAL_DIALOG,
                            viewUrl: "/dialog/addPage"
                        }
                    ],
                    parentStates: [ enums.AppStates.NOTEBOOK ]
                },
                {
                    stateName: enums.AppStates.KNOWLEDGE_TEST_HINT,
                    views: [
                        {
                            absoluteViewName: {
                                viewName: enums.Views.KNOWLEDGE_TEST_HINT,
                                stateName: enums.AppStates.KNOWLEDGE_TEST
                            },
                            viewUrl: "/dialog/knowledgeTestHint"
                        }
                    ],
                    parentStates: [ enums.AppStates.KNOWLEDGE_TEST ]
                }
            ],

            additionalViews: [
                "/module/notebook",
                "/messages/pageMessages",
                "/messages/formMessages",
                "/dialog/abstractNote",
                "/panel/notebookHeader",
                "/directive/lgNote",
                "/directive/lgPage",
                "/directive/lgTestEntry",
                "/directive/lgCurrentNotebookSelector",
                "/other/pageFooter",
                "/other/feedbackButton"
            ],

            additionalWhitelistUrls: [
                    properties.pagesRoot + "/loginFrame",
                    properties.pagesRoot + "/settings"
            ]
        };
    });
