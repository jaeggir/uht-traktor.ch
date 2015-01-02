(function (angular) {
    'use strict';

    angular
        .module('uht.news')

        .controller('NewsController', function CoreController($scope, NewsService) {
            NewsService.get().then(function(result) {
                $scope.news = result.data.items;
            });
        });

}(window.angular));