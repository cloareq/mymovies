const Joi = require('joi');
const handler = require('./handler');

const routes = [
	{
		method:  'POST',
		path:    '/movies',
		handler: handler.addMovie,
		config:  {
			validate: {
				params: {
					idMovie: Joi.number().min(1),
					mark:    Joi.number().min(1).max(5),
					comment: Joi.string(),
				},
			},
		},
	},
	{
		method:  'PUT',
		path:    '/movies/{idMovie}',
		handler: handler.updateMovie,
		config:  {
			validate: {
				params: {
					mark:    Joi.number().min(1).max(5).optional(),
					comment: Joi.string().optional(),
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
	name:    'api.movies',
	version: '0.0.1',
};
