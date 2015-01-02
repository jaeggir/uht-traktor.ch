(function (angular) {
    'use strict';

    angular
        .module('uht.core')

        .controller('CoreController', function CoreController($scope, IdentityService) {
            $scope.isAuthenticated = false;
            IdentityService.getCurrentUser().then(function(data) {
                $scope.user = data;
                $scope.isAuthenticated = data.login !== 'anonymous';
            });
        })

        .controller('LoginController', function LoginController($scope, $location, IdentityService) {

            $scope.user = {
                username: 'user',
                password: 'user'
            };

            $scope.login = function() {
                IdentityService.login($scope.user.username, $scope.user.password).then(function() {
                    console.log('login succeeded');
                }, function() {
                    console.log('login failed');
                });
            };

            $scope.login();

        })

        .controller('LogoutController', function LogoutController(IdentityService) {
            IdentityService.logout();
        });

}(window.angular));