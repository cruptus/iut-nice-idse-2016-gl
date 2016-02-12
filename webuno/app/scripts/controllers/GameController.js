'use strict';

angular.module('unoApp')
    .controller('GameController', ['$rootScope', '$scope', '$http', '$stateParams', 'Game', function ($rootScope, $scope, $http, $stateParams, Game) {

        Game.getUserHand($stateParams.name,$scope.user.name)
            .then(function (data) {
                $scope.cartes = data.data.cartes;
                console.log($scope.cartes);
            }, function (error) {
                $scope.error = 'Une erreur est survenue : ' + error.toString();
            });

        // TODO remplacer par Game.getGame(name)
        Game.getGame($stateParams.name)
            .then(function (response) {
                console.log(response.data);
                //$scope.fausse = {};
            }, function (error) {
                console.error(error);
            });

        $scope.piocherCarte = function () {
            $scope.cartes.push($scope.cartes[$scope.cartes.length % 8]);
        };

        $scope.jouerCarte = function (carte) {
            $scope.cartes.splice($scope.cartes.indexOf(carte), 1);
            $scope.fausse = carte;
        };
    }]);
