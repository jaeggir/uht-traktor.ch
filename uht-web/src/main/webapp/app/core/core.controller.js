(function (angular) {
    'use strict';

    angular
        .module('uht.core')
        .controller('CoreController', function CoreController($scope, IdentityService) {

            $scope.user = IdentityService.getCurrentUser();
            $scope.isAuthenticated = IdentityService.isAuthenticated();
        });

}(window.angular));