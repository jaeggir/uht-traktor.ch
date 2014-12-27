(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($logProvider, $urlRouterProvider, APP_CONFIG) {

        $logProvider.debugEnabled(APP_CONFIG.releaseStage !== 'prod');

        // for all states we redirect to state 'dashboard'
        $urlRouterProvider.otherwise('/dashboard');

    });

}(window.angular));