(function (angular) {
    'use strict';

    angular
        .module('uht.core')

        .controller('CoreController', function CoreController($scope, $location, IdentityService) {

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

            // logout and go to index page
            $scope.$on('user:auth-error', function() {
                IdentityService.logout().finally(function() {
                    $location.path('/');
                });
            });
        })

        .controller('LoginController', function LoginController($scope, $location, IdentityService) {

            $scope.user = {};

            $scope.login = function() {

                $scope.running = true;
                $scope.loginError = false;

                if (!$scope.form.$invalid) {
                    IdentityService.login($scope.user.username, $scope.user.password).then(function() {
                        $scope.running = false;
                        $location.path('/');
                    }, function() {
                        $scope.running = false;
                        $scope.loginError = true;
                    });
                }
            };

            $scope.isValid = function () {
                return !$scope.form.$invalid && !$scope.running;
            };

        })

        .controller('LogoutController', function LogoutController($location, IdentityService) {
            IdentityService.logout().finally(function() {
                $location.path('/');
            });
        });

}(window.angular));