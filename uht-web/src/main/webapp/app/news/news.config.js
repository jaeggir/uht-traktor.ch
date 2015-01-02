(function (angular) {
    'use strict';

    var news = angular.module('uht.news');

    news.config(function ($routeProvider) {

        $routeProvider.when('/news', {
            templateUrl: 'app/news/news.html',
            controller: 'NewsController'
        });

    });

}(window.angular));