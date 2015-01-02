(function (angular) {
    'use strict';

    angular
        .module('uht.api')
        .factory('NewsService', function($http) {

            return {

                get: function() {
                    return $http.get('/api/news');
                }
            };

        });

}(window.angular));