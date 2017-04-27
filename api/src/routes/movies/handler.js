const Boom = require('boom');
const Users = require('./../models/users');
const UsersMovies = require('./../models/usersMovies');
const MovieDB = require('moviedb')('afc5cf08a8c07f7195154de5a1990760');
/* eslint-disable valid-jsdoc */

const IMG_BASE_URL = 'http://image.tmdb.org/t/p/w185';

async function computeAverageMark(user, movieId, callback, idx) {
    const movie = await UsersMovies.getAll({id_movie: movieId});
    // Get all mark for the movie ! then compute average
    let averageMark;
    let comments = [];
    if (movie) {
        let sumMark = 0;
        movie.forEach(m => {
            sumMark += m.mark;
            if (m.idUser != user.id) {
                comments.push(m.comment);
            }
        });
        averageMark = sumMark / movie.length;
    }
    callback({
        averageMark: averageMark,
        comments: comments
    }, idx);
    return {averageMark: averageMark, comments: comments};
}

function replyMovie(request, res, reply, user) {
    try {
        let ret = [];
        let promises = [];
        res.results.forEach(function(el, idx) {
            promises.push(new Promise((resolve, reject) => {
                computeAverageMark(user, el.id, async(stat) => {
                    const userMovie = await UsersMovies.get({id_movie: el.id, id_user: user.id}) || {};
                    ret.push({
                        id: el.id,
                        title: el.title,
                        overview: el.overview,
                        original_title: el.original_title,
                        comment: userMovie.comment,
                        mark: userMovie.mark,
                        averageMark: stat.averageMark,
                        comments: stat.comments,
                        poster_path: `${IMG_BASE_URL}${el.poster_path}`
                    });
                    resolve(ret);
                });
            }));
        });
        Promise.all(promises).then(() => {
            reply(ret);
        });
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
}

/**
 * POST /movies/search
 *
 */
/**
  * @api {post} /movies/search Request search movies.
  * @apiName searchMovie
  * @apiGroup Movie
  *
  * @apiParam {String} idfb Id of the user in facebook.
	* @apiParam {String} search Text to search.
  *
  * @apiSuccess (200)
  *
  * @apiError (404) UserNotFound The id of the User was not found.
  */
exports.searchMovie = async function(request, reply) {
    try {
        const user = await Users.get({fbid: request.payload.idfb});
        MovieDB.searchMovie({
            query: request.payload.search
        }, (err, res) => {
            if (!res.results || res.results.length < 1) {
                reply([]);
                return;
            }
            replyMovie(request, res, reply, user);
        });
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
};

// GET /movies/:idfb/:idMovie
exports.getMovie = async function(request, reply) {
    try {
        const user = await Users.get({fbid: request.params.idfb});

        if (!user) {
            reply(Boom.notFound());
            return;
        }
        const movie = await UsersMovies.get({id_user: user.id, id_movie: request.params.idMovie});
        MovieDB.movieInfo({
            id: request.params.idMovie
        }, (err, res) => {
            computeAverageMark(user, res.id, (stat) => {
                reply({
                    id: res.id,
                    title: res.title,
                    overview: res.overview,
                    original_title: res.original_title,
                    comment: movie.comment,
                    mark: movie.mark,
                    averageMark: stat.averageMark,
                    comments: stat.comments,
                    poster_path: `${IMG_BASE_URL}${res.poster_path}`
                });
            });
        });
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
};

/**
 * Get my movies GET /movies/:idfb
 */
exports.getMovies = async function(request, reply) {
    try {
        const user = await Users.get({fbid: request.params.idfb});
        if (!user) {
            reply(Boom.notFound());
            return;
        }
        const userMovies = await UsersMovies.getAll({id_user: user.id});
        if (!userMovies) {
            reply([]);
            return;
        }
        let ret = [];
        let promises = [];
        userMovies.forEach((movie, idx) => {
            promises.push(new Promise((resolve, reject) => {
                MovieDB.movieInfo({id: movie.idMovie}, (err, res) => {
                    if (!res) {
                        reject(err);
                        return;
                    }
                    computeAverageMark(user, res.id, (stat, idx) => {
                        ret.push({
                            id: res.id,
                            title: res.title,
                            overview: res.overview,
                            original_title: res.original_title,
                            comment: movie.comment,
                            mark: movie.mark,
                            averageMark: stat.averageMark,
                            comments: stat.comments,
                            poster_path: `${IMG_BASE_URL}${res.poster_path}`
                        });
                        resolve(ret);
                    }, idx);
                });
            }));
        });
        Promise.all(promises).then(() => {
            reply(ret);
        })
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
};

// POST /movies/discover/:idfb
exports.discoverMovies = async function(request, reply) {
    const user = await Users.get({fbid: request.params.idfb});

    if (!user) {
        reply(Boom.notFound());
        return;
    }
    try {
        MovieDB.discoverMovie((err, res) => {
            replyMovie(request, res, reply, user);
        });
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
};

//POST /movies/idfb/idMovie
exports.updateMovie = async function(request, reply) {
    try {
        if (Object.keys(request.payload).length < 1) {
            reply(Boom.badRequest("Need comment or mark attribute"));
            return;
        }
        const user = await Users.get({fbid: request.params.idfb});

        if (!user) {
            reply(Boom.notFound());
            return;
        }

        let userMovie = await UsersMovies.get({id_user: user.id, id_movie: request.params.idMovie});
        if (userMovie) {
            userMovie = UsersMovies.update({
                id: userMovie.id,
                id_user: userMovie.idUser,
                id_movie: request.params.idMovie,
                mark: request.payload.mark || userMovie.mark,
                comment: request.payload.comment || userMovie.comment
            });
        } else {
            userMovie = UsersMovies.create({
                id_user: user.id,
                id_movie: request.params.idMovie,
                mark: request.payload.mark || 0,
                comment: request.payload.comment || ''
            });
        }
        reply(userMovie);
    } catch (error) {
        logger.error(error);
        reply(Boom.badImplementation());
    }
};
