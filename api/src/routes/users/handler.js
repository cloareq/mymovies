const Boom = require('boom');
const Users = require('./../models/users');
const UsersMovies = require('./../models/usersMovies');

/* eslint-disable valid-jsdoc */

/**
 * @api {post} /users Request Create new user.
 * @apiName createUser
 * @apiGroup User
 *
 * @apiParam {idfb} idfb Id of the user in facebook.
 * @apiParam {name} name Name of the user.
 *
 * @apiSuccess (200)
 *
 * @apiError (404) UserNotFound The id of the User was not found.
 */
exports.createUser = async function (request, reply) {
	try {
		const idfb = request.payload.id_fb || request.payload.idfb
		if (!await Users.get({fbid: idfb})) {
			const user = await Users.create({
				fbid: idfb,
				name:    request.payload.name,
			});
		}
		reply({});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

/**
 * @api {get} /users/:idUser Request User information.
 * @apiName getUser
 * @apiGroup User
 *
 * @apiParam {Number} idUser User unique ID.
 *
 * @apiSuccess (200) {Number} id The User unique ID.
 * @apiSuccess (200) {String} nickname The nickname of the User.
 * @apiSuccess (200) {Number} email The email of the User.
 *
 * @apiError (404) UserNotFound The id of the User was not found.
 */
exports.getUser = async function (request, reply) {
	try {
		const user = await Users.get({
			fbid: request.params.idUser,
		});

		if (!user) {
			reply(Boom.notFound());
			return;
		}

		reply({
			idfb:       user.idfb,
			name:			user.name,
		});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

/**
 * @api {get} /users Get all users.
 * @apiName getUsers
 * @apiGroup User
 *
 * @apiSuccess (200) {Array} result The array which contains every users.
 * @apiSuccess (200) {Number} id The User unique ID.
 * @apiSuccess (200) {String} nickname The nickname of the User.
 * @apiSuccess (200) {Number} email The email of the User.
 *
 * @apiError (404) UserNotFound The id of the User was not found.
 */
exports.getUsers = async function (request, reply) {
	try {
		const users = await Users.getAll();

		reply(users);
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

/**
 * @api {get} /users/:idUser/movies Request User movies information.
 * @apiName getUserMovies
 * @apiGroup User
 *
 * @apiParam {Number} idUser User unique ID.
 *
 * @apiSuccess (200) {Array} result Array of the user movies.
 * @apiSuccess (200) {String} name The name of the movie.
 * @apiSuccess (200) {Number} summary The summary of the movie.
 *
 * @apiError (404) UserNotFound The id of the User was not found.
 */
exports.getUserMovies = async function (request, reply) {
	try {
		const user = await Users.get({
			id: request.params.idUser,
		});

		if (!user) {
			reply(Boom.notFound());
			return;
		}

		const userMovies = await UsersMovies.getAll({
			id_user: user.id,
		});

		reply(userMovies);
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};
