const Boom = require('boom');
const Users = require('./../models/users');
const UsersMovies = require('./../models/usersMovies');

/* eslint-disable valid-jsdoc */

exports.searchMovie = async function (request, reply) {
	try {
		reply({});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

exports.addMovie = async function (request, reply) {
	try {
		reply({});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};

exports.updateMovie = async function (request, reply) {
	try {
		const user = await Users.get({
			id: request.params.idUser,
		});

		if (!user) {
			reply(Boom.notFound());
			return;
		}

		const userMovies = await UsersMovies.get({
			id_user: request.params.idUser,
		});

		reply({});
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};
