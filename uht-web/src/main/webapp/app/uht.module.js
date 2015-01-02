(function (angular) {
    'use strict';

    // define endsWith for String
    String.prototype.endsWith = function (suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };

    // bootstrap application - resolve application properties first
    window.deferredBootstrapper.bootstrap({
        element: window.document.body,
        module: 'uht',
        moduleResolves: [{
            module: 'uht.core',
            resolve: {
                APP_CONFIG: ['$http', function ($http) {
                    return $http.get('/api/application', {
                        // set X-Auth-Token for anonymous
                        headers: {'X-Auth-Token': 'qfRRrkEVwGWaq1TyZ4IxMJ9x69Lx/xu5lk2ji2d5iUA='}
                    });
                }]
            }
        }]
    });

    angular.module('uht', [

        /* AngularJS modules */
        'ngRoute',
        'ngResource',

        /* common modules */
        'uht.core',
        'uht.api'

        /* feature modules */

    ]);

}(window.angular))  ;