(function (angular) {
    'use strict';

    angular.module('uht.core', [

        /* AngularJS modules */
        'ngResource',

        /* AngularUI modules */
        'ui.bootstrap',
        'ui.bootstrap.showErrors',
        'ui.router',

        /* common, reusable blocks */
        'uht.api'

        /* 3rd party modules */

    ]);

}(window.angular));