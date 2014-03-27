define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    loginController = function($scope) {

        $scope.model = {};
        $scope.func = {};
        
        $scope.func.doLogin = function() {
            
            alert("" + $scope.model.username + $scope.model.password);
        };
    };

    linguaApp.controller("loginController", [ "$scope", loginController ]);
});
