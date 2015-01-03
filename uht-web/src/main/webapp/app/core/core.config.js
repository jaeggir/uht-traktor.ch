(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($httpProvider, $logProvider, $compileProvider, $routeProvider, APP_CONFIG) {

        // $http interceptor to set x-auth-token and to intercept 401 and 403
        $httpProvider.interceptors.push('sessionInjector');

        // disable debug mode in production
        $compileProvider.debugInfoEnabled(APP_CONFIG.releaseStage !== 'prod');
        $logProvider.debugEnabled(APP_CONFIG.releaseStage !== 'prod');

        // define core routes
        $routeProvider.when('/identity/login', {
            templateUrl: 'app/core/identity.login.html',
            controller: 'LoginController'
        }).when('/identity/logout', {
            templateUrl: 'app/core/identity.logout.html',
            controller: 'LogoutController'
        }).otherwise({
            redirectTo: '/news'
        });

    });

    core.factory('sessionInjector', function($q, $rootScope, TokenService) {
        return {

            'request': function(config) {

                // only add X-Auth-Token to /api/ requests except the login url /api/identity/login
                if (!config.url.startsWith('/api/identity/login')) {
                    config.headers['X-Auth-Token'] = TokenService.getToken();
                }

                return config;
            },

            'responseError': function(rejection) {

                if (!rejection.config.url.startsWith('/api/identity/login')) {

                    switch (rejection.status) {
                        case 401:
                        // same message for 401 and 403 - fall through
                        case 403:
                            $rootScope.$broadcast('user:no-auth', rejection);
                            break;
                    }

                }
                // default behaviour
                return $q.reject(rejection);
            }
        };
    });

    core.factory('TokenService', function() {
        return {

            saveToken: function(token) {
                localStorage.setItem('authToken', token);
            },

            removeToken: function() {
                localStorage.removeItem('authToken');
            },

            getToken: function() {
                var authToken = localStorage.getItem('authToken');
                if (authToken === null) {
                    return 'qfRRrkEVwGWaq1TyZ4IxMJ9x69Lx/xu5lk2ji2d5iUA='; // static token for user 'anonymous'
                } else {
                    return authToken;
                }
            }
        };
    });

}(window.angular));