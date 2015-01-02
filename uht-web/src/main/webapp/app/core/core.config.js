(function (angular) {
    'use strict';

    var core = angular.module('uht.core');

    core.config(function ($httpProvider, $logProvider, $compileProvider, $routeProvider, APP_CONFIG) {

        $httpProvider.interceptors.push('sessionInjector');

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

    core.factory('sessionInjector', function(TokenService) {
        var sessionInjector = {
            request: function(config) {
                console.log(config.url);
                if (config.url && config.url.startsWith('/api/')) {
                    config.headers['X-Auth-Token'] = TokenService.getToken();
                }
                return config;
            }
        };
        return sessionInjector;
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
        }
    });

}(window.angular));