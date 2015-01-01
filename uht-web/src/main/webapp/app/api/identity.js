(function (angular) {
    'use strict';

    angular
        .module('uht.api')
        .factory('IdentityService', function($resource) {

            var user = null;
            $resource('/api/identity/user').get({}, function(data) {
                user = data;
            });

            return {
                getCurrentUser: function() {
                    return user;
                },

                isAuthenticated: function() {
                   return user !== null;
                },

                login: function(username, password) {
                    return $resource('/api/identity/login').save({}, {
                        username: username,
                        password: password
                    });
                },

                logout: function() {
                    //$resource('/api/identity/logout').;
                }
            };

        });
}(window.angular));