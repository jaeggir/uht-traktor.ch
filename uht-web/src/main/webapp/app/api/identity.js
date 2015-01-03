(function (angular) {
    'use strict';

    angular
        .module('uht.api')
        .factory('IdentityService', function($q, $rootScope, $http, TokenService) {

            var loadCurrentUser = function(deferred) {

                if (angular.isUndefined(deferred)) {
                    deferred = $q.defer();
                }

                $http.get('/api/identity/user').success(function(data) {
                    deferred.resolve(data);
                    $rootScope.$broadcast('user:login', data);
                }).error(function() {
                    TokenService.removeToken();
                    deferred.reject();
                });

                return deferred.promise;
            };

            var user = loadCurrentUser();

            return {

                getCurrentUser: function() {
                    return user;
                },

                login: function(username, password) {

                    var deferred = $q.defer();

                    $http.post('/api/identity/login', {}, {
                        headers: {
                            'X-Auth-Username': username,
                            'X-Auth-Password': password
                        }
                    }).success(function(data, status, headers) {
                        var token = headers('X-Auth-Token');
                        TokenService.saveToken(token);
                        loadCurrentUser(deferred);
                    }).error(function() {
                        user = null;
                        TokenService.removeToken();
                        deferred.reject();
                    });

                    return deferred.promise;
                },

                clear: function() {
                    TokenService.removeToken();
                    user = null;
                },

                logout: function() {

                    var deferred = $q.defer();

                    $http.post('/api/identity/logout', {}).finally(function () {
                        $rootScope.$broadcast('user:no-auth', null);
                        deferred.resolve();
                    });

                    return deferred.promise;
                }
            };

        });

}(window.angular));