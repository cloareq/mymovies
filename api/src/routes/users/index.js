const Joi = require('joi');
const handler = require('./handler');

const routes = [
	{
		method:  'POST',
		path:    '/users',
		handler: handler.createUser,
		config:  {
			validate: {
				payload: {
					idfb:      Joi.string().min(1),
					name:      Joi.string().min(1),
				},
			},
		},
	},
	{
		method:  'GET',
		path:    '/users',
		handler: handler.getUsers,
	},
	{
		method:  'GET',
		path:    '/users/{idUser}',
		handler: handler.getUser,
		config:  {
			validate: {
				params: {
					idUser: Joi.number().min(1),
				},
			},
		},
	},
	{
		method:  'GET',
		path:    '/users/{idUser}/movies',
		handler: handler.getUserMovies,
		config:  {
			validate: {
				params: {
					idUser: Joi.number().min(1),
				},
			},
		},
	},
];

exports.register = function (server, options, next) {
	server.route(routes);
	next();
};

exports.register.attributes = {
	name:    'api.users',
	version: '0.0.1',
};
