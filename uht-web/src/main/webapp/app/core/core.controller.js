(function (angular) {
    'use strict';

    angular
        .module('uht.core')

        .controller('CoreController', function CoreController($scope, IdentityService) {

            var updateScope = function(data) {
                $scope.user = data;
                $scope.isAuthenticated = !angular.isUndefined(data) && data !== null && data.login !== 'anonymous';
            };

            // init scope variables
            IdentityService.getCurrentUser().then(function(data) {
                updateScope(data);
            });

            // update scope variables on user change
            $scope.$on('user:updated', function(event, data) {
                updateScope(data);
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