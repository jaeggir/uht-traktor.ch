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
                    $location.path('/news');
                }, function(error) {
                    console.log('login failed', error);
                });
            };

            $scope.login();

        })

        .controller('LogoutController', function LogoutController($location, IdentityService) {
            IdentityService.logout().finally(function() {
                $location.path('/news');
            });
        });

}(window.angular));