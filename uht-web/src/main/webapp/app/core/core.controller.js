(function (angular) {
    'use strict';

    angular
        .module('uht.core')

        .controller('CoreController', function CoreController($scope, IdentityService) {

            $scope.user = IdentityService.getCurrentUser();
            $scope.isAuthenticated = IdentityService.isAuthenticated();
        })

        .controller('LoginController', function LoginController($scope, IdentityService) {

            $scope.user = {};

            $scope.login = function() {
                IdentityService.login($scope.user.username, $scope.user.password);
            };

        })

        .controller('LogoutController', function LogoutController(IdentityService) {
            IdentityService.logout();
        });

}(window.angular));