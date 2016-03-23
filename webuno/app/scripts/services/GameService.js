'use strict';

angular.module('unoApp')
    .service('Game', function (Auth, HttpRequest) {
        return {

            /**
             * Permet la création d'une partie
             *
             * @param game
             * @param nbPlayers
             * @param callback
             * @param callbackError
             */
            createGame: function (game, nbPlayers, callback, callbackError) {
                HttpRequest.send({
                    method: 'post',
                    url: 'rest/game',
                    headers: {
                        token: Auth.getUser().token
                    },
                    data: {
                        game: game,
                        player: Auth.getUser().name,
                        numberplayers: nbPlayers
                    }
                }, callback, callbackError);
            },

            /**
             * Retourne l'état de la partie passée en paramètre
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            getGame: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'get',
                    url: 'rest/game/' + gameName,
                    headers: {
                        token: Auth.getUser().token
                    }
                }, callback, callbackError);
            },
      /**
       * Permet à un Admin de supprimer la game passée en paramètre
       *
       * @param gameName
       * @param callback
       * @param callbackError
       */
      deleteGame: function (gameName, callback, callbackError) {
        HttpRequest.send({
          method: 'delete',
          url: 'rest/admin/game/' + gameName,
          headers: {
            token: Auth.getUser().token
          }
        }, callback, callbackError);
      },

            /**
             * Retourne la liste de toutes les parties
             *
             * @param callback
             * @param callbackError
             */
            getAllGames: function (callback, callbackError) {
                HttpRequest.send({
                    method: 'get',
                    url: 'rest/game',
                    headers: {
                        token: Auth.getUser().token
                    }
                }, callback, callbackError);
            },

            /**
             * Retourne la liste de toutes mes parties
             *
             * @param callback
             * @param callbackError
             */
            getMyGames: function (callback, callbackError) {
               HttpRequest.send({
                method: 'get',
                url: 'rest/'+ Auth.getUser().name +'/games',
                headers: {
                  token: Auth.getUser().token
                }
              }, callback, callbackError);
            },

            /**
             * Retourne la main du joueur connecté
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            getUserHand: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'get',
                    url: 'rest/game/' + gameName + '/' + Auth.getUser().name,
                    headers: {
                        token: Auth.getUser().token
                    }
                }, callback, callbackError);
            },

            /**
             * Permet de rejoindre une partie
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            joinGame: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'put',
                    url: 'rest/game/' + gameName,
                    headers: {
                        token: Auth.getUser().token
                    },
                    data: {
                        playerName: Auth.getUser().name
                    }
                }, callback, callbackError);
            },

            /**
             * Permet de démarrer une partie
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            startGame: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'put',
                    url: 'rest/game/' + gameName + '/command',
                    headers: {
                        token: Auth.getUser().token
                    },
                    data: {
                        playerName: Auth.getUser().name
                    }
                }, callback, callbackError);
            },

            /**
             * Permet de piocher une carte
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            drawCard: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'post',
                    url: 'rest/game/' + gameName + '/' + Auth.getUser().name,
                    headers: {
                        token: Auth.getUser().token
                    },
                    data: {}
                }, callback, callbackError);
            },

            /**
             * Permet de jouer une carte
             *
             * @param gameName
             * @param carte
             * @param callback
             * @param callbackError
             */
            playCard: function (gameName, carte, callback, callbackError) {
                var data = {
                    value: carte.number,
                    color: carte.family
                };

                if (carte.setcolor) {
                    data.setcolor = carte.setcolor;
                }

                HttpRequest.send({
                    method: 'put',
                    url: 'rest/game/' + gameName + '/' + Auth.getUser().name,
                    headers: {
                        token: Auth.getUser().token
                    },
                    data: data
                }, callback, callbackError);
            },

            /**
             * Retourne le nom du joueur devant jouer
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            getCurrentPlayer: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'get',
                    url: 'rest/game/' + gameName + '/command',
                    headers: {
                        token: Auth.getUser().token
                    }
                }, callback, callbackError);
            },

            /**
             * Permet de quitter la room passée en paramètre
             *
             * @param gameName
             * @param callback
             * @param callbackError
             */
            quitRoom: function (gameName, callback, callbackError) {
                HttpRequest.send({
                    method: 'delete',
                    url: 'rest/game/' + gameName + '/' + Auth.getUser().name,
                    headers: {
                        token: Auth.getUser().token
                    }
                }, callback, callbackError);
            }
        };
    });
