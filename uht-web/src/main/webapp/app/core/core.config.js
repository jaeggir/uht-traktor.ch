(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($logProvider, APP_CONFIG) {

        $logProvider.debugEnabled(APP_CONFIG.releaseStage !== 'prod');

    });

}(window.angular));