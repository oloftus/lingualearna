define(
    [
        "module/ngModule",
        "types/enums"
    ],
    function (ngModule, enums) {

        var PARENT_STATE = "^";
        var RELATIVE_STATE = ".";
        var STATE_SEPARATOR = ".";
        var PARENT_STATE_RELATIVE = PARENT_STATE + STATE_SEPARATOR;

        ngModule.factory("stateNavigator",
            [
                "$state",

                function ($state) {

                    function getParentStateName() {

                        return $state.current.name.split(".")[0];
                    }

                    function getCurrentStateName() {

                        return $state.current.name;
                    }

                    function removeParentStateInstruction(stateInstruction) {

                        return stateInstruction.substring(PARENT_STATE_RELATIVE.length, stateInstruction.length);
                    }

                    function removeRelativeStateInstruction(stateInstruction) {

                        return stateInstruction.substring(RELATIVE_STATE.length, stateInstruction.length);
                    }

                    function goToState(stateInstruction) {

                        var stateName = [];

                        if (stateInstruction.startsWith(PARENT_STATE_RELATIVE)) {
                            stateInstruction = removeParentStateInstruction(stateInstruction);
                            stateName.push(getParentStateName());
                        }
                        else if (stateInstruction.startsWith(RELATIVE_STATE)) {
                            stateInstruction = removeRelativeStateInstruction(stateInstruction);
                            stateName.push(getCurrentStateName());
                        }

                        stateName.push(stateInstruction);
                        stateName = stateName.join(STATE_SEPARATOR);

                        return $state.go(stateName);
                    }

                    function goToRelativeState(relativeStateName) {

                        return goToState(RELATIVE_STATE + relativeStateName);
                    }

                    function goToSiblingState(siblingStateName) {

                        return goToState(PARENT_STATE_RELATIVE + siblingStateName);
                    }

                    function goToParentState() {

                        return goToState(PARENT_STATE);
                    }

                    function setStartState(stateName) {

                        enums.AppStates.START = stateName;
                    }

                    return {
                        goToState: goToState,
                        goToRelativeState: goToRelativeState,
                        goToSiblingState: goToSiblingState,
                        goToParentState: goToParentState,
                        setStartState: setStartState
                    };
                }
            ])
    });
