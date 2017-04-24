const Boom = require('boom');
const Users = require('./../models/users');
const UsersMovies = require('./../models/usersMovies');
const crypto = require('crypto');

const SECRET_KEY = process.env.SECRET_KEY || 'mymoviestopsecretkey';

/* eslint-disable valid-jsdoc */

exports.createUser = async function (request, reply) {
	try {
		if (request.payload.password !== request.payload.passwordCheck) {
			reply(Boom.badRequest('Passwords are not the same'));
			return;
		}

		const password = await crypto.createHmac('sha512', SECRET_KEY).update(request.payload.password).digest('hex');
		const user = await Users.create({
			nickname: request.payload.nickname,
			password,
			email:    request.payload.email,
		});

		reply({
			id: user.id,
		});
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
			id: request.params.idUser,
		});

		if (!user) {
			reply(Boom.notFound());
			return;
		}

		reply({
			id:       user.id,
			nickname: user.nickname,
			email:    user.email,
		});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

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
