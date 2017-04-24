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
					nickname:      Joi.string().min(3),
					password:      Joi.string().min(6),
					passwordCheck: Joi.string().min(6),
					email:         Joi.string().email(),
				},
			},
		},
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
