(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($logProvider, $compileProvider, APP_CONFIG) {

        $compileProvider.debugInfoEnabled(APP_CONFIG.releaseStage !== 'prod');
        $logProvider.debugEnabled(APP_CONFIG.releaseStage !== 'prod');

    });

}(window.angular));