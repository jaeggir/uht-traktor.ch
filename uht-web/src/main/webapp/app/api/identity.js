(function (angular) {
    'use strict';

    angular
        .module('uht.api')
        .factory('IdentityService', function($q, $resource, $http, TokenService) {

            var user = null;
            var loadCurrentUser = function(deferred) {

                $http.get('/api/identity/user').success(function(data) {
                    user = data;
                    if (!angular.isUndefined(deferred) && deferred !== null) {
                        deferred.resolve();
                    }
                }).error(function() {
                    TokenService.removeToken();
                    if (!angular.isUndefined(deferred) && deferred !== null) {
                        deferred.reject();
                    }
                });
            };
            loadCurrentUser(null);

            return {
                getCurrentUser: function() {
                    return user;
                },

                isAuthenticated: function() {
                   return user !== null;
                },

                login: function(username, password) {

                    var deferred = $q.defer();

                    $http.post('/api/identity/login', {}, {
                        headers: {
                            'X-Auth-Username': username,
                            'X-Auth-Password': password
                        }
                    }).success(function (data, status, headers) {
                        TokenService.saveToken(headers('X-Auth-Token'));
                        loadCurrentUser(deferred);
                    }).error(function () {
                        user = null;
                        TokenService.removeToken();
                        // tell the user about it
                        deferred.reject();
                    });

                    return deferred.promise;
                },

                logout: function() {

                    var deferred = $q.defer();

                    $http.post('/api/identity/logout', {}).success(function () {
                        TokenService.removeToken();
                        deferred.resolve();
                    }).error(function () {
                        // ignore server and just remove the token from the local storage
                        TokenService.removeToken();
                        deferred.resolve();
                    });

                    return deferred.promise;
                }
            };

        });
}(window.angular));