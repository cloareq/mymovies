const Boom = require('boom');
const Users = require('./../models/users');
const UsersMovies = require('./../models/usersMovies');
const MovieDB = require('moviedb')('');

/* eslint-disable valid-jsdoc */

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
		const userMovies = await UsersMovies.get({
			id_user: request.params.idUser,
		});

		if (!user) {
			reply(Boom.notFound());
			return;
		}
	} catch (error) {
		logger.error(error);
		reply(Boom.badImplementation());
	}
};
