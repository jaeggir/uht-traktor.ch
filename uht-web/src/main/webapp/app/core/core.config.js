(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($logProvider, $compileProvider, $routeProvider, APP_CONFIG) {

        $compileProvider.debugInfoEnabled(APP_CONFIG.releaseStage !== 'prod');
        $logProvider.debugEnabled(APP_CONFIG.releaseStage !== 'prod');

        $routeProvider.when('/identity/login', {
            templateUrl: 'app/core/identity.login.html',
            controller: 'LoginController'
        }).when('/identity/logout', {
            templateUrl: 'app/core/identity.logout.html',
            controller: 'LogoutController'
        });

    });

}(window.angular));